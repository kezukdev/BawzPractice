package kezuk.practice.player.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class BuildCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(sender.getName()).getUniqueId());
		if (!sender.hasPermission("bawz.build")) return false;
		if (profile.getGlobalState() != GlobalState.SPAWN) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.GRAY + "You cant do this right now!");
			return false;
		}
		if (profile.getSubState().equals(SubState.BUILD)) {
			new SpawnItems(Bukkit.getPlayer(sender.getName()).getUniqueId(), false);
			Bukkit.getPlayer(sender.getName()).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
			profile.setSubState(SubState.NOTHING);
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Build mode: " + ChatColor.WHITE + "Off");
			return false;
		}
		profile.setSubState(SubState.BUILD);
		sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Build mode: " + ChatColor.WHITE + "On");
		return false;
	}
	
	

}
