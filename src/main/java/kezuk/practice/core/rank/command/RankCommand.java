package kezuk.practice.core.rank.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.core.rank.Rank;
import kezuk.practice.player.Profile;

public class RankCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("bawz.rank")) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but you didn't have the required permissions!");
			return false;
		}
		if (args.length > 2 || args.length < 2) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/rank <player> <rank>");
			return false;
		}
		if (Rank.getRankByName(args[1]) == null) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Rank List:" + ChatColor.WHITE + "Owner, Platform-Admin, Developer, Senior-Mod, Mod, Builder, Trainee, Famous, Media, Strong, Pro, Challenger, Player");
			return false;
		}
		if (Bukkit.getPlayer(args[0]) != null) {
			final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(args[0]).getUniqueId());
			profile.setRank(Rank.getRankByName(args[1].toLowerCase()));
			profile.registerPermissions();
			Bukkit.getPlayer(args[0]).setPlayerListName(profile.getRank().getColor() + Bukkit.getPlayer(args[0]).getName());
		}
		final OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + target.getName() + ChatColor.DARK_AQUA + " have received the rank " + ChatColor.WHITE + args[1]);
		DB.executeUpdateAsync("UPDATE playersdata SET rank=? WHERE name=?", args[1].toLowerCase() , args[0]);
		return false;
	}

}
