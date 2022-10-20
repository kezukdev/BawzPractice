package kezuk.practice.match.inventory.command;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import kezuk.practice.Practice;
import kezuk.practice.player.state.SubState;


public class InventoryCommand implements CommandExecutor {
	
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player) sender;
        if (Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getMatchUUID()) != null && Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState().getSubState() != SubState.FINISHED) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
        	return false;
        }
        if (args.length > 1 || args.length < 1) {
        	player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "/inventory <UUID>");
        	return false;
        }
        try{
            UUID uuid = UUID.fromString(args[0]);
            if (Practice.getInstance().getRegisterCollections().getOfflineInventories().containsKey(uuid)) {
            	player.openInventory(Practice.getInstance().getRegisterCollections().getOfflineInventories().get(uuid).getPreviewInventory());
            	return false;
            }
            else {
            	player.sendMessage(ChatColor.AQUA + "The inventory you request is not saved on the server.");
                return false;
            }
        }
        catch (IllegalArgumentException exception){
            player.sendMessage(ChatColor.AQUA + "The inventory you request is not saved on the server.");
        }
        return false;
    }
}
