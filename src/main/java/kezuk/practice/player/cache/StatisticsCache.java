package kezuk.practice.player.cache;

import java.util.UUID;

import org.bukkit.Bukkit;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.Profile;

public class StatisticsCache {
	
	private String lastPlayedLadder;
	private boolean ranked;
	private String against;
	private Integer unrankedPlayed;
	private Integer rankedPlayed;
	private Integer unrankedWin;
	private Integer rankedWin;
	public int[] mostPlayed;
	private Integer hits;
	private Integer longestCombo;
	
	public void setAfterMatch(String ladder, boolean ranked, String opponent, boolean win, final UUID uuid) {
		this.lastPlayedLadder = ladder;
		DB.executeUpdateAsync("UPDATE playersdata SET lastLadder=? WHERE name=?", this.lastPlayedLadder, Bukkit.getServer().getPlayer(uuid).getName());
		this.ranked = ranked;
		DB.executeUpdateAsync("UPDATE playersdata SET lastRanked=? WHERE name=?", Boolean.toString(this.ranked), Bukkit.getServer().getPlayer(uuid).getName());
		this.against = opponent;
		DB.executeUpdateAsync("UPDATE playersdata SET lastOpponent=? WHERE name=?", this.against, Bukkit.getServer().getPlayer(uuid).getName());
		this.mostPlayed[Ladders.getLadder(ladder).id()] = this.mostPlayed[Ladders.getLadder(ladder).id()]+1;
        final String update = Profile.getStringValue(getMostPlayed(), ":");
		DB.executeUpdateAsync("UPDATE playersdata SET mostPlayed=? WHERE name=?", update, Bukkit.getServer().getPlayer(uuid).getName());
		if (ranked) {
			this.addRankedPlayed();
			DB.executeUpdateAsync("UPDATE playersdata SET rankedPlayed=? WHERE name=?", this.rankedPlayed, Bukkit.getServer().getPlayer(uuid).getName());
			if (win) {
				this.addRankedWin();
				DB.executeUpdateAsync("UPDATE playersdata SET rankedWin=? WHERE name=?", this.rankedWin, Bukkit.getServer().getPlayer(uuid).getName());
			}
			return;
		}
		this.addUnrankedPlayed();
		DB.executeUpdateAsync("UPDATE playersdata SET unrankedPlayed=? WHERE name=?", this.unrankedPlayed, Bukkit.getServer().getPlayer(uuid).getName());
		if (win) {
			this.addUnrankedWin();
			DB.executeUpdateAsync("UPDATE playersdata SET unrankedWin=? WHERE name=?", this.unrankedWin, Bukkit.getServer().getPlayer(uuid).getName());
		}
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getStats().setStatsInventory();
	}
	
	public Integer getHits() {
		return hits;
	}
	
	public Integer getLongestCombo() {
		return longestCombo;
	}
	
	public int[] getMostPlayed() {
		return mostPlayed;
	}
	
	public String getLastPlayedLadder() {
		return lastPlayedLadder;
	}
		
	public void setLastPlayedLadder(String lastPlayedLadder) {
		this.lastPlayedLadder = lastPlayedLadder;
	}
	
	public String getAgainst() {
		return against;
	}
	
	public Integer getUnrankedPlayed() {
		return unrankedPlayed;
	}
	
	public Integer getRankedPlayed() {
		return rankedPlayed;
	}
	
	public Integer getRankedWin() {
		return rankedWin;
	}
	
	public Integer getUnrankedWin() {
		return unrankedWin;
	}
	
	public Boolean getRanked() {
		return ranked;
	}
	
	public void addUnrankedWin() {
		this.unrankedWin = unrankedWin + 1;
	}
	
	public void addRankedPlayed() {
		this.rankedPlayed = rankedPlayed + 1;
	}
	
	public void addUnrankedPlayed() {
		this.unrankedPlayed = unrankedPlayed + 1;
	}
	
	public void addRankedWin() {
		this.rankedWin = rankedWin+1;
	}

	public void setAgainst(String against) {
		this.against = against;
	}
	
	public void setRanked(Boolean ranked) {
		this.ranked = ranked;
	}
	
	public void setRankedPlayed(Integer rankedPlayed) {
		this.rankedPlayed = rankedPlayed;
	}
	
	public void setRankedWin(Integer rankedWin) {
		this.rankedWin = rankedWin;
	}
	
	public void setUnrankedPlayed(Integer unrankedPlayed) {
		this.unrankedPlayed = unrankedPlayed;
	}
	
	public void setUnrankedWin(Integer unrankedWin) {
		this.unrankedWin = unrankedWin;
	}
}
