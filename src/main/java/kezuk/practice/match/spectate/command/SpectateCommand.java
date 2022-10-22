package kezuk.practice.match.spectate.command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.utils.GameUtils;
import kezuk.practice.utils.ItemSerializer;

public class SpectateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (args.length > 1 || args.length < 1) {
			List<ItemStack> matchs = Lists.newArrayList();
	        for (StartMatch matchManager : Practice.getInstance().getRegisterCollections().getMatchs().values()) {
	        	for (UUID uuid : matchManager.getFirstList()) {
	        		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getLadder().displayName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Type" + ChatColor.RESET + ": " + ChatColor.AQUA + (matchManager.isRanked() ? "Ranked" : "Unranked"), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Arena" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getArena().getName(), " "};
	                ItemStack item = ItemSerializer.serialize(new ItemStack(matchManager.getLadder().material()), matchManager.getLadder().data(), ChatColor.GREEN + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.AQUA + " vs " + ChatColor.RED + Bukkit.getServer().getPlayer(GameUtils.getOpponent(uuid)).getName(), Arrays.asList(lore));
	                matchs.add(item);
	        	}
	        }
	        Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().refresh(matchs);
			Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().open(Bukkit.getPlayer(sender.getName()), 1);
			return false;
		}
		if (Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState() != GlobalState.SPAWN) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
			return false;
		}
		if (args.length == 1) {
			final Player target = Bukkit.getServer().getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "The indicated player was not found on the server!");
				return false;
			}
			if (Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId()).getMatchUUID()) == null) {
				sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "Sorry but this player does not appear to be in combat at the moment!");
				return false;
			}
			GameUtils.addSpectatorToMatch(Bukkit.getPlayer(sender.getName()).getUniqueId(), target.getUniqueId());
		}
		return false;
	}

}
