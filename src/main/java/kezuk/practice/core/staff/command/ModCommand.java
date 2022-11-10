package kezuk.practice.core.staff.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.practice.Practice;
import kezuk.practice.core.staff.items.ModItems;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;

public class ModCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (!sender.hasPermission("bawz.moderation")) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You doesn't have required permissions!");
			return false;
		}
		final Player player = (Player) sender;
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.QUEUE) || profile.getGlobalState().equals(GlobalState.PARTY) || profile.getGlobalState().equals(GlobalState.EDITOR) || profile.getGlobalState().equals(GlobalState.EVENT) || profile.getGlobalState().equals(GlobalState.FIGHT) || profile.getGlobalState().equals(GlobalState.TOURNAMENT) || profile.getGlobalState().equals(GlobalState.PARTY)) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You can't do that right now.");
			return false;
		}
		if (profile.getGlobalState().equals(GlobalState.MOD)) {
			new SpawnItems(player.getUniqueId(), false);
			player.setAllowFlight(false);
			player.setFlying(false);
			profile.setGlobalState(GlobalState.SPAWN);
			profile.getPlayerCache().getStaffCache().setVanish(false);
			profile.getPlayerCache().getStaffCache().setSilent(false);
			player.teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been left the mod mode!");
	        return false;
		}
		profile.getPlayerCache().getStaffCache().setVanish(true);
		profile.getPlayerCache().getStaffCache().setSilent(false);
		profile.setGlobalState(GlobalState.MOD);
		player.setAllowFlight(true);
		new ModItems(player.getUniqueId());
		sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been entered the mod mode!");
		return false;
	}
	
	

}
