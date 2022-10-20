package kezuk.practice.registering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Lists;

import kezuk.practice.arena.Arena;
import kezuk.practice.match.StartMatch;
import kezuk.practice.match.inventory.MatchSeeInventory;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.queue.QueueSystem.QueueEntry;
import kezuk.practice.queue.task.EloRange;
import net.minecraft.util.com.google.common.collect.Maps;

public class RegisterCollections {
	
	public ConcurrentMap<UUID, QueueEntry> queue;
	public ConcurrentMap<UUID, EloRange> eloRange;
	private HashMap<UUID, StartMatch> matchs;
	private HashMap<UUID, Profile> profile;
	private HashMap<UUID, Party> partys;
	private ArrayList<Arena> arena;
    private Map<UUID, MatchSeeInventory> offlineInventories;
	
	public RegisterCollections() {
		this.queue = Maps.newConcurrentMap();
		this.eloRange = Maps.newConcurrentMap();
		this.matchs = Maps.newHashMap();
		this.profile = Maps.newHashMap();
		this.partys = Maps.newHashMap();
		this.arena = Lists.newArrayList();
		this.offlineInventories = Maps.newHashMap();
	}
	
	public ConcurrentMap<UUID, QueueEntry> getQueue() {
		return queue;
	}
	
	public ConcurrentMap<UUID, EloRange> getEloRange() {
		return eloRange;
	}
	
	public HashMap<UUID, StartMatch> getMatchs() {
		return matchs;
	}
	
	public HashMap<UUID, Profile> getProfile() {
		return profile;
	}
	
	public HashMap<UUID, Party> getPartys() {
		return partys;
	}
	
	public ArrayList<Arena> getArena() {
		return arena;
	}
	
	public Map<UUID, MatchSeeInventory> getOfflineInventories() {
		return offlineInventories;
	}
}