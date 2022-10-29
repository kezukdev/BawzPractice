package kezuk.practice.core.staff.cache;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

public class StaffCache {
	
	private boolean vanish;
	private boolean silent;
	private UUID uuid;
	
	public StaffCache(final UUID uuid) {
		this.uuid = uuid;
		this.vanish = false;
		this.silent = false;
	}
	
	public void setSilent(boolean silent) {
		if (silent == true) {
			final Player player = Bukkit.getPlayer(uuid);
	        final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(((CraftPlayer) player).getHandle(), PacketPlayOutPlayerInfo.PlayerInfo.REMOVE_PLAYER );
	        for(Player entityplayer : Bukkit.getOnlinePlayers()) {
	        	((CraftPlayer)entityplayer).getHandle().playerConnection.sendPacket(packet);
	            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(((CraftPlayer)entityplayer).getHandle(), PacketPlayOutPlayerInfo.PlayerInfo.REMOVE_PLAYER ));
	        }
		}
		if (silent == false) {
			final Player player = Bukkit.getPlayer(uuid);
	        final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(((CraftPlayer) player).getHandle(), PacketPlayOutPlayerInfo.PlayerInfo.ADD_PLAYER );
	        final PacketPlayOutPlayerInfo displayPacket = new PacketPlayOutPlayerInfo(((CraftPlayer) player).getHandle(), PacketPlayOutPlayerInfo.PlayerInfo.UPDATE_DISPLAY_NAME);
	        for(Player entityplayer : Bukkit.getOnlinePlayers()) {
	        	((CraftPlayer)entityplayer).getHandle().playerConnection.sendPacket(packet);
	            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(((CraftPlayer)entityplayer).getHandle(), PacketPlayOutPlayerInfo.PlayerInfo.ADD_PLAYER ));
	            if (!player.getName().equals(((CraftPlayer)entityplayer).getHandle().listName)) {
	            	if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() > 28) {
	            		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(((CraftPlayer)entityplayer).getHandle(), PacketPlayOutPlayerInfo.PlayerInfo.UPDATE_DISPLAY_NAME));
	            	}
	            	if (((CraftPlayer)entityplayer).getHandle().playerConnection.networkManager.getVersion() > 28) {
	            		((CraftPlayer)entityplayer).getHandle().playerConnection.sendPacket(displayPacket);
	            	}
	            }
	        }
		}
		this.silent = silent;
	}
	
	public void setVanish(boolean vanish) {
		if (vanish == true) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (!players.hasPermission("bawz.moderation")) {
					players.hidePlayer(Bukkit.getPlayer(uuid));
				}
			}
		}
		if (vanish == false) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (!players.hasPermission("bawz.moderation")) {
					players.showPlayer(Bukkit.getPlayer(uuid));
				}
			}
		}
		this.vanish = vanish;
	}

	public boolean isVanish() {
		return vanish;
	}
	
	public boolean isSilent() {
		return silent;
	}
}
