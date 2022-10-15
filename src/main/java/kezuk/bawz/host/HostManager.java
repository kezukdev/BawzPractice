package kezuk.bawz.host;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.manager.FfaMatchManager;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.utils.MessageSerializer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.util.com.google.common.collect.Maps;

public class HostManager {
	
	private int size;
	private UUID creator;
	private UUID hostUUID;
	private HostType type;
	private HostStatus status;
	private List<UUID> members;
	private Ladders ladder;
	public static HashMap<UUID, HostManager> hosts = Maps.newHashMap();
	
	public void startHost(final UUID creatorUUID, int size, final HostType type) {
		this.startHost(creatorUUID, size, type, null);
	}
	
	public void startHost(final UUID creatorUUID, int size, final HostType type, final Ladders ladder) {
		this.size = size;
		this.creator = creatorUUID;
		this.hostUUID = UUID.randomUUID();
		this.type = type;
		this.ladder = ladder;
		this.members = Lists.newArrayList(creatorUUID);
		PlayerManager.getPlayers().get(creatorUUID).setPlayerStatus(Status.HOST);
		PlayerManager.getPlayers().get(creatorUUID).setHostUUID(hostUUID);
		Practice.getInstance().getItemsManager().giveLeaveItems(Bukkit.getServer().getPlayer(creatorUUID), "Host");
		hosts.put(hostUUID, this);
        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.WHITE + Bukkit.getServer().getPlayer(creatorUUID).getName() + ChatColor.DARK_AQUA + " have started a host " + ChatColor.WHITE + type.toString() + (ladder != null ? ChatColor.DARK_AQUA + " in " + ChatColor.WHITE + ChatColor.stripColor(ladder.displayName()) + ChatColor.DARK_AQUA + "." : ChatColor.DARK_AQUA + "."));
        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + hostUUID));
        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + type.toString() + " event!").create()));
        for (Player players : Bukkit.getOnlinePlayers()) {
        	players.spigot().sendMessage(hostMessage);
        }
        new BukkitRunnable() {
        	public void run() {
        		int cooldown = 120;
        		HostManager host = HostManager.getHosts().get(hostUUID);
        		cooldown--;
        		if (cooldown == 60) {
        	        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "The host " + ChatColor.WHITE + host.getType().toString() + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + Bukkit.getPlayer(host.getCreator()).getName() + ChatColor.DARK_AQUA + " starts in 1minutes" + (host.getLadder() != null ? ChatColor.WHITE + ChatColor.stripColor(host.getLadder().displayName()) + ChatColor.DARK_AQUA + "." : "."));
        	        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + hostUUID));
        	        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + host.getType().toString() + " event!").create()));
        	        for (Player players : Bukkit.getOnlinePlayers()) {
        	        	players.spigot().sendMessage(hostMessage);
        	        }
        		}
        		if (cooldown == 30) {
        	        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "The host " + ChatColor.WHITE + host.getType().toString() + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + Bukkit.getPlayer(host.getCreator()).getName() + ChatColor.DARK_AQUA + " starts in 30seconds" + (host.getLadder() != null ? ChatColor.WHITE + ChatColor.stripColor(host.getLadder().displayName()) + ChatColor.DARK_AQUA + "." : "."));
        	        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + hostUUID));
        	        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + host.getType().toString() + " event!").create()));
        	        for (Player players : Bukkit.getOnlinePlayers()) {
        	        	players.spigot().sendMessage(hostMessage);
        	        }
        		}
        		if (cooldown == 5 || cooldown == 4 || cooldown == 3 || cooldown == 2 || cooldown == 1) {
        			if (host.getMembers().size() == 1) {
        				for (UUID uuid : host.getMembers()) {
        					PlayerManager.getPlayers().get(uuid).sendToSpawn();
        					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.AQUA + "The host as cancelled because the host contains one player!");
        				}
        				this.cancel();
        				return;
        			}
        			for (UUID uuid : host.getMembers()) {
        				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.AQUA + "The host started in " + ChatColor.WHITE + cooldown);
        			}
        		}
        		if (cooldown <= 0) {
        			if (host.getType().equals(HostType.FFA)) {
        				for (UUID uuid : host.getMembers()) {
        					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " The host ffa as been started!");
        				}
        				FfaMatchManager match = new FfaMatchManager();
        				match.startMatch(host.getMembers(), host.getLadder());
        			}
        		}
        	}
		}.runTaskTimer(Practice.getInstance(), 2400L, 2400L);
	}
	
	public void addMember(final UUID uuid, UUID hostUUID) {
		hosts.get(hostUUID).getMembers().add(uuid);
		final Player player = Bukkit.getServer().getPlayer(uuid);
		PlayerManager.getPlayers().get(uuid).setPlayerStatus(Status.HOST);
		PlayerManager.getPlayers().get(uuid).setHostUUID(hostUUID);
		Practice.getInstance().getItemsManager().giveLeaveItems(player, "Host");
		for (UUID inHost : hosts.get(hostUUID).getMembers()) {
			Bukkit.getServer().getPlayer(inHost).sendMessage(MessageSerializer.JOIN_HOST + ChatColor.DARK_AQUA + player.getName());
		}
	}
	
	public void removeMember(final UUID uuid) {
		final UUID hostUUID = PlayerManager.getPlayers().get(uuid).getHostUUID();
		final HostManager host = hosts.get(hostUUID);
		if (host.getMembers().size() <= 1) {
			this.destroyHost(hostUUID);
			return;
		}
		PlayerManager.getPlayers().get(uuid).setHostUUID(null);
		host.getMembers().remove(uuid);
		PlayerManager.getPlayers().get(uuid).sendToSpawn();
		Bukkit.getServer().getPlayer(uuid).sendMessage(MessageSerializer.LEAVE_HOST);
	}
	
	public void destroyHost(final UUID hostUUID) {
		for (UUID inHost : hosts.get(hostUUID).getMembers()) {
			PlayerManager.getPlayers().get(inHost).sendToSpawn();
			Bukkit.getServer().getPlayer(inHost).sendMessage(MessageSerializer.DESTROY_HOST);
			PlayerManager.getPlayers().get(inHost).setHostUUID(null);
		}
		hosts.get(hostUUID).getMembers().clear();
		hosts.remove(hostUUID);
	}
	
	public UUID getHostUUID() {
		return hostUUID;
	}
	
	public HostType getType() {
		return type;
	}
	
	public UUID getCreator() {
		return creator;
	}
	
	public int getSize() {
		return size;
	}
	
	public Ladders getLadder() {
		return ladder;
	}
	
	public HostStatus getStatus() {
		return status;
	}
	
	public List<UUID> getMembers() {
		return members;
	}
	
	public static HashMap<UUID, HostManager> getHosts() {
		return hosts;
	}

}
