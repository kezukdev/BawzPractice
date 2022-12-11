package kezuk.practice.match.stats;

import java.util.UUID;

public class MatchStats {
    private int hits;
    private int combo;
    private int missedPotions;
    private int longestCombo;
    private int thrownPotions;
    private int round;
    private boolean winner;
    private UUID lastAttacker;
	private Long enderpearlCooldown;
	private Long nextHitTick;
    
    public MatchStats() {
        this.hits = 0;
        this.combo = 0;
        this.missedPotions = 0;
        this.longestCombo = 0;
        this.thrownPotions = 0;
        this.round = 0;
        this.enderpearlCooldown = 0L;
        this.nextHitTick = 0L;
    }
    
    public void resetStats() {
        this.hits = 0;
        this.combo = 0;
        this.lastAttacker = null;
        this.missedPotions = 0;
        this.longestCombo = 0;
        this.thrownPotions = 0;
		this.removeEnderPearlCooldown();
		this.nextHitTick = 0L;
		this.winner = false;
    }
    
    public void setWinner(boolean winner) {
		this.winner = winner;
	}
    
    public boolean isWinner() {
		return winner;
	}
    
    public void setLastAttacker(UUID lastAttacker) {
		this.lastAttacker = lastAttacker;
	}
    
    public UUID getLastAttacker() {
		return lastAttacker;
	}
    
    public void setRound(int round) {
		this.round = round;
	}
    
    public int getRound() {
		return round;
	}
    
    public int getThrownPotions() {
		return thrownPotions;
	}
    
    public void setThrownPotions(int thrownPotions) {
		this.thrownPotions = thrownPotions;
	}
    
    public int getLongestCombo() {
		return longestCombo;
	}
    
    public void setLongestCombo(int longestCombo) {
		this.longestCombo = longestCombo;
	}
    
    public int getHits() {
        return this.hits;
    }
    
    public void setHits(final int hits) {
        this.hits = hits;
    }
    
    public int getCombo() {
        return this.combo;
    }
    
    public void setCombo(final int combo) {
        this.combo = combo;
    }
    
    public int getMissedPotions() {
        return this.missedPotions;
    }
    
    public void setMissedPotions(final int missedPotions) {
        this.missedPotions = missedPotions;
    }
    
	public boolean isEnderPearlCooldownActive() {
		return this.enderpearlCooldown > System.currentTimeMillis();
	}

	public long getEnderPearlCooldown() {
		return Math.max(0L, this.enderpearlCooldown - System.currentTimeMillis());
	}

	public void applyEnderPearlCooldown() {
		this.enderpearlCooldown = Long.valueOf(System.currentTimeMillis() + 16 * 1000);
	}

	public void removeEnderPearlCooldown() {
		this.enderpearlCooldown = 0L;
	}
	
    public Long getNextHitTick() {
        return this.nextHitTick;
    }
    
    public void updateNextHitTick() {
        this.nextHitTick = System.currentTimeMillis() + 500L;
    }
}
