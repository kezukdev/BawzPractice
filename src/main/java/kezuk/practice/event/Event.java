package kezuk.practice.event;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.event.oitc.OitcEvent;
import kezuk.practice.event.sumo.SumoEvent;
import kezuk.practice.event.task.StartRunnable;
import kezuk.practice.event.type.EventType;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Event {
	
	private List<UUID> members;
	private UUID creatorUUID;
	private EventType eventType;
	private Ladders ladder;
	private String prefix = ChatColor.GRAY.toString() + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "]";
	
	public SumoEvent sumoEvent;
	public OitcEvent oitcEvent;
	
	public void startHost(final UUID uuid, final EventType eventType, final Ladders ladder) {
		this.creatorUUID = uuid;
		this.members = Lists.newArrayList();
		this.eventType = eventType;
		if (this.eventType.equals(EventType.FFA)) {
			this.ladder = ladder;
		}
		this.addMemberToEvent(uuid);
        final TextComponent startMessage = new TextComponent(this.prefix + ChatColor.WHITE + " " + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " to launch an event of the type " + ChatColor.WHITE + eventType.toString());
        final TextComponent join = new TextComponent(ChatColor.GRAY + " (" + ChatColor.AQUA + "Join" + ChatColor.GRAY + ")");
        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + Bukkit.getPlayer(uuid).getUniqueId()));
        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Join the event of type " + eventType.toString()).create()));
        startMessage.addExtra((BaseComponent)join);
        for(Player players : Bukkit.getOnlinePlayers()) {
        	players.spigot().sendMessage(startMessage);
        }
	}
	
	public void addMemberToEvent(final UUID uuid) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		profile.setGlobalState(GlobalState.EVENT);
		this.members.add(uuid);
		for(UUID uuids : members) {
			if (this.creatorUUID == uuids) {
				return;
			}
			Bukkit.getPlayer(uuids).sendMessage(this.prefix + ChatColor.WHITE + " " + Bukkit.getPlayer(uuids).getName() + ChatColor.AQUA + " have joined the event! " + ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + this.members.size() + ChatColor.GRAY + ")");
		}
		if (members.size() == 5) {
			new StartRunnable(120);
		}
	}
	
	public void removeMemberToEvent(final UUID uuid) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		profile.getGlobalState().setSubState(SubState.NOTHING);
		profile.setGlobalState(GlobalState.SPAWN);
		new SpawnItems(uuid);
		this.members.remove(uuid);
		for(UUID uuids : members) {
			if (this.creatorUUID == uuids) {
				final UUID newLeader = this.members.get(0);
				this.creatorUUID = newLeader;
				return;
			}
			Bukkit.getPlayer(uuids).sendMessage(this.prefix + ChatColor.WHITE + " " + Bukkit.getPlayer(uuids).getName() + ChatColor.AQUA + " have left the event! " + ChatColor.GRAY + "(" + ChatColor.RED + this.members.size() + ChatColor.GRAY + ")");
		}
	}
	
	public Ladders getLadder() {
		return ladder;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public SumoEvent getSumoEvent() {
		return sumoEvent;
	}
	
	public OitcEvent getOitcEvent() {
		return oitcEvent;
	}
	
	public UUID getCreatorUUID() {
		return creatorUUID;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public List<UUID> getMembers() {
		return members;
	}

}
