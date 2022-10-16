package kezuk.bawz.match.manager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import kezuk.bawz.Practice;
import kezuk.bawz.arena.ArenaManager;
import kezuk.bawz.host.HostStatus;
import kezuk.bawz.ladders.Kit;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.party.PartyState;
import kezuk.bawz.party.manager.PartyManager;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.utils.MessageSerializer;
import net.md_5.bungee.api.ChatColor;

public class FfaMatchManager {
	
	private List<UUID> players;
	private UUID matchUUID;
	private Ladders ladder;
	private List<UUID> alive;
	private List<UUID> spectator;
    private Set<UUID> dropped;
	private MatchStatus status;
	private ArenaManager arena;
	
	public void startMatch(final List<UUID> players, final Ladders ladder) {
		this.matchUUID = UUID.randomUUID();
		this.ladder = ladder;
		this.players = players;
		this.dropped = Sets.newConcurrentHashSet();
		this.alive = Lists.newArrayList(players);
		this.status = MatchStatus.STARTING;
		this.spectator = Lists.newArrayList();
		Practice.getFfaMatchs().put(matchUUID, this);
		this.arena = ArenaManager.getRandomArena(ladder.arenaType());
		for (UUID uuid : players) {
        	if (Practice.getInstance().getOfflineInventories().containsKey(uuid)) {
        		Practice.getInstance().getOfflineInventories().remove(uuid);
        		Bukkit.getServer().getPlayer(uuid).closeInventory();
        	}
			final Player player = Bukkit.getServer().getPlayer(uuid);
			for (FfaMatchManager match : Practice.getFfaMatchs().values()) {
				final List<UUID> allPlayers = Lists.newArrayList(match.getPlayers());
				allPlayers.removeAll(this.players);
				allPlayers.addAll(match.getSpectator());
				for (UUID lotOfPlayer : allPlayers) {
					Bukkit.getPlayer(lotOfPlayer).hidePlayer(player);
					player.hidePlayer(Bukkit.getPlayer(lotOfPlayer));
				}
			}
			player.closeInventory();
			if (PlayerManager.getPlayers().get(uuid).getPlayerStatus().equals(Status.PARTY)) {
				PartyManager.getPartyMap().get(uuid).setStatus(PartyState.FIGHT);
			}
			if (PlayerManager.getPlayers().get(uuid).getPlayerStatus().equals(Status.HOST)) {
				Practice.getHosts().get(PlayerManager.getPlayers().get(uuid).getHostUUID()).setStatus(HostStatus.PLAYING);
			}
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "FFA Match as been started with " + ChatColor.WHITE + players.size() + ChatColor.DARK_AQUA + " players!");
			player.teleport(arena.getMiddle());
			final Kit kit = (Kit) ladder;
			player.getInventory().clear();
			player.getInventory().setArmorContents(kit.armor());
			player.getInventory().setContents(kit.content());
			player.setMaximumNoDamageTicks(ladder.damageTicks());
			PlayerManager.getPlayers().get(uuid).setMatchUUID(matchUUID);
			player.updateInventory();
            ((CraftPlayer)Bukkit.getServer().getPlayer(uuid)).getHandle().setKnockbackProfile(ladder.knockback());
            ((CraftLivingEntity)Bukkit.getServer().getPlayer(uuid)).getHandle().setKnockbackProfile(ladder.knockback());
            if (ladder.displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
            	Bukkit.getServer().getPlayer(uuid).addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 9999999, 1)));
            }
            if (ladder.displayName().equals(ChatColor.DARK_AQUA + "Combo")) {
            	Bukkit.getServer().getPlayer(uuid).addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
            }
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
	                    	Practice.getFfaMatchs().get(matchUUID).setStatus(MatchStatus.PLAYING);
	                        Bukkit.getServer().getPlayer(uuid).sendMessage(MessageSerializer.MATCH_STARTED);
	                        this.cancel();
	                    }
	                }
	            }
	        }.runTaskTimer(Practice.getInstance(), 20L, 20L);
		}
	}
	
	public void addKill(final UUID uuid, final UUID killer) {
		FfaMatchManager match = Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID());
		match.getAlive().remove(uuid);
		if (match.getAlive().size() == 1) {
			for (UUID winner : match.getAlive()) {
				this.endMatch(winner);
			}
			return;
		}
		match.getPlayers().remove(uuid);
		match.getSpectator().add(uuid);
		for (UUID ig : match.getAlive()) {
			Bukkit.getPlayer(ig).hidePlayer(Bukkit.getPlayer(uuid));
		}
		for (UUID spectator : match.getSpectator()) {
			Bukkit.getPlayer(uuid).showPlayer(Bukkit.getPlayer(spectator));
		}
		Bukkit.getPlayer(uuid).setArrowsStuck(0);
		for (PotionEffect effect : Bukkit.getPlayer(uuid).getActivePotionEffects()) {
			Bukkit.getPlayer(uuid).removePotionEffect(effect.getType());
		}
		Bukkit.getPlayer(uuid).setSaturation(100000.0f);
		Bukkit.getPlayer(uuid).setAllowFlight(true);
		Bukkit.getPlayer(uuid).setFlying(true);
		Practice.getInstance().getItemsManager().giveLeaveItems(Bukkit.getPlayer(uuid), "Spectate Party Match");
		List<UUID> allInMatch = Lists.newArrayList(match.getAlive());
		allInMatch.addAll(match.getSpectator());
		for (UUID all : allInMatch) {
			if (killer != null) {
				Bukkit.getPlayer(all).sendMessage(Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have got killed by " + ChatColor.WHITE + Bukkit.getPlayer(killer).getName());	
			}
			else {
				Bukkit.getPlayer(all).sendMessage(Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " has slain!");	
			}
		}
		allInMatch.clear();
	}
	
	public void addDisconnected(final UUID uuid) {
		FfaMatchManager match = Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(uuid).getMatchUUID());
		match.getPlayers().remove(uuid);
		if (match.getAlive().contains(uuid)) {
			for (UUID uuidIG : match.getAlive()) {
				Bukkit.getPlayer(uuidIG).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.AQUA + " have disconnected.");
			}
			match.getAlive().remove(uuid);
			if (match.getAlive().size() == 1) {
				for (UUID alive : match.getAlive()) {
					this.endMatch(alive);
				}
			}
		}
		if (match.getSpectator().contains(uuid)) {
			match.getSpectator().remove(uuid);	
		}
	}
	
	public void endMatch(final UUID winnerUUID) {
		FfaMatchManager match = Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(winnerUUID).getMatchUUID());
		List<UUID> allList = Lists.newArrayList();
		allList.addAll(match.getPlayers());
		allList.addAll(match.getSpectator());
		match.setStatus(MatchStatus.FINISHED);
		if (PlayerManager.getPlayers().get(winnerUUID).getPlayerStatus().equals(Status.HOST)) {
			Practice.getHosts().get(PlayerManager.getPlayers().get(winnerUUID).getHostUUID()).setStatus(HostStatus.FINSIHED);
		}
        this.clearDrops();
		for (UUID uuid : allList) {
			Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.WHITE + Bukkit.getPlayer(winnerUUID).getName() + ChatColor.AQUA + " have won the ffa match!");
            new BukkitRunnable() {
                public void run() {
        			final PlayerManager pm = PlayerManager.getPlayers().get(uuid);
        			if (pm.getPlayerStatus().equals(Status.PARTY)) {
        				PartyManager.getPartyMap().get(uuid).setStatus(PartyState.SPAWN);
        		        Bukkit.getServer().getPlayer(uuid).teleport(new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f));
        		        Bukkit.getServer().getPlayer(uuid).setHealth(Bukkit.getServer().getPlayer(uuid).getMaxHealth());
        		        Bukkit.getServer().getPlayer(uuid).setFoodLevel(20);
        		        Bukkit.getServer().getPlayer(uuid).setSaturation(20);
        		        Bukkit.getServer().getPlayer(uuid).extinguish();
        		        Bukkit.getServer().getPlayer(uuid).setArrowsStuck(0);
        		        Bukkit.getServer().getPlayer(uuid).setMaximumNoDamageTicks(10);
        		        Bukkit.getServer().getPlayer(uuid).setLevel(0);
        		        Bukkit.getServer().getPlayer(uuid).setExp(0.0f);
        		        Bukkit.getServer().getPlayer(uuid).setAllowFlight(false);
        		        Bukkit.getServer().getPlayer(uuid).setFlying(false);
        		        Bukkit.getServer().getPlayer(uuid).getInventory().clear();
        		        Bukkit.getServer().getPlayer(uuid).getInventory().setArmorContents(null);
        		        Bukkit.getServer().getPlayer(uuid).updateInventory();
        		        for (PotionEffect effect : Bukkit.getServer().getPlayer(uuid).getActivePotionEffects()) {
        		        	Bukkit.getServer().getPlayer(uuid).removePotionEffect(effect.getType());
        		        }
        				for (UUID partyUUID : PartyManager.getPartyMap().get(uuid).getPartyList()) {
        			        Practice.getInstance().getItemsManager().givePartyItems(Bukkit.getPlayer(partyUUID));	
        				}
        				Practice.getFfaMatchs().remove(matchUUID);
        		        try {
        					this.finalize();
        				} catch (Throwable e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				return;
        			}
        			pm.sendToSpawn();
        			Practice.getFfaMatchs().remove(matchUUID);
        	        try {
        				this.finalize();
        			} catch (Throwable e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
                }
            }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 60L);
		}
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
	
	public List<UUID> getSpectator() {
		return spectator;
	}
	
	public List<UUID> getAlive() {
		return alive;
	}
	
	public ArenaManager getArena() {
		return arena;
	}
	
	public Ladders getLadder() {
		return ladder;
	}
	
	public UUID getMatchUUID() {
		return matchUUID;
	}
	
	public List<UUID> getPlayers() {
		return players;
	}
	
	public MatchStatus getStatus() {
		return status;
	}
	
	public void setStatus(MatchStatus status) {
		this.status = status;
	}
}
