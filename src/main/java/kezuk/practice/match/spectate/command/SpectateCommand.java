package kezuk.practice.match.spectate.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import kezuk.practice.Practice;
import kezuk.practice.player.state.GlobalState;

public class SpectateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (args.length > 1 || args.length < 1) {
            Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().open(player, 1);
			return false;
		}
		if (Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState() != GlobalState.SPAWN) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
			return false;
		}
		final Player target = Bukkit.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
			return false;
		}
		if (Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getMatchUUID()) == null) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "Sorry but this player does not appear to be in combat at the moment!");
			return false;
		}
		return false;
	}

}
