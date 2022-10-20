package kezuk.practice.request.actions;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;

public class Accepting {

	public Accepting(final UUID requested, final UUID requester) {
		if (Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest() != null && Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester) != null) {
			if (Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getRequestCooldown() <= 0L) {
				Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The request from " + ChatColor.WHITE + Bukkit.getPlayer(requester).getName() + ChatColor.AQUA + " have expired!");
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().remove(requester);
				return;
			}
			Bukkit.getPlayer(requested).sendMessage(ChatColor.AQUA + (Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getLadder() == "Party" ? "Party" : "Duel") + " request accepted!");
			Bukkit.getPlayer(requester).sendMessage(ChatColor.AQUA + (Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getLadder() == "Party" ? "Party" : "Duel") + " request accepted!");
			if (Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getLadder() != "Party") {
				List<UUID> firstList = Lists.newArrayList(requested);
				List<UUID> secondList = Lists.newArrayList(requester);
				new StartMatch(firstList, secondList, null, Ladders.getLadder(Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).getLadder()), false, false);
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).removeRequestCooldown();
				Practice.getInstance().getRegisterCollections().getProfile().get(requester).setTargetDuel(null);
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().clear();
				firstList.clear();
				secondList.clear();
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().clear();
			}
			else {
				Practice.getInstance().getRegisterCollections().getPartys().get(requester).addToParty(requested);
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().get(requester).removeRequestCooldown();
				Practice.getInstance().getRegisterCollections().getProfile().get(requested).getRequest().clear();
			}
		}
		else {
			Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You doesn't have any invitation from this player!");
		}
	}
}
