package kezuk.practice.registering;

import kezuk.practice.ladders.inventory.LadderInventory;
import kezuk.practice.match.spectate.SpectateInventory;
import kezuk.practice.party.inventory.ManageInventory;
import kezuk.practice.party.inventory.MatchInventory;
import kezuk.practice.player.utils.UtilsInventory;
import kezuk.practice.player.utils.leaderboard.Leaderboard;
import kezuk.practice.queue.QueueSystem;

public class RegisterObject {
	
	private QueueSystem queueSystem;
	private LadderInventory ladderInventory;
	private SpectateInventory spectateInventory;
	private Leaderboard leaderboard;
	private UtilsInventory utilsInventory;
	private MatchInventory partyMatchInventory;
	private ManageInventory partyManageInventory;
	
	public RegisterObject() {
		this.queueSystem = new QueueSystem();
		this.utilsInventory = new UtilsInventory();
		this.spectateInventory = new SpectateInventory();
		this.leaderboard = new Leaderboard();
		this.ladderInventory = new LadderInventory();
		this.partyMatchInventory = new MatchInventory();
		this.partyManageInventory = new ManageInventory();
	}
	
	public QueueSystem getQueueSystem() {
		return queueSystem;
	}
	
	public LadderInventory getLadderInventory() {
		return ladderInventory;
	}
	
	public SpectateInventory getSpectateInventory() {
		return spectateInventory;
	}
	
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
	
	public UtilsInventory getUtilsInventory() {
		return utilsInventory;
	}
	
	public MatchInventory getPartyMatchInventory() {
		return partyMatchInventory;
	}

	public ManageInventory getPartyManageInventory() {
		return partyManageInventory;
	}
}
