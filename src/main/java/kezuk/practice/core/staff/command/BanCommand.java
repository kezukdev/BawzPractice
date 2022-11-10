package kezuk.practice.core.staff.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.aikar.idb.DB;
import kezuk.practice.utils.TimeUtils;

public class BanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("bawz.moderation")) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You don't have required permissions!");
			return false;
		}
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String reason = "Unfair Advantage";
		if (cmd.getName().equalsIgnoreCase("ban")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/ban <player> <time> <reason>");
				return false;
			}
			new TimeUtils(args[1], calendar);
            reason = args.length > 2 ? StringUtils.join(args, ' ', 2, args.length) : "Unfair Advantage";  
	        Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + args[0] + ChatColor.DARK_AQUA + " had his account suspended from the server by " + ChatColor.WHITE + sender.getName());
			DB.executeUpdateAsync("UPDATE playersdata SET banned=? WHERE name=?",  "true", args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET banExpires=? WHERE name=?",  sdf.format(calendar.getTime()), args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET banReason=? WHERE name=?", reason , args[0]);
			if (Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.GRAY + " ✴ " + ChatColor.AQUA + "This user is currently banned from the network!");
			}
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " /unban <player>");
				return false;
			}
	        Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + args[0] + ChatColor.DARK_AQUA + " will be able to reconnect in peace");
			DB.executeUpdateAsync("UPDATE playersdata SET banned=? WHERE name=?",  "false", args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET banExpires=? WHERE name=?",  "null", args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET banReason=? WHERE name=?", "null" , args[0]);
		}
		return false;
	}

}
