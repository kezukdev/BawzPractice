package kezuk.practice.player.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.player.utils.listener.UtilsListener;
import kezuk.practice.utils.ItemSerializer;

public class UtilsInventory {
	
	private Inventory leaderboardInventory;
	private Inventory utilsInventory;
	
	public UtilsInventory() {
		this.leaderboardInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Leaderboard:");
		this.utilsInventory = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.DARK_GRAY + "Utils:");
		this.setUtilsInventory();
		Bukkit.getPluginManager().registerEvents(new UtilsListener(), Practice.getInstance());
	}
	
	private void setUtilsInventory() {
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 27; ++i) {
            this.utilsInventory.setItem(i, glass);
        }
        final ItemStack party = ItemSerializer.serialize(new ItemStack(Material.CAKE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Party", Arrays.asList(new String(ChatColor.WHITE + " Create your party!")));
        final ItemStack tourney = ItemSerializer.serialize(new ItemStack(Material.FERMENTED_SPIDER_EYE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Tournaments (BETA)", Arrays.asList(new String(ChatColor.WHITE + " Create your tournaments!")));
        final ItemStack host = ItemSerializer.serialize(new ItemStack(Material.PAPER),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Host (BETA)", Arrays.asList(new String(ChatColor.WHITE + " Create your host")));
        final ItemStack leaderboard = ItemSerializer.serialize(new ItemStack(Material.DIAMOND),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Leaderboard", Arrays.asList(new String(ChatColor.WHITE + " View the leaderboard")));
        final ItemStack spectate = ItemSerializer.serialize(new ItemStack(Material.ENDER_PORTAL),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Spectate", Arrays.asList(new String(ChatColor.WHITE + " See the current fights")));
        final ItemStack editor = ItemSerializer.serialize(new ItemStack(Material.ENCHANTMENT_TABLE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Editor", Arrays.asList(new String(ChatColor.WHITE + " Change your game mode to your liking!")));
        this.utilsInventory.setItem(4, party);
        this.utilsInventory.setItem(10, tourney);
        this.utilsInventory.setItem(12, host);
        this.utilsInventory.setItem(22, leaderboard);
        this.utilsInventory.setItem(14, spectate);
        this.utilsInventory.setItem(16, editor);
	}

	public Inventory getUtilsInventory() {
		return utilsInventory;
	}
	
	public Inventory getLeaderboardInventory() {
		return leaderboardInventory;
	}
}
