package kezuk.practice.player.utils.subinventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class HostInventory {
	
	private Inventory host;
	
	public HostInventory() {
		this.host = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_GRAY + "Host:");
		this.setHost();
	}

	private void setHost() {
    	this.host.clear();
        final ItemStack ffa = ItemSerializer.serialize(new ItemStack(Material.FLINT), (short)0, ChatColor.AQUA + "FFA Event");
        final ItemStack sumo = ItemSerializer.serialize(new ItemStack(Material.ANVIL), (short)0, ChatColor.DARK_AQUA + "SUMO Event");
        final ItemStack oitc = ItemSerializer.serialize(new ItemStack(Material.GOLD_SWORD), (short)0, ChatColor.AQUA + "OITC Event");
        this.host.setItem(0, ffa);
        this.host.setItem(2, sumo);
        this.host.setItem(4, oitc);
	}
	
	public Inventory getHost() {
		return host;
	}

}
