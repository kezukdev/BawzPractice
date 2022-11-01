package kezuk.practice.core.tag.inventory.listener;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.core.tag.Tag;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class TagInventoryListener implements Listener {
	
	@EventHandler
	public void onTagInventory(final InventoryClickEvent event) {
		final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (pm.getSubState().equals(SubState.BUILD)) { 
			event.setCancelled(false);
			return;
		}
		if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory().getName().equals(Practice.getInstance().getRegisterObject().getTagInventory().getTagTypeInventory().getName()) && pm.getGlobalState().equals(GlobalState.SPAWN)) {	
        	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;
        	if (event.getCurrentItem().getType().equals(Material.NAME_TAG)) {
        		event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getTagInventory().getTagClassicInventory());
        	}
        	if (event.getCurrentItem().getType().equals(Material.BOOK)) {
        		event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getTagInventory().getTagJapanInventory());
        	}
        	if (event.getCurrentItem().getType().equals(Material.COMPASS)) {
        		event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getTagInventory().getTagRegionInventory());
        	}
        	if (event.getCurrentItem().getType().equals(Material.BUCKET)) {
        		pm.setTag(Tag.getTagByName("Normal"));
        		event.getWhoClicked().closeInventory();
        		final Player player = (Player) event.getWhoClicked();
        		player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have been reset your tags!");
            	DB.executeUpdateAsync("UPDATE playersdata SET tag=? WHERE name=?", "Normal", player.getName());
        	}	
        	event.setCancelled(true);
		}
        if (event.getClickedInventory().getName().equals(Practice.getInstance().getRegisterObject().getTagInventory().getTagClassicInventory().getName()) || event.getClickedInventory().getName().equals(Practice.getInstance().getRegisterObject().getTagInventory().getTagJapanInventory().getName()) || event.getClickedInventory().getName().equals(Practice.getInstance().getRegisterObject().getTagInventory().getTagRegionInventory().getName())) {
        	if (pm.getGlobalState().equals(GlobalState.SPAWN)) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            	final Tag tag = Tag.getTagByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            	pm.setTag(tag);
            	event.getWhoClicked().closeInventory();
            	final Player player = (Player) event.getWhoClicked();
            	player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You've been set the " + ChatColor.AQUA + tag.getDisplay() + ChatColor.DARK_AQUA + " tag!");
            	DB.executeUpdateAsync("UPDATE playersdata SET tag=? WHERE name=?", ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()), player.getName());	
            	event.setCancelled(true);
        	}
        }
	}

}
