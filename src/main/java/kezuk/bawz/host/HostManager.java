package kezuk.bawz.host;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.manager.FfaMatchManager;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.utils.DiscordWebhook;
import kezuk.bawz.utils.DiscordWebhook.EmbedObject;
import kezuk.bawz.utils.MessageSerializer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HostManager {
	
	private int size;
	private UUID creator;
	private UUID hostUUID;
	private UUID firstUUID;
	private UUID secondUUID;
	private HostType type;
	private HostStatus status;
	private List<UUID> members;
	private List<UUID> alivePlayer;
	private Ladders ladder;
	
	public void startHost(final UUID creatorUUID, int size, final HostType type) {
		this.startHost(creatorUUID, size, type, null);
	}
	
	public void startHost(final UUID creatorUUID, int size, final HostType type, final Ladders ladder) {
		this.size = size;
		this.creator = creatorUUID;
		this.hostUUID = UUID.randomUUID();
		this.type = type;
		this.status = HostStatus.STARTING;
		this.ladder = ladder;
		this.members = Lists.newArrayList(creatorUUID);
		DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1031183031022657557/iUL3mbkNXzzEV0bdOPpclu-u6wL4YtxoTvZl5gxNw8RK90hG5EbZUCqAbRWse2MnVIHo");
		EmbedObject embed = new EmbedObject();
		embed.setTitle(type.toString());
		embed.setDescription(Bukkit.getPlayer(creatorUUID).getName() + " have launch a event in game connect for join this!");
		embed.setColor(Color.CYAN);
		webhook.addEmbed(embed);
		PlayerManager.getPlayers().get(creatorUUID).setPlayerStatus(Status.HOST);
		PlayerManager.getPlayers().get(creatorUUID).setHostUUID(hostUUID);
		Practice.getInstance().getItemsManager().giveLeaveItems(Bukkit.getServer().getPlayer(creatorUUID), "Host");
		Practice.getHosts().put(hostUUID, this);
        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.WHITE + Bukkit.getServer().getPlayer(creatorUUID).getName() + ChatColor.DARK_AQUA + " have started a host " + ChatColor.WHITE + type.toString() + (ladder != null ? ChatColor.DARK_AQUA + " in " + ChatColor.WHITE + ChatColor.stripColor(ladder.displayName()) + ChatColor.DARK_AQUA + "." : ChatColor.DARK_AQUA + "."));
        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + hostUUID));
        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + type.toString() + " event!").create()));
        for (Player players : Bukkit.getOnlinePlayers()) {
        	players.spigot().sendMessage(hostMessage);
        }
        this.startRunnable(120);
		try {
			webhook.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addMember(final UUID uuid, UUID hostUUID) {
		if (Practice.getHosts().get(hostUUID).getStatus() != HostStatus.STARTING) {
			Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You cannot access to this host!");
			return;
		}
		Practice.getHosts().get(hostUUID).getMembers().add(uuid);
		final Player player = Bukkit.getServer().getPlayer(uuid);
		PlayerManager.getPlayers().get(uuid).setPlayerStatus(Status.HOST);
		PlayerManager.getPlayers().get(uuid).setHostUUID(hostUUID);
		Practice.getInstance().getItemsManager().giveLeaveItems(player, "Host");
		for (UUID inHost : Practice.getHosts().get(hostUUID).getMembers()) {
			Bukkit.getServer().getPlayer(inHost).sendMessage(MessageSerializer.JOIN_HOST + ChatColor.DARK_AQUA + player.getName());
		}
	}
	
	public void removeMember(final UUID uuid) {
		final UUID hostUUID = PlayerManager.getPlayers().get(uuid).getHostUUID();
		final HostManager host = Practice.getHosts().get(hostUUID);
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
		for (UUID inHost : Practice.getHosts().get(hostUUID).getMembers()) {
			PlayerManager.getPlayers().get(inHost).sendToSpawn();
			Bukkit.getServer().getPlayer(inHost).sendMessage(MessageSerializer.DESTROY_HOST);
			PlayerManager.getPlayers().get(inHost).setHostUUID(null);
		}
		Practice.getHosts().get(hostUUID).getMembers().clear();
		Practice.getHosts().remove(hostUUID);
	}
	
	public void startRunnable(int cooldowns) {
        new BukkitRunnable() {
    		int cooldown = cooldowns;	
    		@Override
        	public void run() {
        		cooldown -= 1;
        		HostManager host = Practice.getHosts().get(hostUUID);
        		if (host.getStatus() != HostStatus.STARTING) {
        			this.cancel();
        			return;
        		}
        		if (cooldown == 60) {
        	        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "The host " + ChatColor.WHITE + host.getType().toString() + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + Bukkit.getPlayer(host.getCreator()).getName() + ChatColor.DARK_AQUA + " starts in 1minutes" + (host.getLadder() != null ? ChatColor.DARK_AQUA + " in " + ChatColor.WHITE + ChatColor.stripColor(host.getLadder().displayName()) + ChatColor.DARK_AQUA + "." : "."));
        	        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + hostUUID));
        	        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + host.getType().toString() + " event!").create()));
        	        for (Player players : Bukkit.getOnlinePlayers()) {
        	        	players.sendMessage(" ");
        	        	players.spigot().sendMessage(hostMessage);
        	        	players.sendMessage(" ");
        	        }
        		}
        		if (cooldown == 30) {
        	        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "The host " + ChatColor.WHITE + host.getType().toString() + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + Bukkit.getPlayer(host.getCreator()).getName() + ChatColor.DARK_AQUA + " starts in 30seconds" + (host.getLadder() != null ? ChatColor.DARK_AQUA + " in " + ChatColor.WHITE + ChatColor.stripColor(host.getLadder().displayName()) + ChatColor.DARK_AQUA + "." : "."));
        	        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + hostUUID));
        	        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + host.getType().toString() + " event!").create()));
        	        for (Player players : Bukkit.getOnlinePlayers()) {
        	        	players.sendMessage(" ");
        	        	players.spigot().sendMessage(hostMessage);
        	        	players.sendMessage(" ");
        	        }
        		}
        		if (cooldown == 5 || cooldown == 4 || cooldown == 3 || cooldown == 2 || cooldown == 1) {
        			if (host.getMembers().size() == 1) {
        				for (UUID uuid : host.getMembers()) {
        					PlayerManager.getPlayers().get(uuid).sendToSpawn();
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.AQUA + "The host as cancelled because the host contains one player!");
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        				}
        				destroyHost(hostUUID);
        				this.cancel();
        				try {
							this.finalize();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				return;
        			}
        			for (UUID uuid : host.getMembers()) {
        				Bukkit.getPlayer(uuid).sendMessage(" ");
        				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.AQUA + "The host started in " + ChatColor.WHITE + cooldown);
        				Bukkit.getPlayer(uuid).sendMessage(" ");
        			}
        		}
        		if (cooldown <= 0) {
        			status = HostStatus.PLAYING;
        			if (host.getType().equals(HostType.FFA)) {
        				for (UUID uuid : host.getMembers()) {
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " The host ffa as been started!");
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        				}
        				FfaMatchManager match = new FfaMatchManager();
        				match.startMatch(host.getMembers(), host.getLadder());
        			}
        			if (host.getType().equals(HostType.SUMO)) {
        				startSumo();
        			}
        		}
        	}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}
	
	public void pickupTwoPlayer() {
		Collections.shuffle(alivePlayer);
		this.setFirstUUID(alivePlayer.get(0));
		this.setSecondUUID(alivePlayer.get(1));
	}
	
	public void startSumo() {
		this.alivePlayer = Lists.newArrayList(members);
		pickupTwoPlayer();
		Bukkit.getPlayer(firstUUID).teleport(getPos1());
		Bukkit.getPlayer(secondUUID).teleport(getPos2());
		for (UUID uuids : members) {
			Bukkit.getPlayer(uuids).sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.WHITE + Bukkit.getPlayer(firstUUID).getName() + ChatColor.DARK_AQUA + " vs " + ChatColor.WHITE + Bukkit.getPlayer(secondUUID).getName());
		}
		PlayerManager.getPlayers().get(firstUUID).setHostStatus(PlayerHostStatus.TELEPORTED);
		PlayerManager.getPlayers().get(secondUUID).setHostStatus(PlayerHostStatus.TELEPORTED);
		startSumoRunnable();
	}
	
	public void startSumoRunnable() {
		new BukkitRunnable() {
			int i = 5;
			@Override
        	public void run() {
				i =- 1;
				Bukkit.getPlayer(firstUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Match starts in " + ChatColor.WHITE + i + ChatColor.DARK_AQUA + "seconds!" );
				Bukkit.getPlayer(secondUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Match starts in " + ChatColor.WHITE + i + ChatColor.DARK_AQUA + "seconds!" );
				if (i <= 0) {
					Bukkit.getPlayer(firstUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Good luck!");
					Bukkit.getPlayer(secondUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Good luck!");
					PlayerManager.getPlayers().get(firstUUID).setHostStatus(PlayerHostStatus.FIGHTING);
					PlayerManager.getPlayers().get(secondUUID).setHostStatus(PlayerHostStatus.FIGHTING);
					this.cancel();
				}
			}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}
	
	public void endHostSumo(final UUID winnerUUID) {
		Bukkit.broadcastMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.WHITE + Bukkit.getPlayer(winnerUUID).getName() + ChatColor.DARK_AQUA + " have won the sumo host!");
		for (UUID uuid : members) {
			PlayerManager.getPlayers().get(uuid).sendToSpawn();
		}
	}
	
	public Location getPos1() {
		return new Location(null, size, size, size, size, size);
	}
	
	public Location getPos2() {
		return new Location(null, size, size, size, size, size);
	}
	
	public List<UUID> getAlivePlayer() {
		return alivePlayer;
	}
	
	public UUID getFirstUUID() {
		return firstUUID;
	}
	
	public UUID getSecondUUID() {
		return secondUUID;
	}
	
	public void setFirstUUID(UUID firstUUID) {
		this.firstUUID = firstUUID;
	}
	
	public void setSecondUUID(UUID secondUUID) {
		this.secondUUID = secondUUID;
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
	
	public void setStatus(HostStatus status) {
		this.status = status;
	}
	
	public List<UUID> getMembers() {
		return members;
	}

}
