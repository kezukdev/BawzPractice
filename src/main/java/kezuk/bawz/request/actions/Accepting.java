package kezuk.bawz.request.actions;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.player.PlayerManager;

public class Accepting {

	public Accepting(final UUID requested, final UUID requester) {
		if (PlayerManager.getPlayers().get(requested).getRequest() != null && PlayerManager.getPlayers().get(requested).getRequest().get(requester) != null) {
			if (!PlayerManager.getPlayers().get(requested).haveDuelCooldownActive()) {
				Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The duel request from " + ChatColor.WHITE + Bukkit.getPlayer(requester).getName() + ChatColor.AQUA + " have expired!");
				PlayerManager.getPlayers().get(requested).getRequest().remove(requester);
				return;
			}
			Bukkit.getPlayer(requested).sendMessage(ChatColor.AQUA + "Duel request accepted!");
			Bukkit.getPlayer(requester).sendMessage(ChatColor.AQUA + "Duel request accepted!");
			MatchManager match = new MatchManager();
			List<UUID> firstList = Lists.newArrayList(requested);
			List<UUID> secondList = Lists.newArrayList(requester);
			match.startMath(firstList, secondList, Ladders.getLadder(PlayerManager.getPlayers().get(requested).getRequest().get(requester).getLadder()), false);
			PlayerManager.getPlayers().get(requester).removeDuelCooldown();
			PlayerManager.getPlayers().get(requester).setTargetDuel(null);
			PlayerManager.getPlayers().get(requested).getRequest().clear();
			firstList.clear();
			secondList.clear();
		}
		else {
			Bukkit.getPlayer(requested).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You doesn't have any invitation from this player!");
		}
	}
}
