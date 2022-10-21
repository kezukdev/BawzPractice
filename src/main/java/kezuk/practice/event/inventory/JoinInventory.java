package kezuk.practice.event.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class JoinInventory {
	
	private Inventory joinInventory;
	
	public JoinInventory() {
		this.joinInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "What would you like to join?");
		this.setJoinInventory();
	}
	
	private void setJoinInventory() {
		this.joinInventory.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 9; ++i) {
            this.joinInventory.setItem(i, glass);
        }
		final ItemStack host = ItemSerializer.serialize(new ItemStack(Material.WATER_BUCKET), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Host Event");
		final ItemStack tourney = ItemSerializer.serialize(new ItemStack(Material.LAVA_BUCKET), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Tournament Event");
		this.joinInventory.setItem(3, host);
		this.joinInventory.setItem(5, tourney);
	}

	public Inventory getJoinInventory() {
		return joinInventory;
	}

}
