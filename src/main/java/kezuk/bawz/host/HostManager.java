package kezuk.bawz.host;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.utils.MessageSerializer;
import net.minecraft.util.com.google.common.collect.Maps;

public class HostManager {
	
	private int size;
	private UUID creator;
	private UUID hostUUID;
	private HostStatus status;
	private List<UUID> members;
	public static HashMap<UUID, HostManager> hosts = Maps.newHashMap();
	
	public void startHost(final UUID creatorUUID, int size) {
		this.size = size;
		this.creator = creatorUUID;
		this.hostUUID = UUID.randomUUID();
		this.members = Lists.newArrayList(creatorUUID);
		PlayerManager.getPlayers().get(creatorUUID).setPlayerStatus(Status.HOST);
		PlayerManager.getPlayers().get(creatorUUID).setHostUUID(hostUUID);
		Practice.getInstance().getItemsManager().giveLeaveItems(Bukkit.getServer().getPlayer(creatorUUID), "Host");
		hosts.put(hostUUID, this);
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
	
	public UUID getCreator() {
		return creator;
	}
	
	public int getSize() {
		return size;
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
