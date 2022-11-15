package kezuk.practice.player.personnal;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.utils.ItemSerializer;

public class PersonnalInventory implements Listener {
	
	private Inventory personnalInventory;
	
	public PersonnalInventory(final Player player) {
		this.personnalInventory = Bukkit.createInventory((InventoryHolder)null, InventoryType.BREWING, ChatColor.DARK_GRAY + "Personnal Management:");
		this.setPersonnalInventory(player);
	}
	
	public void setPersonnalInventory(final Player player) {
		this.personnalInventory.clear();
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        final ItemStack skull = ItemSerializer.serialize(new ItemStack(Material.SIGN),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + player.getName(), Arrays.asList(ChatColor.GRAY + " * " + ChatColor.WHITE + "Rank: " + profile.getRank().getColor() + profile.getRank().getDisplayName(), ChatColor.GRAY + " * " + ChatColor.WHITE + "Tag: " + profile.getTag().getColor() + profile.getTag().getDisplay()));
        final ItemStack stats = ItemSerializer.serialize(new ItemStack(Material.CAULDRON_ITEM),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Statistics");
        final ItemStack tag = ItemSerializer.serialize(new ItemStack(Material.NAME_TAG),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Tag", Arrays.asList(new String(ChatColor.WHITE + " Change your tag")));
        final ItemStack settings = ItemSerializer.serialize(new ItemStack(Material.DAYLIGHT_DETECTOR),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Settings");
        this.personnalInventory.setItem(3, skull);
        this.personnalInventory.setItem(0, stats);
        this.personnalInventory.setItem(1, tag);
        this.personnalInventory.setItem(2, settings);
    }
	
	public Inventory getPersonnalInventory() {
		return personnalInventory;
	}

}
