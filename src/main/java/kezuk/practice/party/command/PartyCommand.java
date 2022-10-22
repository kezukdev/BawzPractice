package kezuk.practice.party.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.practice.Practice;
import kezuk.practice.party.Party;
import kezuk.practice.party.state.PartyState;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.request.Requesting;
import kezuk.practice.request.actions.Accepting;
import kezuk.practice.request.actions.Deny;
import net.md_5.bungee.api.ChatColor;

public class PartyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
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
			if (pm.getGlobalState() != GlobalState.SPAWN) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			new Party(player.getUniqueId());
			return false;
		}
		else if (args[0].equalsIgnoreCase("leave") && args.length == 1) {
			if (pm.getGlobalState() != GlobalState.PARTY) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			final Party party = Party.getPartyMap().get(player.getUniqueId());
			if (party.getStatus() != PartyState.SPAWN) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			party.removeToParty(player.getUniqueId(), false);
		}
		else if (args[0].equalsIgnoreCase("invite") && args.length == 2) {
			if (pm.getGlobalState() != GlobalState.PARTY) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == player) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You cannot invite yourself!");
				return false;
			}
			if (target == null) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
				return false;
			}
			if (Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getGlobalState() != GlobalState.SPAWN) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but this player state is not available for you.");
				return false;
			}
			new Requesting(player.getUniqueId(), target.getUniqueId(), "Party");
		}
		else if (args[0].equalsIgnoreCase("accept") && args.length == 2) {
			if (pm.getGlobalState() != GlobalState.SPAWN) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
				return false;
			}
			final Profile pmTarget = Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId());
			if (pmTarget.getGlobalState() != GlobalState.PARTY) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The target is not in party!");
				return false;
			}
			if (pmTarget.getSubState().equals(SubState.QUEUE)) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You cannot join this party at this time");
				return false;
			}
			new Accepting(player.getUniqueId(), target.getUniqueId());
		}
		else if (args[0].equalsIgnoreCase("deny") && args.length == 2) {
			if (pm.getGlobalState() != GlobalState.SPAWN) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
				return false;
			}
			final Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
				return false;
			}
			final Profile pmTarget = Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId());
			if (pmTarget.getGlobalState() != GlobalState.PARTY) {
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
