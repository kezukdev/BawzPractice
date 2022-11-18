package kezuk.practice.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo.PlayerInfo;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;

public class NPCUtils {
	
	static EntityPlayer firstNPC;
	static EntityPlayer secondNPC;
	static EntityPlayer thirdNPC;
	
	public static void createNPC(final String entry, final Location location, final String displayName, final Integer id) {
		final GameProfile profile = new GameProfile(UUID.randomUUID(), displayName);
		try {
			URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + Bukkit.getOfflinePlayer(entry).getUniqueId() + "?unsigned=false");
			InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
			JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = textureProperty.get("value").getAsString();
			String signature = textureProperty.get("signature").getAsString();
			profile.getProperties().put("texture", new Property("texture", texture, signature));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final EntityPlayer entityNPC = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(),((CraftWorld)Bukkit.getWorld("world")).getHandle(), profile, new PlayerInteractManager(((CraftWorld)Bukkit.getWorld("world")).getHandle()));
		entityNPC.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		if (id == 0) {
			firstNPC = entityNPC;
			return;
		}
		if (id == 1) {
			secondNPC = entityNPC; 
			return;
		}
		if (id == 2) {
			thirdNPC = entityNPC;
			return;
		}
	}
	
	public static void showNPCtoPlayer(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
		if (firstNPC != null) {
			final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(firstNPC, PacketPlayOutPlayerInfo.PlayerInfo.REMOVE_PLAYER);
			connection.sendPacket(new PacketPlayOutPlayerInfo(firstNPC, PlayerInfo.ADD_PLAYER));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(firstNPC));
			new BukkitRunnable() {
				@Override
				public void run() {
					connection.sendPacket(packet);
				}
			}.runTaskLaterAsynchronously(Practice.getInstance(), 2L);
		}
		if (secondNPC != null) {
			final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(secondNPC, PacketPlayOutPlayerInfo.PlayerInfo.REMOVE_PLAYER);
			connection.sendPacket(new PacketPlayOutPlayerInfo(secondNPC, PlayerInfo.ADD_PLAYER));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(secondNPC));
			new BukkitRunnable() {
				@Override
				public void run() {
					connection.sendPacket(packet);
				}
			}.runTaskLaterAsynchronously(Practice.getInstance(), 2L);
		}
		if (thirdNPC != null) {
			final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(thirdNPC, PacketPlayOutPlayerInfo.PlayerInfo.REMOVE_PLAYER);
			connection.sendPacket(new PacketPlayOutPlayerInfo(thirdNPC, PlayerInfo.ADD_PLAYER));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(thirdNPC));
			new BukkitRunnable() {
				@Override
				public void run() {
					connection.sendPacket(packet);
				}
			}.runTaskLaterAsynchronously(Practice.getInstance(), 2L);
		}
	}

}
