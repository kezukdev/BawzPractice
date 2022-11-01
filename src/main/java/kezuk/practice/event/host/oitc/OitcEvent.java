package kezuk.practice.event.host.oitc;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.event.host.oitc.items.OitcStuff;
import kezuk.practice.event.host.oitc.stats.OitcStats;
import kezuk.practice.event.host.type.EventSubType;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class OitcEvent {
	
	private List<UUID> alive;
	
	public OitcEvent(List<UUID> players) {
		this.alive = Lists.newArrayList(players);
		for (UUID uuid : alive) {
			Practice.getInstance().getRegisterObject().getEvent().setLaunched(true);
			Practice.getInstance().getRegisterObject().getEvent().getEventType().setSubType(EventSubType.STARTED);
			Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setSubState(SubState.PLAYING);
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.DARK_AQUA + "OITC have started!");
			new OitcStuff(uuid);
			Bukkit.getPlayer(uuid).teleport(this.getRandomLocation(Practice.getInstance().getRegisterCommon().getOitcLocations()));
			Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getPlayerCache().oitcStats = new OitcStats();
		}
	}
	
	public void addKill(UUID killer, UUID killed) {
		final Profile killerProfile = Practice.getInstance().getRegisterCollections().getProfile().get(killer);
		final Profile killedProfile = Practice.getInstance().getRegisterCollections().getProfile().get(killed);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				killerProfile.getPlayerCache().getOitcStats().setKill(killerProfile.getPlayerCache().getOitcStats().getKill()+1);
				killedProfile.getPlayerCache().getOitcStats().setDeath(killedProfile.getPlayerCache().getOitcStats().getDeath()+1);
				Bukkit.getPlayer(killed).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have got killed your statistics:\n" + ChatColor.WHITE + "Kill: " + ChatColor.GREEN + killedProfile.getPlayerCache().getOitcStats().getKill() + ChatColor.WHITE + "\nDeath: " + ChatColor.RED + killedProfile.getPlayerCache().getOitcStats().getDeath());
				Bukkit.getPlayer(killer).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your statistics as upgraded\n" + ChatColor.WHITE + "Kill: " + ChatColor.GREEN + killerProfile.getPlayerCache().getOitcStats().getKill() + ChatColor.WHITE + "\nDeath: " + ChatColor.RED + killerProfile.getPlayerCache().getOitcStats().getDeath());
				if (killerProfile.getPlayerCache().getOitcStats().getKill() >= 20) {
					endOitc(killer);
					return;
				}
				new OitcStuff(killer);
				new OitcStuff(killed);
				Bukkit.getPlayer(killed).teleport(getRandomLocation(Practice.getInstance().getRegisterCommon().getOitcLocations()));
			}
		}.runTaskLaterAsynchronously(Practice.getInstance(), 2L);
	}
	
	public void endOitc(final UUID winnerUUID) {
		for (UUID uuid : alive) {
			final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);	
            profile.setSubState(SubState.NOTHING);
            Practice.getInstance().getRegisterObject().getEvent().setLaunched(false);
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (UUID uuid : alive) {
					final Player player = Bukkit.getPlayer(uuid);
					final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);	
					player.setHealth(player.getMaxHealth());
                    player.teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    new SpawnItems(player.getUniqueId(), false);
                    profile.setGlobalState(GlobalState.SPAWN);
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                    	player.removePotionEffect(effect.getType());
                    }
				}
			}
		}.runTaskLaterAsynchronously(Practice.getInstance(), 40L);
		Bukkit.broadcastMessage(ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + "OITC" + ChatColor.GRAY + ") " + ChatColor.AQUA + "The goal of 20 kills was reached! We have a winner and his name is " + ChatColor.WHITE + Bukkit.getPlayer(winnerUUID).getName());
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Location getRandomLocation(List<Location> locs) {
	    Random random = new Random();
	    return locs.get(random.nextInt(locs.size()));
	}
	
	public List<UUID> getAlive() {
		return alive;
	}

}
