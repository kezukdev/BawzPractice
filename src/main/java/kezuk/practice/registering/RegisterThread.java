package kezuk.practice.registering;

import kezuk.practice.player.utils.subinventory.LeaderboardInventory;

public class RegisterThread {
	
	private Thread leaderboardThread;
	
	public RegisterThread() {
        (this.leaderboardThread = new Thread(new LeaderboardInventory())).start();
	}
	
	public Thread getLeaderboardThread() {
		return leaderboardThread;
	}

}
