package kezuk.practice.request.actions;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import kezuk.practice.Practice;

public class Deny {
	
	public Deny(final UUID requested, final UUID requester) {
		if (Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest() != null && Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester) != null) {
			if (Practice.getInstance().getRegisterCollections().getProfile().get(requester).getRequest().get(requester).getRequestCooldown() == 0L) {
				Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The duel request from " + ChatColor.WHITE + Bukkit.getPlayer(requester).getName() + ChatColor.AQUA + " have expired!");
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().remove(requester);
				return;
			}
			Bukkit.getPlayer(requested).sendMessage(ChatColor.AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getLadder() + " request has been denied!");
			Bukkit.getPlayer(requester).sendMessage(ChatColor.AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getLadder() + " request denied.");
			Practice.getInstance().getRegisterCollections().getProfile().get(requester).getRequest().get(requester).removeRequestCooldown();
			Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().remove(requester);
		}
		else {
			Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You doesn't have any invitation from this player!");
		}
	}

}
