package kezuk.practice.event.host.sumo;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.SubState;

public class SumoEvent {
	
	private UUID firstUUID;
	private UUID secondUUID;
	private List<UUID> alives;
	
	public SumoEvent(final List<UUID> players) {
		this.alives = Lists.newArrayList(players);
		for (UUID alived : this.alives) {
			Bukkit.getPlayer(alived).teleport(Practice.getInstance().getRegisterCommon().getSpectatorLocation());
		}
		this.startSumo();
	}
	
	public void startSumo() {
		Collections.shuffle(this.alives);
		this.firstUUID = this.alives.get(0);
		this.secondUUID = this.alives.get(1);
		Bukkit.getPlayer(firstUUID).teleport(Practice.getInstance().getRegisterCommon().getFirstLocation());
		Bukkit.getPlayer(secondUUID).teleport(Practice.getInstance().getRegisterCommon().getSecondLocation());
		for (UUID uuids : Practice.getInstance().getRegisterObject().getEvent().getMembers()) {
			Bukkit.getPlayer(uuids).sendMessage(Practice.getInstance().getRegisterObject().getEvent().getPrefix() + ChatColor.WHITE + Bukkit.getPlayer(firstUUID).getName() + ChatColor.DARK_AQUA + " vs " + ChatColor.WHITE + Bukkit.getPlayer(secondUUID).getName());
		}
		Practice.getInstance().getRegisterCollections().getProfile().get(firstUUID).getGlobalState().setSubState(SubState.STARTING);
		Practice.getInstance().getRegisterCollections().getProfile().get(secondUUID).getGlobalState().setSubState(SubState.STARTING);
		new BukkitRunnable() {
			int i = 5;
			@Override
        	public void run() {
				i =- 1;
				Bukkit.getPlayer(firstUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Match starts in " + ChatColor.WHITE + i + ChatColor.DARK_AQUA + "seconds!" );
				Bukkit.getPlayer(secondUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Match starts in " + ChatColor.WHITE + i + ChatColor.DARK_AQUA + "seconds!" );
				if (i <= 0) {
					Bukkit.getPlayer(firstUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Good luck!");
					Bukkit.getPlayer(secondUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Good luck!");
					Practice.getInstance().getRegisterCollections().getProfile().get(firstUUID).getGlobalState().setSubState(SubState.PLAYING);
					Practice.getInstance().getRegisterCollections().getProfile().get(secondUUID).getGlobalState().setSubState(SubState.PLAYING);
					this.cancel();
				}
			}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}
	
	public void newRound(final UUID winner, final UUID looser) {
		final Profile winnerProfile = Practice.getInstance().getRegisterCollections().getProfile().get(winner);
		final Profile looserProfile = Practice.getInstance().getRegisterCollections().getProfile().get(looser);
		winnerProfile.getGlobalState().setSubState(SubState.NOTHING);
		looserProfile.getGlobalState().setSubState(SubState.NOTHING);
		if (this.alives.size() == 1) {
			Practice.getInstance().getRegisterObject().getEvent().applyCooldown();
			Practice.getInstance().getRegisterObject().getEvent().setLaunched(false);
			Bukkit.broadcastMessage(Practice.getInstance().getRegisterObject().getEvent().getPrefix() + ChatColor.WHITE + " " + Bukkit.getPlayer(winner).getName() + ChatColor.DARK_AQUA + " won the sumo event!");
			for (UUID uuid : Practice.getInstance().getRegisterObject().getEvent().getMembers()) {
				new SpawnItems(uuid);
				Bukkit.getPlayer(uuid).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
			}
			return;
		}
		this.alives.remove(looser);
		Bukkit.getPlayer(winner).teleport(Practice.getInstance().getRegisterCommon().getSpectatorLocation());
		Bukkit.getPlayer(looser).teleport(Practice.getInstance().getRegisterCommon().getSpectatorLocation());
		this.startSumo();
	}
	
	public UUID getFirstUUID() {
		return firstUUID;
	}
	
	public UUID getSecondUUID() {
		return secondUUID;
	}
	
	public List<UUID> getAlives() {
		return alives;
	}

}
