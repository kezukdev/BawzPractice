package kezuk.practice.request.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.request.actions.Deny;

public class DenyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (args.length == 1) {
			final Player target = Bukkit.getServer().getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
				return false;
			}
			final Player player = (Player) sender;
			final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
			if (pm.getGlobalState() != GlobalState.SPAWN) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			new Deny(player.getUniqueId(), target.getUniqueId());
		}
		else {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/deny <player>");
		}
		return false;
	}

}
