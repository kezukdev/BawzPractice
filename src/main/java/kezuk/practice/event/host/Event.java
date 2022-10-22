package kezuk.practice.event.host;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import kezuk.bspigot.utils.DiscordWebhook;
import kezuk.bspigot.utils.DiscordWebhook.EmbedObject;
import kezuk.practice.Practice;
import kezuk.practice.event.host.items.HostItems;
import kezuk.practice.event.host.oitc.OitcEvent;
import kezuk.practice.event.host.sumo.SumoEvent;
import kezuk.practice.event.host.task.StartRunnable;
import kezuk.practice.event.host.type.EventType;
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
	private boolean launched = false;
	private Long cooldown = 0L;
	private String prefix = ChatColor.GRAY.toString() + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "]";
	
	public SumoEvent sumoEvent;
	public OitcEvent oitcEvent;
	
	public void startHost(final UUID uuid, final EventType eventType, final Ladders ladder) {
		if (launched) {
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.AQUA + "An event is already in progress, please wait for it to end or join it if it hasn't started yet!");
			return;
		}
		if (cooldown != 0L) {
			final double time = this.getCooldown() / 1000.0D;
			final DecimalFormat df = new DecimalFormat("##.##");
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.AQUA + "The cooldown to launch an event is currently underway please be patient" + ChatColor.WHITE + ": " + df.format(time) + "seconds.");
			return;
		}
		this.creatorUUID = uuid;
		this.members = Lists.newArrayList();
		this.eventType = eventType;
		if (this.eventType.equals(EventType.FFA)) {
			this.ladder = ladder;
		}
		this.addMemberToEvent(uuid);
        final TextComponent startMessage = new TextComponent(this.prefix + ChatColor.WHITE + " " + Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " to launch an event of the type " + ChatColor.WHITE + eventType.toString());
        final TextComponent join = new TextComponent(ChatColor.GRAY + " (" + ChatColor.AQUA + "Join" + ChatColor.GRAY + ")");
        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join host"));
        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Join the event of type " + eventType.toString()).create()));
        startMessage.addExtra((BaseComponent)join);
        for(Player players : Bukkit.getOnlinePlayers()) {
        	players.spigot().sendMessage(startMessage);
        }
        this.launched = true;
        this.sendEmbed();
		new StartRunnable(120);
	}
	
	public void addMemberToEvent(final UUID uuid) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		profile.setGlobalState(GlobalState.EVENT);
		this.members.add(uuid);
		new HostItems(uuid);
		for(UUID uuids : members) {
			Bukkit.getPlayer(uuids).sendMessage(this.prefix + ChatColor.WHITE + " " + Bukkit.getPlayer(uuid).getName() + ChatColor.AQUA + " have joined the event! " + ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + this.members.size() + ChatColor.GRAY + ")");
		}
	}
	
	public void removeMemberToEvent(final UUID uuid, final boolean disconnected) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		if(!disconnected) {
			profile.setSubState(SubState.NOTHING);
			profile.setGlobalState(GlobalState.SPAWN);
			new SpawnItems(uuid);
			Bukkit.getPlayer(uuid).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());			
		}
		if (this.members.size() == 1) {
			this.setLaunched(false);
			this.members.remove(uuid);
			try {
				this.finalize();
				new Event();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		this.members.remove(uuid);
		for(UUID uuids : members) {
			if (this.creatorUUID == uuids) {
				final UUID newLeader = this.members.get(0);
				this.creatorUUID = newLeader;
				return;
			}
			Bukkit.getPlayer(uuids).sendMessage(this.prefix + ChatColor.WHITE + " " + Bukkit.getPlayer(uuid).getName() + ChatColor.AQUA + " have left the event! " + ChatColor.GRAY + "(" + ChatColor.RED + this.members.size() + ChatColor.GRAY + ")");
		}
	}
	
	private void sendEmbed() {
		try {
			DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1031183031022657557/iUL3mbkNXzzEV0bdOPpclu-u6wL4YtxoTvZl5gxNw8RK90hG5EbZUCqAbRWse2MnVIHo");
			EmbedObject embed = new EmbedObject();
			embed.setTitle("   🪸  EVENT  🪸  ");
			embed.addField("Author: ", Bukkit.getPlayer(creatorUUID).getName(), false);
			embed.addField("Type: ", eventType.toString(), false);
			embed.addField("IP: ", "bawz.eu", false);
			embed.setFooter("Will start once 5 players have joined!", null);
			embed.setColor(Color.CYAN);
			webhook.addEmbed(embed);
			webhook.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isLaunched() {
		return launched;
	}
	
	public void setLaunched(boolean launched) {
		this.launched = launched;
	}
	
	public long getCooldown() {
		return Math.max(0L, this.cooldown - System.currentTimeMillis());
	}

	public void applyCooldown() {
		this.cooldown = Long.valueOf(System.currentTimeMillis() + 300 * 1000);
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
