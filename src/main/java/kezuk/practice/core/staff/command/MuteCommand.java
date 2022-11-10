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
import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.utils.TimeUtils;

public class MuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("bawz.moderation")) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You don't have required permissions!");
			return false;
		}
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String reason = "Chat Violation";
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/mute <player> <time> <reason>");
				return false;
			}
			new TimeUtils(args[1], calendar);
			reason = args.length > 2 ? StringUtils.join(args, ' ', 2, args.length) : "Chat Violation";  
	        DB.executeUpdateAsync("UPDATE playersdata SET muted=? WHERE name=?",  "true", args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET muteExpires=? WHERE name=?",  sdf.format(calendar.getTime()), args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET muteReason=? WHERE name=?", reason , args[0]);
			if (Bukkit.getPlayer(args[0]) != null) {
				final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(args[0]).getUniqueId());
				profile.getPlayerCache().setMuted(true);
				profile.getPlayerCache().setMuteExpiresOn(calendar.getTime());
				profile.getPlayerCache().setMuteReason(reason);
				Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have just received a mute penalty for the following reason: " + ChatColor.WHITE + reason);
			}
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " /unmute <player>");
				return false;
			}
	        Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + args[0] + ChatColor.DARK_AQUA + " will be able to reconnect in peace");
			DB.executeUpdateAsync("UPDATE playersdata SET muted=? WHERE name=?",  "false", args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET muteExpires=? WHERE name=?",  "null", args[0]);
			DB.executeUpdateAsync("UPDATE playersdata SET muteReason=? WHERE name=?", "null" , args[0]);
			if (Bukkit.getPlayer(args[0]) != null) {
				final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(args[0]).getUniqueId());
				profile.getPlayerCache().setMuted(false);
				profile.getPlayerCache().setMuteExpiresOn(null);
				profile.getPlayerCache().setMuteReason(null);
				Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your sanction has just been revoked by a member of the moderation.");
			}
		}
		return false;
	}

}