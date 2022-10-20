package kezuk.practice.party.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class MatchInventory {
	
	private Inventory matchInventory;
	
	public MatchInventory() {
		this.matchInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Party Match:");
		this.setPartyMatchInventory();
	}
	
	private void setPartyMatchInventory() {
		this.matchInventory.clear();
		final ItemStack ffa = ItemSerializer.serialize(new ItemStack(Material.DIAMOND_SWORD), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "FFA");
		final ItemStack queue = ItemSerializer.serialize(new ItemStack(Material.EMERALD), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "2v2");
		final ItemStack split = ItemSerializer.serialize(new ItemStack(Material.DIAMOND_CHESTPLATE), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "SPLIT");
		final ItemStack duel = ItemSerializer.serialize(new ItemStack(Material.CHEST), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Duel Other Parties");
		this.matchInventory.setItem(3, ffa);
		this.matchInventory.setItem(4, queue);
		this.matchInventory.setItem(5, split);
		this.matchInventory.setItem(8, duel);
	}
	
	public Inventory getMatchInventory() {
		return matchInventory;
	}

}
