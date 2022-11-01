package kezuk.practice.party;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.party.items.PartyItems;
import kezuk.practice.party.listener.PartyListener;
import kezuk.practice.party.state.PartyState;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import net.minecraft.util.com.google.common.collect.Maps;

public class Party {
	
	private UUID leader;
	private PartyState status;
	private List<UUID> partyList;
	public static HashMap<UUID, Party> partyMap;
	
	public Party(final UUID uuid) {
		Bukkit.getPluginManager().registerEvents(new PartyListener(), Practice.getInstance());
		this.status = PartyState.SPAWN;
		this.partyList = Lists.newArrayList();
		partyMap = Maps.newHashMap();
		this.leader = uuid;
		Practice.getInstance().getRegisterCollections().getPartys().putIfAbsent(uuid, this);
		Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have been created the party!");
		this.addToParty(uuid);
	}
	
	public void addToParty(final UUID uuid) {
		for (UUID partyUUID : this.partyList) {
			Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have joined the party!");
		}
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setGlobalState(GlobalState.PARTY);
		this.partyList.add(uuid);
		partyMap.putIfAbsent(uuid, this);
		System.out.println("TEST ADD TO PARTY!");
		new PartyItems(Bukkit.getPlayer(uuid));
	}
	
	public void removeToParty(final UUID uuid, final boolean disconnect) {
		if (uuid == this.getLeader()) {
			if (this.partyList.size() > 1) {
				final UUID newLeader = this.partyList.get(1);
				this.setLeader(newLeader);
				Practice.getInstance().getRegisterCollections().getPartys().remove(uuid);
				Practice.getInstance().getRegisterCollections().getPartys().putIfAbsent(newLeader, this);
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have left your party the new leader is " + ChatColor.WHITE + Bukkit.getPlayer(newLeader).getName());
				for (UUID partyUUID : this.partyList) {
					Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have left the party the new leader is " + ChatColor.WHITE + Bukkit.getPlayer(newLeader).getName());
				}
				this.partyList.remove(uuid);
				partyMap.remove(uuid);
			}
			if (this.partyList.size() == 1) {
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been disband your party!");
				this.partyList.clear();
				partyMap.clear();
				Practice.getInstance().getRegisterCollections().getPartys().remove(uuid, this);
			}
		}
		if (uuid != this.getLeader()) {
			this.partyList.remove(uuid);
			partyMap.remove(uuid);
			for (UUID partyUUID : this.partyList) {
				Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have left the party.");
			}
		}
		if (!disconnect) {
			new SpawnItems(uuid, false);
		}
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setGlobalState(GlobalState.SPAWN);
	}
	
	public static HashMap<UUID, Party> getPartyMap() {
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
