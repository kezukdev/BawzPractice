package kezuk.bawz.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		event.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
		if (Bukkit.getServer().hasWhitelist()) {
			event.setMotd(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Bawz" + ChatColor.AQUA + " Network" + ChatColor.GRAY + " ~ " + ChatColor.DARK_AQUA + "22/10/2022 BETA RELEASE!\n" + ChatColor.GRAY + " * " + ChatColor.AQUA + "Follow the advance on our twitter @BawzNet");
			return;
		}
		event.setMotd(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Bawz" + ChatColor.AQUA + " Network" + ChatColor.GRAY + " ~ " + ChatColor.DARK_AQUA + "BETA\n" + ChatColor.GRAY + " * " + ChatColor.AQUA + "Follow us twitter @BawzNet");
	}

}
