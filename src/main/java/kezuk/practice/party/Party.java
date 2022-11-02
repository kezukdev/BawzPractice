package kezuk.practice.party;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.party.items.PartyItems;
import kezuk.practice.party.listener.PartyListener;
import kezuk.practice.party.state.PartyState;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import net.minecraft.util.com.google.common.collect.Maps;

public class Party {
	
	public UUID leader;
	private PartyState status;
	public List<UUID> partyList;
	public static ConcurrentMap<UUID, Party> partyMap;
	
	public Party(final UUID uuid) {
		Bukkit.getPluginManager().registerEvents(new PartyListener(), Practice.getInstance());
		this.status = PartyState.SPAWN;
		this.partyList = Lists.newArrayList();
		partyMap = Maps.newConcurrentMap();
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
		new PartyItems(Bukkit.getPlayer(uuid));
	}
	
	public void removeToParty(final UUID uuid, final boolean disconnect) {
		if (uuid == this.getLeader()) {
			if (this.partyList.size() > 1) {
				final UUID newLeader = this.partyList.get(1);
				Practice.getInstance().getRegisterCollections().getPartys().put(newLeader, new Party(newLeader));
				Practice.getInstance().getRegisterCollections().getPartys().replace(newLeader, Practice.getInstance().getRegisterCollections().getPartys().get(newLeader), Practice.getInstance().getRegisterCollections().getPartys().get(uuid));
				this.partyList.remove(uuid);
				partyMap.remove(uuid);
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have left your party the new leader is " + ChatColor.WHITE + Bukkit.getPlayer(newLeader).getName());
				for (UUID partyUUID : Practice.getInstance().getRegisterCollections().getPartys().get(uuid).getPartyList()) {
					Bukkit.getPlayer(partyUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have left the party the new leader is " + ChatColor.WHITE + Bukkit.getPlayer(newLeader).getName());
					if (Practice.getInstance().getRegisterCollections().getProfile().get(partyUUID).getSubState().equals(SubState.NOTHING)) {
						new PartyItems(Bukkit.getPlayer(newLeader));
						Practice.getInstance().getRegisterCollections().getPartys().get(newLeader).partyMap.put(partyUUID, this);
						Practice.getInstance().getRegisterCollections().getPartys().get(newLeader).partyList.add(partyUUID);
						Practice.getInstance().getRegisterCollections().getPartys().get(newLeader).leader = newLeader;
						System.out.println(partyMap);
					}
				}
				Practice.getInstance().getRegisterCollections().getPartys().remove(uuid, this);
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
	
	public static ConcurrentMap<UUID, Party> getPartyMap() {
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
