package kezuk.practice.party.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class ManageInventory {
	
	private Inventory manageInventory;
	
	public ManageInventory() {
		this.manageInventory = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Manage your party settings:");
	}
	
	public Inventory getManageInventory() {
		return manageInventory;
	}

}
