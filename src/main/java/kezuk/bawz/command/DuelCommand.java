package kezuk.bawz.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.bawz.Practice;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.request.RequestManager;
import kezuk.bawz.utils.MessageSerializer;
import net.md_5.bungee.api.ChatColor;

public class DuelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (args.length < 1 | args.length > 1) {
			sender.sendMessage(MessageSerializer.DUEL_USAGE);
			return false;
		}
		final Player target = Bukkit.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(MessageSerializer.PLAYER_NOT_FOUND);
			return false;
		}
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.getPlayerStatus() != Status.SPAWN) {
			sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
			return false;
		}
		if (args[0] == player.getName()) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but you cannot duel yourself!");
			return false;
		}
		if (RequestManager.getRequest().get(player.getUniqueId()) != null) {
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Please wait your another request has expired.");
			return false;
		}
		pm.setTargetDuel(target);
		player.openInventory(Practice.getInstance().getInventoryManager().getDuelInventory());
		return false;
	}

}
