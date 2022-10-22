package kezuk.practice.event.tournament.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.utils.ItemSerializer;

public class TournamentItems {
	
	public TournamentItems(final UUID uuid) {
        Bukkit.getPlayer(uuid).getInventory().clear();
        Bukkit.getPlayer(uuid).getInventory().setItem(8, ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Leave Tournaments."));
        Bukkit.getPlayer(uuid).updateInventory();
	}

}
