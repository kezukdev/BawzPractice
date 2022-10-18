package kezuk.bawz.match.manager;

import com.google.common.collect.Lists;
import co.aikar.idb.DB;
import kezuk.bawz.*;
import kezuk.bawz.arena.*;
import kezuk.bawz.ladders.*;
import kezuk.bawz.match.MatchListener;
import kezuk.bawz.match.MatchSeeInventory;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.player.*;
import kezuk.bawz.utils.*;

import org.bukkit.craftbukkit.v1_7_R4.entity.*;

import java.sql.SQLException;
import java.util.*;
import org.bukkit.*;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.util.com.google.common.collect.Sets;

import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.*;

public class MatchManager {

    private Ladders ladder;
    private UUID matchUUID;
    private boolean ranked;
    private List<UUID> firstList;
    private List<UUID> secondList;
    private ArenaManager arena;
    private MatchStatus status;
    private Set<UUID> dropped;
    private List<UUID> spectator;
    
    public void startMath(final List<UUID> firstList, final List<UUID> secondList, final Ladders ladder, boolean ranked) {
    	this.matchUUID = UUID.randomUUID();
        this.ladder = ladder;
        this.ranked = ranked;
        this.firstList = Lists.newArrayList(firstList);
        this.secondList = Lists.newArrayList(secondList);
        this.status = MatchStatus.STARTING;
        this.dropped = Sets.newHashSet();
        final ArrayList<UUID> allPlayers = Lists.newArrayList(firstList);
        allPlayers.addAll(secondList);
        this.arena = ArenaManager.getRandomArena(ladder.arenaType());
        spectator = Lists.newArrayList();
        Practice.getMatchs().put(this.matchUUID, this);
        for (final UUID uuid : allPlayers) {
        	if (Practice.getInstance().getOfflineInventories().containsKey(uuid)) {
        		Practice.getInstance().getOfflineInventories().remove(uuid);
        		Bukkit.getServer().getPlayer(uuid).closeInventory();
        	}
            for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            	Bukkit.getServer().getPlayer(uuid).hidePlayer(playerOnline);
            	playerOnline.hidePlayer(Bukkit.getServer().getPlayer(uuid));
            }
            PlayerManager.getPlayers().get(uuid).setMatchUUID(this.matchUUID);
            PlayerManager.getPlayers().get(uuid).setPlayerStatus(Status.FIGHT);
            Bukkit.getServer().getPlayer(uuid).setMaximumNoDamageTicks(ladder.damageTicks());
            Bukkit.getServer().getPlayer(uuid).teleport(firstList.contains(uuid) ? this.arena.getLoc1() : this.arena.getLoc2());
            Bukkit.getServer().getPlayer(uuid).sendMessage(MessageSerializer.FOUND_OPPONENT);
            if (ladder.displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
            	Bukkit.getServer().getPlayer(uuid).addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 9999999, 1)));
            }
            if (ladder.displayName().equals(ChatColor.DARK_AQUA + "Combo")) {
            	Bukkit.getServer().getPlayer(uuid).addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
            }
            if (ranked) {
            	Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + Bukkit.getServer().getPlayer(getOpponent(uuid)).getName() + ChatColor.DARK_AQUA + " have " + ChatColor.AQUA + PlayerManager.getPlayers().get(getOpponent(uuid)).getElos()[ladder.id()] + ChatColor.DARK_AQUA + " elos!");
            }
            ((CraftPlayer)Bukkit.getServer().getPlayer(uuid)).getHandle().setKnockbackProfile(ladder.knockback());
            ((CraftLivingEntity)Bukkit.getServer().getPlayer(uuid)).getHandle().setKnockbackProfile(ladder.knockback());
            Bukkit.getServer().getPlayer(uuid).getInventory().clear();
            final Kit kit = (Kit) ladder;
            Bukkit.getServer().getPlayer(uuid).getInventory().setContents(kit.content());
            Bukkit.getServer().getPlayer(uuid).getInventory().setArmorContents(kit.armor());
            Bukkit.getServer().getPlayer(uuid).updateInventory();
        	Bukkit.getServer().getPlayer(Practice.getMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID()).getOpponent(uuid)).showPlayer(Bukkit.getServer().getPlayer(uuid));
        	Bukkit.getServer().getPlayer(uuid).showPlayer(Bukkit.getPlayer(uuid));
        	Bukkit.getServer().getPlayer(uuid).showPlayer(Bukkit.getServer().getPlayer(Practice.getMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID()).getOpponent(uuid)));

            new BukkitRunnable() {
	            int i = 5;

	            @Override
	            public void run() {
	                if (status == MatchStatus.FINISHED) {
	                    this.cancel();
	                } else {
	                	Bukkit.getServer().getPlayer(uuid).sendMessage(MessageSerializer.MATCH_START_IN + ChatColor.AQUA + i + ChatColor.DARK_AQUA + " seconds.");
	                    i -= 1;
	                    if (i <= 0) {
	                    	Practice.getMatchs().get(matchUUID).setStatus(MatchStatus.PLAYING);
	                        Bukkit.getServer().getPlayer(uuid).sendMessage(MessageSerializer.MATCH_STARTED);
	                        this.cancel();
	                    }
	                }
	            }
	        }.runTaskTimer(Practice.getInstance(), 20L, 20L);
        }
		allPlayers.clear();
    }
    
    public void endMatch(final UUID killed, final UUID killer, final UUID matchUUID, final boolean kill) {
        final List<UUID> firstList = Lists.newArrayList(killed);
        final List<UUID> secondList = Lists.newArrayList(killer);
        final ArrayList<UUID> allPlayers = Lists.newArrayList(firstList);
        allPlayers.addAll(secondList);
        Practice.getMatchs().get(matchUUID).setStatus(MatchStatus.FINISHED);
        this.clearDrops();
        final TextComponent inventoriesMessage = new TextComponent(ChatColor.GRAY + " * " + ChatColor.AQUA + "Inventories" + ChatColor.RESET + ": ");
        for (final UUID winnerUUID : secondList) {
            final TextComponent name1 = new TextComponent(ChatColor.GREEN + Bukkit.getServer().getPlayer(winnerUUID).getName());
            name1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + winnerUUID));
            name1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to view this inventory.").create()));
            inventoriesMessage.addExtra((BaseComponent)name1);
        }
        inventoriesMessage.addExtra(ChatColor.GRAY + ", ");
        for (final UUID looserUUID : firstList) {
            final TextComponent name2 = new TextComponent(ChatColor.RED + Bukkit.getServer().getPlayer(looserUUID).getName());
            name2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + looserUUID));
            name2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to view this inventory.").create()));
            inventoriesMessage.addExtra((BaseComponent)name2);
        }
        if (Practice.getMatchs().get(matchUUID).isRanked()) {
    		int winnersElo = PlayerManager.getPlayers().get(killer).getElos()[ladder.id()];
    		int losersElo = PlayerManager.getPlayers().get(killed).getElos()[ladder.id()];
    		final double expectedp = 1.0D / (1.0D + Math.pow(10.0D, (winnersElo - losersElo) / 400.0D));
    		final int scoreChange = (int) MathHelper.limit((expectedp * 32.0D), 4, 40);
    		PlayerManager.getPlayers().get(killer).getElos()[ladder.id()] += scoreChange;
    		PlayerManager.getPlayers().get(killed).getElos()[ladder.id()] -= scoreChange;
    		for (final UUID uuid2 : allPlayers) {
    			Bukkit.getServer().getPlayer(uuid2).sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + " won!");
    			Bukkit.getServer().getPlayer(uuid2).spigot().sendMessage((BaseComponent)inventoriesMessage);
    			Bukkit.getServer().getPlayer(uuid2).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Elo changes" + ChatColor.RESET + ": " + ChatColor.GREEN + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.GRAY + " (" + ChatColor.AQUA + "+" + scoreChange + ChatColor.GRAY + ") " + ChatColor.RED + Bukkit.getServer().getPlayer(killed).getName() + ChatColor.GRAY + " (" + ChatColor.AQUA + "-" + scoreChange + ChatColor.GRAY + ")");
        		PlayerManager pm = PlayerManager.getPlayers().get(uuid2);
                try {
                    final String elos = PlayerManager.getStringValue(pm.getElos(), ":");
                    DB.executeUpdate("UPDATE playersdata SET elos=? WHERE name=?", elos, Bukkit.getServer().getPlayer(uuid2).getName());
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
    		}
        }
        if(!kill) {
        	for (final UUID uuid : firstList) {
        		new MatchSeeInventory(Bukkit.getPlayer(uuid));
        	}
        	allPlayers.removeAll(firstList);
        }
        for (final UUID uuid2 : allPlayers) {
            final Player player = Bukkit.getServer().getPlayer(uuid2);
            new MatchSeeInventory(player);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.extinguish();
            player.setHealth(player.getMaxHealth());
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.updateInventory();
            for (PotionEffect effect : player.getActivePotionEffects()) {
            	player.removePotionEffect(effect.getType());
            }
            if(!kill) {
            	player.sendMessage(MessageSerializer.MATCH_FINISH_DICONNECT);
            }
            if (!Practice.getMatchs().get(matchUUID).isRanked()) {
                player.sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + " won!");
                player.spigot().sendMessage((BaseComponent)inventoriesMessage);
            }
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            new BukkitRunnable() {
                public void run() {
                    PlayerManager.getPlayers().get(uuid2).setMatchUUID(null);
                    PlayerManager.getPlayers().get(uuid2).getMatchStats().resetStats();
                    PlayerManager.getPlayers().get(uuid2).sendToSpawn();
                }
            }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 120L);
        }
        for (UUID uuid : Practice.getMatchs().get(matchUUID).getSpectator()) {
        	final Player player = Bukkit.getServer().getPlayer(uuid);
            player.sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + " won!");
            player.spigot().sendMessage((BaseComponent)inventoriesMessage);
            for (Player players : Bukkit.getOnlinePlayers()) {
            	players.showPlayer(Bukkit.getServer().getPlayer(uuid));
            	Bukkit.getServer().getPlayer(uuid).showPlayer(players);
            }
            new BukkitRunnable() {
                public void run() {
                    PlayerManager.getPlayers().get(uuid).sendToSpawn();
                    PlayerManager.getPlayers().get(uuid).setMatchUUID(null);
                }
            }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 60L);
        }
        Practice.getMatchs().get(matchUUID).getFirstList().clear();
        Practice.getMatchs().get(matchUUID).getSecondList().clear();
        Practice.getMatchs().remove(matchUUID);
        Bukkit.getPluginManager().registerListeners(Practice.getInstance(), new MatchListener());
        firstList.clear();
        secondList.clear();
        allPlayers.clear();
    }
    
    public UUID getOpponent(final UUID uuid) {
    	if (Practice.getMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID()).getFirstList().contains(uuid)) {
    		for (UUID uuid1 : Practice.getMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID()).getSecondList()) {
    			return uuid1;
    		}
    	}
    	if (Practice.getMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID()).getSecondList().contains(uuid)) {
    		for (UUID uuid1 : Practice.getMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID()).getFirstList()) {
    			return uuid1;
    		}
    	}
    	return null;
    }
    
    public void addSpectatorToMatch(final UUID uuid, final UUID targetUUID) {
    	PlayerManager pm = PlayerManager.getPlayers().get(uuid);
    	PlayerManager pmTarget = PlayerManager.getPlayers().get(targetUUID);
    	MatchManager match = Practice.getMatchs().get(pmTarget.getMatchUUID());
    	if (match == null || match.getStatus().equals(MatchStatus.FINISHED) || match.getStatus().equals(MatchStatus.STARTING) || pmTarget == null) {
    		Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "This match is not available!");
    		return;
    	}
    	if (match.getSpectator().contains(uuid)) {
    		Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You are already spectating this match!");
    		return;
    	}
    	match.getSpectator().add(uuid);
    	List<UUID> allUUID = Lists.newArrayList(match.getFirstList());
    	allUUID.addAll(match.getSecondList());
    	for (UUID uuids : allUUID) {
        	Bukkit.getServer().getPlayer(uuid).showPlayer(Bukkit.getServer().getPlayer(uuids));
        	Bukkit.getServer().getPlayer(uuids).hidePlayer(Bukkit.getServer().getPlayer(uuid));
        	Bukkit.getServer().getPlayer(uuids).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " come viewing your match.");
    	}
    	allUUID.clear();
    	Bukkit.getServer().getPlayer(uuid).teleport(Bukkit.getServer().getPlayer(targetUUID).getLocation());
    	pm.setPlayerStatus(Status.SPECTATE);
    	pm.setMatchUUID(match.getMatchUUID());
    	Bukkit.getServer().getPlayer(uuid).setAllowFlight(true);
    	Practice.getInstance().getItemsManager().giveLeaveItems(Bukkit.getServer().getPlayer(uuid), "Spectate");
    }
    
	public void addDrops(Item item) {
		this.dropped.add(item.getUniqueId());
	}
	
	public void removeDrops(Item item) {
		this.dropped.remove(item.getUniqueId());
	}
	
	public boolean containDrops(Item item) {
		return this.dropped.contains(item.getUniqueId());
	}
	
	public void clearDrops() {
		if (this.dropped.isEmpty()) {
			return;
		}
		final World world = Bukkit.getWorld("world");
		for (Entity entities : world.getEntities()) {
			if (entities == null || !(entities instanceof Item) && !this.dropped.contains(entities.getUniqueId())) continue;
			entities.remove();
		}
	}
    
    public boolean isRanked() {
		return ranked;
	}
    
    public List<UUID> getSpectator() {
		return spectator;
	}
    
    public MatchStatus getStatus() {
		return status;
	}
    
    public void setStatus(MatchStatus status) {
		this.status = status;
	}
    
    public ArenaManager getArena() {
		return arena;
	}
    
    public Ladders getLadder() {
        return this.ladder;
    }
    
    public UUID getMatchUUID() {
        return this.matchUUID;
    }
    
    public List<UUID> getFirstList() {
        return this.firstList;
    }
    
    public List<UUID> getSecondList() {
        return this.secondList;
    }
}
