package kezuk.bawz.command;

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

import kezuk.bawz.Practice;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.utils.ItemSerializer;
import kezuk.bawz.utils.MessageSerializer;

public class SpectateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (args.length > 1 || args.length < 1) {
            List<ItemStack> matchs = Lists.newArrayList();
            for (MatchManager matchManager : Practice.getMatchs().values()) {
            	for (UUID uuid : matchManager.getFirstList()) {
            		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getLadder().displayName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Type" + ChatColor.RESET + ": " + ChatColor.AQUA + (matchManager.isRanked() ? "Ranked" : "Unranked"), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Arena" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getArena().getName(), " "};
                    ItemStack item = ItemSerializer.serialize(new ItemStack(matchManager.getLadder().material()), matchManager.getLadder().data(), ChatColor.GREEN + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.AQUA + " vs " + ChatColor.RED + Bukkit.getServer().getPlayer(matchManager.getOpponent(uuid)).getName(), Arrays.asList(lore));
                    matchs.add(item);
            	}
            }
            Practice.getInstance().getInventoryManager().getSpectateInventory().refresh(matchs);
            Practice.getInstance().getInventoryManager().getSpectateInventory().open(player, 1);
			return false;
		}
		if (PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus() != Status.SPAWN) {
			sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
			return false;
		}
		final Player target = Bukkit.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(MessageSerializer.PLAYER_NOT_FOUND);
			return false;
		}
		if (Practice.getMatchs().get(PlayerManager.getPlayers().get(target.getUniqueId()).getMatchUUID()) == null) {
			sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Sorry but this player isn't in match.");
			return false;
		}
		Practice.getMatchs().get(PlayerManager.getPlayers().get(target.getUniqueId()).getMatchUUID()).addSpectatorToMatch(player.getUniqueId(), target.getUniqueId());
		return false;
	}

}
