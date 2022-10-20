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

public class DuelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (args.length < 1 | args.length > 1) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/duel <player>");
			return false;
		}
		final Player target = Bukkit.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
			return false;
		}
		final Player player = (Player) sender;
		if (args[0] == player.getName()) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but you cannot duel yourself!");
			return false;
		}
		final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
		if (pm.getGlobalState() != GlobalState.SPAWN || Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getGlobalState() != GlobalState.SPAWN) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
			return false;
		}
		if (Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getRequest() != null && Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getRequest().get(player.getUniqueId()) != null){
			if (Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getRequest().get(player.getUniqueId()).getRequestCooldown() != 0L) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Please, wait for retry this command!");
				return false;
			}
			Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getRequest().get(player.getUniqueId()).removeRequestCooldown();
			Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getRequest().remove(player.getUniqueId());
		}
		pm.setTargetDuel(target);
		player.openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getDuelInventory());
		return false;
	}

}
