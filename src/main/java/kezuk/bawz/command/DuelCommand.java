package kezuk.bawz.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.bawz.Practice;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
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
		if (args[0] == player.getName()) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but you cannot duel yourself!");
			return false;
		}
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.getPlayerStatus() != Status.SPAWN || PlayerManager.getPlayers().get(target.getUniqueId()).getPlayerStatus() != Status.SPAWN) {
			sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
			return false;
		}
		if (PlayerManager.getPlayers().get(target.getUniqueId()).getRequest() != null && PlayerManager.getPlayers().get(target.getUniqueId()).getRequest().get(player.getUniqueId()) != null){
			if (PlayerManager.getPlayers().get(player.getUniqueId()).haveDuelCooldownActive()) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Please, wait for retry this command!");
				return false;
			}
			PlayerManager.getPlayers().get(player.getUniqueId()).removeDuelCooldown();
			PlayerManager.getPlayers().get(target.getUniqueId()).getRequest().remove(player.getUniqueId());
		}
		pm.setTargetDuel(target);
		player.openInventory(Practice.getInstance().getInventoryManager().getDuelInventory());
		return false;
	}

}
