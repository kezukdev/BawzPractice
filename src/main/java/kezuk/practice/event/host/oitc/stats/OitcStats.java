package kezuk.practice.event.host.oitc.stats;

public class OitcStats {
	
	private int kill;
	private int death;
	
	public OitcStats() {
		this.kill = 0;
		this.death = 0;
	}
	
	public int getKill() {
		return kill;
	}
	
	public int getDeath() {
		return death;
	}
	
	public void setKill(int kill) {
		this.kill = kill;
	}
	
	public void setDeath(int death) {
		this.death = death;
	}

}
