package kezuk.practice.player.state;

public enum GlobalState {
	
	SPAWN(SubState.NOTHING),
	QUEUE(SubState.NOTHING),
	FIGHT(SubState.NOTHING),
	SPECTATE(SubState.NOTHING),
	EVENT(SubState.NOTHING),
	EDITOR(SubState.NOTHING),
	MOD(SubState.NOTHING),
	PARTY(SubState.NOTHING);
	
	private SubState subState;
	
	GlobalState(final SubState subState) {
		this.subState = subState;
	}
	
	public SubState getSubState() {
		return subState;
	}
	
	public void setSubState(SubState subState) {
		this.subState = subState;
	}

}
