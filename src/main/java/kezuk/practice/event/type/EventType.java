package kezuk.practice.event.type;

public enum EventType {
	
	FFA(EventSubType.WAITTING),
	SUMO(EventSubType.WAITTING),
	OITC(EventSubType.WAITTING);
	
	private EventSubType subType;
	
	EventType(final EventSubType subType) {
		this.subType = subType;
	}
	
	public EventSubType getSubType() {
		return subType;
	}
	
	public void setSubType(EventSubType subType) {
		this.subType = subType;
	}

}
