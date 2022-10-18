package kezuk.bawz.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.request.actions.Accepting;
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
		if (pm.getPlayerStatus() != Status.SPAWN) {
			sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
			return false;
		}
		new Accepting(player.getUniqueId(), target.getUniqueId());
		return false;
	}

}
