package kezuk.bawz.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.MatchManager;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.request.DuelRequestStatus;
import kezuk.bawz.request.RequestManager;
import kezuk.bawz.utils.MessageSerializer;

public class AcceptCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (args.length > 1 || args.length < 1) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/accept <player>");
			return false;
		}
		final Player target = Bukkit.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Target isn't online.");
			return false;
		}
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		final PlayerManager pmTarget = PlayerManager.getPlayers().get(target.getUniqueId());
		if (pm.getPlayerStatus() != Status.SPAWN) {
			sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
			return false;
		}
		if (pmTarget.getTargetDuel() != player) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You didn't have request from this player");
			return false;
		}
		if (pmTarget.getDuelRequest() != DuelRequestStatus.CANNOT) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You didn't have request from this player");
			return false;
		}
		target.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + player.getName() + ChatColor.DARK_AQUA + " have accept the duel request!");
		player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have accept the duel request from " + ChatColor.AQUA + target.getName());
		MatchManager match = new MatchManager();
		final Ladders ladder = 	RequestManager.request.get(player.getUniqueId());
		List<UUID> firstList = Lists.newArrayList(player.getUniqueId());
		List<UUID> secondList = Lists.newArrayList(target.getUniqueId());
		match.startMath(firstList, secondList, ladder, false);
		firstList.clear();
		secondList.clear();
		pmTarget.setDuelRequest(DuelRequestStatus.CAN);
		pmTarget.setTargetDuel(null);
		pmTarget.setRoundable(false);
		return false;
	}

}
