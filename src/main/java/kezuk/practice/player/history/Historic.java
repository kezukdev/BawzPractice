package kezuk.practice.player.history;

import org.bukkit.entity.Player;

public class Historic {
	
	private String lastPlayedLadder;
	private Boolean ranked;
	private Player against;
	private Integer numberPlayed;
	
	public String getLastPlayedLadder() {
		return lastPlayedLadder;
	}
		
	public void setLastPlayedLadder(String lastPlayedLadder) {
		this.lastPlayedLadder = lastPlayedLadder;
	}
	
	public Player getAgainst() {
		return against;
	}
	
	public Integer getNumberPlayed() {
		return numberPlayed;
	}
	
	public Boolean getRanked() {
		return ranked;
	}
	
	public Integer addNumberPlayed() {
		int addition = numberPlayed + 1;
		numberPlayed = addition;
		return numberPlayed;
	}

	public void setAgainst(Player against) {
		this.against = against;
	}
	
	public void setRanked(Boolean ranked) {
		this.ranked = ranked;
	}
}
