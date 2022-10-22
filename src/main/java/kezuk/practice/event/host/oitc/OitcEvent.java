package kezuk.practice.event.host.oitc;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
			Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getGlobalState().setSubState(SubState.PLAYING);
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.DARK_AQUA + "OITC have started!");
			new OitcStuff(uuid);
			Bukkit.getPlayer(uuid).teleport(this.getRandomLocation(Practice.getInstance().getRegisterCommon().getOitcLocations()));
			Practice.getInstance().getRegisterCollections().getProfile().get(uuid).oitcStats = new OitcStats();
		}
	}
	
	public void addKill(UUID killer, UUID killed) {
		final Profile killerProfile = Practice.getInstance().getRegisterCollections().getProfile().get(killer);
		final Profile killedProfile = Practice.getInstance().getRegisterCollections().getProfile().get(killed);
		killedProfile.getOitcStats().setDeath(killedProfile.getOitcStats().getDeath() + 1);
		killerProfile.getOitcStats().setKill(killedProfile.getOitcStats().getKill() + 1);
		Bukkit.getPlayer(killed).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have got killed your statistics:\n" + ChatColor.WHITE + "Kill: " + ChatColor.GREEN + killedProfile.getOitcStats().getKill() + ChatColor.WHITE + "\nDeath: " + ChatColor.RED + killedProfile.getOitcStats().getDeath());
		Bukkit.getPlayer(killer).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your statistics as upgraded\n" + ChatColor.WHITE + "Kill: " + ChatColor.GREEN + killerProfile.getOitcStats().getKill() + ChatColor.WHITE + "\nDeath: " + ChatColor.RED + killerProfile.getOitcStats().getDeath());
		Bukkit.getPlayer(killer).setHealth(Bukkit.getPlayer(killer).getMaxHealth());
		if (killerProfile.getOitcStats().getKill() >= 20) {
			this.endOitc(killer);
			return;
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				new OitcStuff(killer);
				new OitcStuff(killed);
				Bukkit.getPlayer(killed).teleport(getRandomLocation(Practice.getInstance().getRegisterCommon().getOitcLocations()));
			}
		}.runTaskLaterAsynchronously(Practice.getInstance(), 20L);
	}
	
	public void endOitc(final UUID winnerUUID) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (UUID uuid : alive) {
					final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
					final Player player = Bukkit.getPlayer(uuid);
                    profile.getGlobalState().setSubState(SubState.NOTHING);
                    profile.setGlobalState(GlobalState.SPAWN);
                    player.teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
                    new SpawnItems(player.getUniqueId());
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                    	player.removePotionEffect(effect.getType());
                    }
				}
			}
		}.runTaskLaterAsynchronously(Practice.getInstance(), 40L);
		Bukkit.broadcastMessage(ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + "OITC" + ChatColor.GRAY + ") " + ChatColor.AQUA + "The goal of 20 kills was reached! We have a winner and his name is " + ChatColor.WHITE + Bukkit.getPlayer(winnerUUID).getName());
	}
	
	private Location getRandomLocation(List<Location> locs) {
	    Random random = new Random();
	    return locs.get(random.nextInt(locs.size()));
	}
	
	public List<UUID> getAlive() {
		return alive;
	}

}
