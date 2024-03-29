package kezuk.practice.registering;

import kezuk.practice.core.tag.inventory.TagInventory;
import kezuk.practice.event.host.Event;
import kezuk.practice.event.inventory.JoinInventory;
import kezuk.practice.ladders.inventory.LadderInventory;
import kezuk.practice.match.spectate.SpectateInventory;
import kezuk.practice.party.inventory.ManageInventory;
import kezuk.practice.party.inventory.MatchInventory;
import kezuk.practice.player.utils.UtilsInventory;
import kezuk.practice.player.utils.leaderboard.Leaderboard;
import kezuk.practice.player.utils.subinventory.HostInventory;
import kezuk.practice.queue.QueueSystem;
import kezuk.practice.queue.inventory.PotentialInventory;

public class RegisterObject {
	
	private QueueSystem queueSystem;
	private PotentialInventory queuePotential;
	private LadderInventory ladderInventory;
	private SpectateInventory spectateInventory;
	private Leaderboard leaderboard;
	private Event event;
	private UtilsInventory utilsInventory;
	private MatchInventory partyMatchInventory;
	private ManageInventory partyManageInventory;
	private HostInventory hostInventory;
	private JoinInventory joinInventory;
	private TagInventory tagInventory;
	
	public RegisterObject() {
		this.queueSystem = new QueueSystem();
		this.queuePotential = new PotentialInventory();
		this.spectateInventory = new SpectateInventory();
		this.leaderboard = new Leaderboard();
		this.event = new Event();
		this.utilsInventory = new UtilsInventory();
		this.ladderInventory = new LadderInventory();
		this.partyMatchInventory = new MatchInventory();
		this.partyManageInventory = new ManageInventory();
		this.hostInventory = new HostInventory();
		this.joinInventory = new JoinInventory();
		this.tagInventory = new TagInventory();
		
	}
	
	public TagInventory getTagInventory() {
		return tagInventory;
	}
	
	public QueueSystem getQueueSystem() {
		return queueSystem;
	}
	
	public PotentialInventory getQueuePotential() {
		return queuePotential;
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
	
	public Event getEvent() {
		return event;
	}
	
	public HostInventory getHostInventory() {
		return hostInventory;
	}
	
	public JoinInventory getJoinInventory() {
		return joinInventory;
	}
}
