package kezuk.bawz.request.actions;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import kezuk.bawz.player.PlayerManager;

public class Deny {
	
	public Deny(final UUID requested, final UUID requester) {
		if (PlayerManager.getPlayers().get(requested).getRequest() != null && PlayerManager.getPlayers().get(requested).getRequest().get(requester) != null) {
			if (PlayerManager.getPlayers().get(requester).getDuelCooldown() == 0L) {
				Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The duel request from " + ChatColor.WHITE + Bukkit.getPlayer(requester).getName() + ChatColor.AQUA + " have expired!");
				PlayerManager.getPlayers().get(requested).getRequest().remove(requester);
				return;
			}
			Bukkit.getPlayer(requested).sendMessage(ChatColor.AQUA + "Duel request has been denied!");
			Bukkit.getPlayer(requester).sendMessage(ChatColor.AQUA + "Duel request has not accepted!");
			PlayerManager.getPlayers().get(requested).getRequest().remove(requester);
			PlayerManager.getPlayers().get(requester).removeDuelCooldown();
		}
		else {
			Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You doesn't have any invitation from this player!");
		}
	}

}
