package kezuk.bawz.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.bawz.party.PartyManager;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.request.Requesting;
import kezuk.bawz.request.actions.Accepting;
import kezuk.bawz.request.actions.Deny;
import kezuk.bawz.utils.MessageSerializer;
import net.md_5.bungee.api.ChatColor;

public class PartyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (args.length == 0) {
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party create");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party leave");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party invite <player>");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party accept <player>");
			player.sendMessage(" ");
			return false;
		}
		if (args[0].equalsIgnoreCase("create") && args.length == 1) {
			if (pm.getPlayerStatus() != Status.SPAWN) {
				player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
				return false;
			}
			new PartyManager(player.getUniqueId());
			return false;
		}
		else if (args[0].equalsIgnoreCase("leave") && args.length == 1) {
			if (pm.getPlayerStatus() != Status.PARTY) {
				player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
				return false;
			}
			PartyManager.getPartyMap().get(player.getUniqueId()).removeToParty(player.getUniqueId(), false);
		}
		else if (args[0].equalsIgnoreCase("invite") && args.length == 2) {
			if (pm.getPlayerStatus() != Status.PARTY) {
				player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == player) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You cannot invite yourself!");
				return false;
			}
			if (target == null) {
				player.sendMessage(MessageSerializer.PLAYER_NOT_FOUND);
				return false;
			}
			if (PlayerManager.getPlayers().get(target.getUniqueId()).getPlayerStatus() != Status.SPAWN) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but this player state is not available for you.");
				return false;
			}
			new Requesting(player.getUniqueId(), target.getUniqueId(), "Party");
		}
		else if (args[0].equalsIgnoreCase("accept") && args.length == 2) {
			if (pm.getPlayerStatus() != Status.SPAWN) {
				player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				player.sendMessage(MessageSerializer.PLAYER_NOT_FOUND);
				return false;
			}
			final PlayerManager pmTarget = PlayerManager.getPlayers().get(target.getUniqueId());
			if (pmTarget.getPlayerStatus() != Status.PARTY) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The target is not in party!");
				return false;
			}
			new Accepting(player.getUniqueId(), target.getUniqueId());
		}
		else if (args[0].equalsIgnoreCase("deny") && args.length == 2) {
			if (pm.getPlayerStatus() != Status.SPAWN) {
				player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				player.sendMessage(MessageSerializer.PLAYER_NOT_FOUND);
				return false;
			}
			final PlayerManager pmTarget = PlayerManager.getPlayers().get(target.getUniqueId());
			if (pmTarget.getPlayerStatus() != Status.PARTY) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The target is not in party!");
				return false;
			}
			new Deny(player.getUniqueId(), target.getUniqueId());
		}
		else {
			player.sendMessage(" ");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party create");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party leave");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party invite <player>");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party accept <player>");
			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/party deny <player>");
			player.sendMessage(" ");
		}
		return false;
	}

}
