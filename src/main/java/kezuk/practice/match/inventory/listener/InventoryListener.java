package kezuk.practice.match.inventory.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.SubState;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryClickEvent(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getSubState().equals(SubState.FINISHED) || profile.getSubState().equals(SubState.NOTHING)) {
	        if (event.getClickedInventory().getTitle().contains(ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Preview of")) {
	        	event.setCancelled(true);
	        	if (event.getCurrentItem().getType().equals(Material.PISTON_STICKY_BASE)) {
	        		final Player player = (Player) event.getWhoClicked();
	        		player.closeInventory();
	        		String name = event.getCurrentItem().getItemMeta().getLore().get(1);
	        		String uuidWithoutColor = ChatColor.stripColor(name);
	        		player.chat("/inventory " + uuidWithoutColor);
	        		return;
	        	}
	        	else {
	        		return;
	        	}
	        }
		}
	}

}
