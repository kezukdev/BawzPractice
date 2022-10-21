package kezuk.practice.event.host.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class HostItems {
	
	public HostItems(final UUID uuid) {
        Bukkit.getPlayer(uuid).getInventory().clear();
        Bukkit.getPlayer(uuid).getInventory().setItem(8, ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Leave Host."));
        Bukkit.getPlayer(uuid).updateInventory();
	}
}
