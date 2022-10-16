package kezuk.bawz.host;

import java.util.UUID;

public class HostStats {
	
	private UUID lastAttacker;
	
	public void setLastAttacker(UUID lastAttacker) {
		this.lastAttacker = lastAttacker;
	}
	
	public UUID getLastAttacker() {
		return lastAttacker;
	}

}
