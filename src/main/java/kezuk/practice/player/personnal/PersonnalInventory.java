package kezuk.practice.player.personnal;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class PersonnalInventory implements Listener {
	
	private Inventory personnalInventory;
	
	public PersonnalInventory() {
		this.personnalInventory = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.DARK_GRAY + "Personnal Management:");
		this.setPersonnalInventory();
	}
	
	private void setPersonnalInventory() {
		this.personnalInventory.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 27; ++i) {
            this.personnalInventory.setItem(i, glass);
        }
        final ItemStack stats = ItemSerializer.serialize(new ItemStack(Material.CAULDRON_ITEM),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Statistics", Arrays.asList(new String(ChatColor.WHITE + " View your statistics")));
        final ItemStack tag = ItemSerializer.serialize(new ItemStack(Material.NAME_TAG),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Tag", Arrays.asList(new String(ChatColor.WHITE + " Change your tag")));
        final ItemStack settings = ItemSerializer.serialize(new ItemStack(Material.DAYLIGHT_DETECTOR),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Settings", Arrays.asList(new String(ChatColor.WHITE + " Select your settings")));
        this.personnalInventory.setItem(12, stats);
        this.personnalInventory.setItem(13, tag);
        this.personnalInventory.setItem(14, settings);
    }
	
	public Inventory getPersonnalInventory() {
		return personnalInventory;
	}

}
