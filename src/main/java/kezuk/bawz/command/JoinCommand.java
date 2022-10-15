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
import kezuk.bawz.utils.ItemSerializer;

public class JoinCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (args.length > 1 || args.length < 1) {
			final UUID hostUUID = UUID.fromString(args[0]);
			if (hostUUID == null) {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Please provide a correct host uuid!");
				return false;
			}
			if (HostManager.getHosts().containsKey(hostUUID)) {
				HostManager.getHosts().get(hostUUID).addMember(player.getUniqueId(), hostUUID);
			}
			else {
				sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "No event existed with this uuid!");
			}
		}
		if (args.length == 0) {
            List<ItemStack> event = Lists.newArrayList();
            for (HostManager hostManager : HostManager.getHosts().values()) {
        		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Creator" + ChatColor.RESET + ": " + Bukkit.getPlayer(hostManager.getCreator()).getName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Players" + ChatColor.RESET + ": " + ChatColor.AQUA + hostManager.getMembers().size() + ChatColor.GRAY + "/" + ChatColor.RED + hostManager.getSize(),ChatColor.DARK_AQUA + "UUID" + ChatColor.RESET + ": " + ChatColor.GRAY + hostManager.getHostUUID() ," "};
                ItemStack item = ItemSerializer.serialize(new ItemStack(Material.WATER_BUCKET), (short)0, ChatColor.DARK_AQUA + hostManager.getType().toString(), Arrays.asList(lore));
                event.add(item);
            }
            Practice.getInstance().getInventoryManager().getEventRunningInventory().refresh(event);
            Practice.getInstance().getInventoryManager().getEventRunningInventory().open(player, 1);
		}
		return false;
	}

}
