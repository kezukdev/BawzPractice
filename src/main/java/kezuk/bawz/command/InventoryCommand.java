package kezuk.bawz.command;

import java.util.UUID;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import kezuk.bawz.Practice;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.player.*;
import kezuk.bawz.utils.MessageSerializer;

public class InventoryCommand implements CommandExecutor {
	
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player) sender;
        if (Practice.getMatchs().get(PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID()) != null && Practice.getMatchs().get(PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID()).getStatus() != MatchStatus.FINISHED) {
        	player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
        	return false;
        }
        if (args.length > 1 || args.length < 1) {
        	player.sendMessage(MessageSerializer.INVENTORY_HELP);
        	return false;
        }
        try{
            UUID uuid = UUID.fromString(args[0]);
            if (Practice.getInstance().getOfflineInventories().containsKey(uuid)) {
            	player.openInventory(Practice.getInstance().getOfflineInventories().get(uuid).getPreviewInventory());
            	return false;
            }
            else {
                player.sendMessage(MessageSerializer.INVENTORY_NOT_EXIST);
                return false;
            }
        }
        catch (IllegalArgumentException exception){
            player.sendMessage(MessageSerializer.INVENTORY_NOT_EXIST);
        }
        return false;
    }
}
