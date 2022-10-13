package kezuk.bawz.party;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import net.minecraft.util.com.google.common.collect.Maps;

public class PartyManager {
	
	private UUID leader;
	private PartyState status;
	private List<UUID> partyList;
	private static HashMap<UUID, PartyManager> partyMap;
	
	public PartyManager(final UUID uuid) {
		this.leader = uuid;
		final PlayerManager pm = PlayerManager.getPlayers().get(uuid);
		pm.setPlayerStatus(Status.PARTY);
		Practice.getInstance().getItemsManager().givePartyItems(Bukkit.getPlayer(uuid));
		this.status = PartyState.SPAWN;
		this.partyList = Lists.newArrayList();
		partyMap = Maps.newHashMap();
		this.partyList.add(uuid);
		partyMap.put(uuid, this);
		Practice.getPartys().putIfAbsent(uuid, this);
		Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have been created the party!");
	}
	
	public void addToParty(final UUID uuid) {
		for (UUID partyUUID : this.partyList) {
			Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have joined the party!");
		}
		Practice.getInstance().getItemsManager().givePartyItems(Bukkit.getPlayer(uuid));
		PlayerManager.getPlayers().get(uuid).setPlayerStatus(Status.PARTY);
		partyMap.put(uuid, this);
		this.partyList.add(uuid);
	}
	
	public void removeToParty(final UUID uuid, final boolean disconnect) {
		if (uuid == this.getLeader()) {
			if (this.partyList.size() > 1) {
				Collections.shuffle(partyList);
				final UUID newLeader = this.partyList.get(0);
				this.setLeader(newLeader);
				this.partyList.remove(uuid);
				Practice.getPartys().remove(uuid);
				Practice.getPartys().putIfAbsent(newLeader, this);
				partyMap.remove(uuid);
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have left your party the new leader is " + ChatColor.WHITE + Bukkit.getPlayer(newLeader).getName());
				for (UUID partyUUID : this.partyList) {
					Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have left the party the new leader is " + ChatColor.WHITE + Bukkit.getPlayer(newLeader).getName());
				}
			}
			if (this.partyList.size() == 1) {
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been disband your party!");
				this.partyList.clear();
				partyMap.clear();
				Practice.getPartys().remove(uuid);
			}
		}
		if (uuid != this.getLeader()) {
			partyMap.remove(uuid);
			this.partyList.remove(uuid);
			for (UUID partyUUID : this.partyList) {
				Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have left the party.");
			}
		}
		if (!disconnect) {
			PlayerManager.getPlayers().get(uuid).sendToSpawn();
		}
	}
	
	public static HashMap<UUID, PartyManager> getPartyMap() {
		return partyMap;
	}
	
	public void setStatus(PartyState status) {
		this.status = status;
	}
	
	public PartyState getStatus() {
		return status;
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public void setLeader(UUID leader) {
		this.leader = leader;
	}
	
	public List<UUID> getPartyList() {
		return partyList;
	}

}
