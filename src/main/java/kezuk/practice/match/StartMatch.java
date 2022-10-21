package kezuk.practice.match;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.arena.Arena;
import kezuk.practice.ladders.Kit;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.stats.MatchStats;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;
import net.minecraft.util.com.google.common.collect.Sets;

public class StartMatch {
	
    private Ladders ladder;
    private UUID matchUUID;
    private boolean ranked;
    private boolean to2;
    private List<UUID> firstList;
    private int aliveFirst;
    private List<UUID> secondList;
    private int aliveSecond;
    private List<UUID> players;
    private List<UUID> alive;
    private Arena arena;
    private Set<UUID> dropped;
    private List<UUID> spectator;
	
	public StartMatch(final List<UUID> firstList, final List<UUID> secondList,final List<UUID> players, final Ladders ladder, final boolean ranked, final boolean to2) {
    	this.matchUUID = UUID.randomUUID();
        this.ladder = ladder;
        this.ranked = ranked;
        this.to2 = to2;
        if (firstList != null) {
            this.firstList = Lists.newArrayList(firstList);
            this.secondList = Lists.newArrayList(secondList);
            this.aliveFirst = firstList.size();
            this.aliveSecond = secondList.size();
            this.alive = Lists.newArrayList(firstList);
            this.alive.addAll(secondList);
        }
        if (players != null) {
        	this.players = Lists.newArrayList(players);
        	this.alive = Lists.newArrayList(players);
        }
        this.dropped = Sets.newHashSet();
        this.arena = Arena.getRandomArena(ladder.arenaType());
        spectator = Lists.newArrayList();
        for (UUID uuid : alive) {
        	final Player player = Bukkit.getPlayer(uuid);
        	GameUtils.displayMatchPlayer(player);
        }
        Practice.getInstance().getRegisterCollections().getMatchs().put(this.matchUUID, this);
        for (final UUID uuid : alive) {
        	final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
        	if (Practice.getInstance().getRegisterCollections().getOfflineInventories().containsKey(uuid)) {
        		Practice.getInstance().getRegisterCollections().getOfflineInventories().remove(uuid);
        		Bukkit.getPlayer(uuid).closeInventory();
        	}
            profile.setMatchUUID(matchUUID);
            if (profile.getGlobalState() == GlobalState.QUEUE) {
            	profile.setGlobalState(GlobalState.FIGHT);
            }
        	profile.getGlobalState().setSubState(SubState.STARTING);
            profile.matchStats = new MatchStats();
            Bukkit.getServer().getPlayer(uuid).setMaximumNoDamageTicks(ladder.damageTicks());
            if (firstList != null && !to2) {
                Bukkit.getServer().getPlayer(uuid).teleport(firstList.contains(uuid) ? this.arena.getLoc1() : this.arena.getLoc2());
                if (firstList.size() == 1 && secondList.size() == 1) {
                    Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "An opponent was found! It is about " + ChatColor.WHITE + Bukkit.getPlayer(GameUtils.getOpponent(uuid)).getName());		
                }
                if (firstList.size() > 1 || secondList.size() > 1) {
                    Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "A team fight has just been launched! This fight will be in " + ChatColor.WHITE + firstList.size() + ChatColor.DARK_AQUA + " vs " + ChatColor.WHITE + secondList.size());		
                }
            }
            if (players != null) {
            	Bukkit.getServer().getPlayer(uuid).teleport(this.arena.getMiddle());
                Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "A fight in ffa has just been launched! It currently contains " + ChatColor.WHITE + alive.size() + ChatColor.DARK_AQUA + " players!");	
            }
            if (ladder.displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
            	Bukkit.getServer().getPlayer(uuid).addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 9999999, 1)));
            }
            if (ladder.displayName().equals(ChatColor.DARK_AQUA + "Combo")) {
            	Bukkit.getServer().getPlayer(uuid).addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
            }
            if (ranked) {
            	Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + Bukkit.getServer().getPlayer(GameUtils.getOpponent(uuid)).getName() + ChatColor.DARK_AQUA + " have " + ChatColor.AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(GameUtils.getOpponent(uuid)).getElos()[ladder.id()] + ChatColor.DARK_AQUA + " elos!");
            }
            ((CraftPlayer)Bukkit.getServer().getPlayer(uuid)).getHandle().setKnockbackProfile(ladder.knockback());
            ((CraftLivingEntity)Bukkit.getServer().getPlayer(uuid)).getHandle().setKnockbackProfile(ladder.knockback());
            Bukkit.getServer().getPlayer(uuid).getInventory().clear();
            final Kit kit = (Kit) ladder;
            Bukkit.getServer().getPlayer(uuid).getInventory().setContents(kit.content());
            Bukkit.getServer().getPlayer(uuid).getInventory().setArmorContents(kit.armor());
            Bukkit.getServer().getPlayer(uuid).updateInventory();
            new BukkitRunnable() {
	            int i = 5;

	            @Override
	            public void run() {
	                if (profile.getGlobalState().getSubState().equals(SubState.FINISHED)) {
	                    this.cancel();
	                } else {
	                	Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " » " + ChatColor.WHITE + "The fight begins in " + ChatColor.AQUA + i + ChatColor.WHITE + " seconds.");
	                    i -= 1;
	                    if (i <= 0) {
	                    	profile.getGlobalState().setSubState(SubState.PLAYING);
	                        Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "The fight has begun!");
	                        this.cancel();
	                    }
	                }
	            }
	        }.runTaskTimer(Practice.getInstance(), 20L, 20L);
        }
	}
	
	public void endMatch(final UUID killed, final UUID killer, final UUID matchUUID, final boolean kill) {
		new EndMatch(killed, killer, matchUUID, kill);
	}
	
	@SuppressWarnings("deprecation")
	public void destroy() throws Throwable {
		this.finalize();
	}
	
	public int getAliveFirst() {
		return aliveFirst;
	}
	
	public int getAliveSecond() {
		return aliveSecond;
	}
	
	public void setAliveFirst(int aliveFirst) {
		this.aliveFirst = aliveFirst;
	}
	
	public void setAliveSecond(int aliveSecond) {
		this.aliveSecond = aliveSecond;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public List<UUID> getAlive() {
		return alive;
	}
	
	public Set<UUID> getDropped() {
		return dropped;
	}
	
	public Ladders getLadder() {
		return ladder;
	}
	
	public boolean isRanked() {
		return ranked;
	}
	
	public boolean isTo2() {
		return to2;
	}
	
	public List<UUID> getFirstList() {
		return firstList;
	}
	
	public List<UUID> getSecondList() {
		return secondList;
	}
	
	public List<UUID> getSpectator() {
		return spectator;
	}
	
	public List<UUID> getPlayers() {
		return players;
	}
}
