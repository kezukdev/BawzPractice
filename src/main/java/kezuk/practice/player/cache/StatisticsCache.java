package kezuk.practice.player.cache;

import org.bukkit.entity.Player;

public class StatisticsCache {
	
	private String lastPlayedLadder;
	private Boolean ranked;
	private Player against;
	private Integer unrankedPlayed;
	private Integer rankedPlayed;
	private Integer unrankedWin;
	private Integer rankedWin;
	
	public String getLastPlayedLadder() {
		return lastPlayedLadder;
	}
		
	public void setLastPlayedLadder(String lastPlayedLadder) {
		this.lastPlayedLadder = lastPlayedLadder;
	}
	
	public Player getAgainst() {
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
		this.unrankedWin = unrankedWin++;
	}
	
	public void addRankedPlayed() {
		this.rankedPlayed = rankedPlayed++;
	}
	
	public void addUnrankedPlayed() {
		this.unrankedPlayed = unrankedPlayed++;
	}
	
	public void addRankedWin() {
		this.rankedWin = rankedWin++;
	}

	public void setAgainst(Player against) {
		this.against = against;
	}
	
	public void setRanked(Boolean ranked) {
		this.ranked = ranked;
	}
}
