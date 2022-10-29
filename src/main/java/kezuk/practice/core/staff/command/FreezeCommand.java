package kezuk.practice.core.staff.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;

public class FreezeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if(!sender.hasPermission("bawz.moderation")) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You doesn't have the required permissions!");
			return false;
		}
		if (args.length > 1 || args.length < 1) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/freeze <player>");
			return false;
		}
		final Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Asked player is not found on the server!");
			return false;
		}
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId());
		if (profile.isFrozen()) {
			profile.setFrozen(false);
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have unfrozen " + ChatColor.WHITE + args[0] + ChatColor.AQUA + " !");
			target.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "A member of the moderation team unfroze you!");
			return false;
		}
		profile.setFrozen(true);
		sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have frozen " + ChatColor.WHITE + args[0]);
		target.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "A staff member has frozen you please join our discord for verification: " + ChatColor.WHITE + "discord.gg/bawz !");
		return false;
	}

}
