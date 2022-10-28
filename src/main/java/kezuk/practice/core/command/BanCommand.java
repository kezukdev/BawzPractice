package kezuk.practice.core.command;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.aikar.idb.DB;

public class BanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("bawz.moderation")) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You don't have required permissions!");
			return false;
		}
		Date today = new Date();
    	SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	Date banExpire = null;
    	String reason = "Cheating";
		if (cmd.getName().equalsIgnoreCase("ban")) {
			if (args.length > 3 || args.length < 3) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/ban <player> <time> <reason>");
				return false;
			}
			if (args[1].contains("y")) {
	        	final String yearBefore = args[1];
	        	final String year = yearBefore.replace("y", "");
	        	final int timeLeftYear = today.getYear() + Integer.valueOf(year);
	        	banExpire = new Date();
	        	banExpire.setYear(timeLeftYear);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;      	
	        }
	        else if (args[1].contains("m")) {
	        	final String minutes = args[1].replace("m", "");
	        	if (Integer.valueOf(minutes) > 12) {
	        		sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Must provide any years at this time!");
	        		return false;
	        	}
	        	final int timeMinutes = today.getMonth() + Integer.valueOf(minutes);
	        	banExpire = new Date();
	        	banExpire.setMonth(timeMinutes);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;       	
	        }
	        else if (args[1].contains("d")) {
	        	final String day = args[1].replace("d", "");
	        	if (Integer.valueOf(day) > 31) {
	        		sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Must provide month at this time!");
	        		return false;
	        	}
	        	final int timeLeftToday = today.getDay() + Integer.valueOf(day);
	        	banExpire = new Date();
	        	banExpire.setHours(timeLeftToday * 24);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;       	
	        }
	        else if (args[1].contains("h")) {
	        	final String hours = args[1].replace("h", "");
	        	final int timeHours = today.getHours() + Integer.valueOf(hours);
	        	banExpire = new Date();
	        	banExpire.setHours(timeHours);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;      	
	        }
	        else if (args[1].contains("min")) {
	        	final String minutes = args[1].replace("min", "");
	        	final int timeMinutes = today.getMinutes() + Integer.valueOf(minutes);
	        	banExpire = new Date();
	        	banExpire.setMinutes(timeMinutes);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;       	
	        }
	        else {
	        	sender.sendMessage(ChatColor.AQUA + "Current ban format: " + ChatColor.WHITE + "y,M,d,min");
	        	return false;
	        }
	        String banTime = s.format(banExpire);
	        try {
	        	Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + args[0] + ChatColor.DARK_AQUA + " had his account suspended from the server by " + ChatColor.WHITE + sender.getName());
	        	DB.executeUpdate("UPDATE playersdata SET banned=? WHERE name=?",  "true", args[0]);
				DB.executeUpdate("UPDATE playersdata SET banExpires=? WHERE name=?",  banTime, args[0]);
				DB.executeUpdate("UPDATE playersdata SET banReason=? WHERE name=?", reason , args[0]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.GRAY + " ✴ " + ChatColor.AQUA + "Your account got suspended from the bawz network!");
			}
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " /unban <player>");
				return false;
			}
	        try {
	        	Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + args[0] + ChatColor.DARK_AQUA + " will be able to reconnect in peace");
	        	DB.executeUpdate("UPDATE playersdata SET banned=? WHERE name=?",  "false", args[0]);
				DB.executeUpdate("UPDATE playersdata SET banExpires=? WHERE name=?",  "null", args[0]);
				DB.executeUpdate("UPDATE playersdata SET banReason=? WHERE name=?", "null" , args[0]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
