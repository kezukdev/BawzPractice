package kezuk.bawz.command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.host.HostManager;
import kezuk.bawz.host.HostStatus;
import kezuk.bawz.utils.ItemSerializer;
import kezuk.bawz.utils.MessageSerializer;

public class JoinCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (args.length == 0) {
            List<ItemStack> event = Lists.newArrayList();
            for (HostManager hostManager : Practice.getHosts().values()) {
        		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Creator" + ChatColor.RESET + ": " + Bukkit.getPlayer(hostManager.getCreator()).getName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Players" + ChatColor.RESET + ": " + ChatColor.AQUA + hostManager.getMembers().size() + ChatColor.GRAY + "/" + ChatColor.RED + hostManager.getSize(),ChatColor.GRAY.toString() + hostManager.getHostUUID() ," "};
                ItemStack item = ItemSerializer.serialize(new ItemStack(Material.WATER_BUCKET), (short)0, ChatColor.DARK_AQUA + hostManager.getType().toString(), Arrays.asList(lore));
                event.add(item);
            }
            Practice.getInstance().getInventoryManager().getEventRunningInventory().refresh(event);
            Practice.getInstance().getInventoryManager().getEventRunningInventory().open(player, 1);
		}
		if (args.length == 1) {
			final UUID hostUUID = UUID.fromString(args[0]);
			if (hostUUID == null) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Please provide a correct host uuid!");
				return false;
			}
			if (Practice.getHosts().get(hostUUID) == null) {
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "No event with this uuid exist.");
				return false;
			}
			if (Practice.getHosts().get(hostUUID).getStatus() != HostStatus.STARTING) {
				sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
				return false;
			}
			Practice.getHosts().get(hostUUID).addMember(player.getUniqueId(), hostUUID);
		}
		return false;
	}

}
