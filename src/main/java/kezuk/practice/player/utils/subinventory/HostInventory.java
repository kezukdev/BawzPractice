package kezuk.practice.player.utils.subinventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class HostInventory {
	
	private Inventory host;
	
	public HostInventory() {
		this.host = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Host:");
		this.setHost();
	}

	private void setHost() {
    	this.host.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 9; ++i) {
            this.host.setItem(i, glass);
        }
        final ItemStack ffa = ItemSerializer.serialize(new ItemStack(Material.FLINT), (short)0, ChatColor.AQUA + "FFA Event");
        final ItemStack sumo = ItemSerializer.serialize(new ItemStack(Material.ANVIL), (short)0, ChatColor.DARK_AQUA + "SUMO Event");
        final ItemStack oitc = ItemSerializer.serialize(new ItemStack(Material.GOLD_SWORD), (short)0, ChatColor.AQUA + "OITC Event");
        this.host.setItem(2, ffa);
        this.host.setItem(4, sumo);
        this.host.setItem(6, oitc);
	}
	
	public Inventory getHost() {
		return host;
	}

}
