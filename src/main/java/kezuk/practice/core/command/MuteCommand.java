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
import kezuk.practice.Practice;
import kezuk.practice.player.Profile;

public class MuteCommand implements CommandExecutor {

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
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (args.length > 3 || args.length < 3) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/mute <player> <time> <reason>");
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
	        if (args[1].contains("d")) {
	        	final String day = args[1].replace("d", "");
	        	final int timeLeftToday = today.getDay() + Integer.valueOf(day);
	        	banExpire = new Date();
	        	banExpire.setHours(timeLeftToday * 24);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;      	
	        }
	        if (args[1].contains("h")) {
	        	final String hours = args[1].replace("h", "");
	        	final int timeHours = today.getHours() + Integer.valueOf(hours);
	        	banExpire = new Date();
	        	banExpire.setHours(timeHours);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;       	
	        }
	        if (args[1].contains("m")) {
	        	final String minutes = args[1].replace("m", "");
	        	final int timeMinutes = today.getMinutes() + Integer.valueOf(minutes);
	        	banExpire = new Date();
	        	banExpire.setMinutes(timeMinutes);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;       	
	        }
	        if (args[1].contains("s")) {
	        	final String second = args[1].replace("s", "");
	        	final int timeSecond = today.getSeconds() + Integer.valueOf(second);
	        	banExpire.setSeconds(timeSecond);
	            reason = args.length > 2 ? StringUtils.join(args, ' ', 4, args.length) : null;      	
	        }
	        String banTime = s.format(banExpire);
	        try {
	        	DB.executeUpdate("UPDATE playersdata SET muted=? WHERE name=?",  "true", args[0]);
				DB.executeUpdate("UPDATE playersdata SET muteExpires=? WHERE name=?",  banTime, args[0]);
				DB.executeUpdate("UPDATE playersdata SET muteReason=? WHERE name=?", args[2] , args[0]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Bukkit.getPlayer(args[0]) != null) {
				final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(args[0]).getUniqueId());
				try {
					profile.setMuted(true);
					profile.setMuteExpiresOn(s.parse(banTime));
					profile.setMuteReason(reason);
					Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have just received a mute penalty for the following reason: " + ChatColor.WHITE + reason);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " /unmute <player>");
				return false;
			}
	        try {
	        	Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + args[0] + ChatColor.DARK_AQUA + " will be able to reconnect in peace");
	        	DB.executeUpdate("UPDATE playersdata SET muted=? WHERE name=?",  "false", args[0]);
				DB.executeUpdate("UPDATE playersdata SET muteExpiresOn=? WHERE name=?",  "null", args[0]);
				DB.executeUpdate("UPDATE playersdata SET muteReason=? WHERE name=?", "null" , args[0]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Bukkit.getPlayer(args[0]) != null) {
				final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(args[0]).getUniqueId());
				profile.setMuted(false);
				profile.setMuteExpiresOn(null);
				profile.setMuteReason(null);
				Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your sanction has just been revoked by a member of the moderation.");
			}
		}
		return false;
	}

}