package kezuk.practice.player.utils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.utils.leaderboard.Top;
import kezuk.practice.utils.ItemSerializer;

public class UtilsInventory {
	
	private Inventory leaderboardInventory;
	private Inventory utilsInventory;
	
	public UtilsInventory() {
		this.leaderboardInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Leaderboard:");
		this.utilsInventory = Bukkit.createInventory((InventoryHolder)null, InventoryType.DISPENSER, ChatColor.DARK_GRAY + "Utils:");
		this.setUtilsInventory();
		this.setLeaderboardInventory();
        new BukkitRunnable() {
			
			@Override
			public void run() {
	            Practice.getInstance().getRegisterObject().getLeaderboard().refresh();
	            Top[] top = Practice.getInstance().getRegisterObject().getLeaderboard().getTop();
	            Top global_top = Practice.getInstance().getRegisterObject().getLeaderboard().getGlobal();
	            for (Ladders ladder : Practice.getInstance().getLadder()) {
	            	if (ladder.isRanked()) {
	                    ItemStack current = leaderboardInventory.getItem(ladder.id() + 9);
	                    ItemMeta meta = current.getItemMeta();

	                    meta.setLore(top[ladder.id()].getLore());
	                    current.setItemMeta(meta);	
	            	}
	            }

	            ItemStack current = leaderboardInventory.getItem(4);
	            ItemMeta meta = current.getItemMeta();

	            meta.setLore(global_top.getLore());
	            current.setItemMeta(meta);
				
			}
		}.runTaskLaterAsynchronously(Practice.getInstance(), 2L);
	}
	
	private void setLeaderboardInventory() {
		this.leaderboardInventory.clear();
        for(Ladders ladder : Practice.getInstance().getLadder()) {
        	if (ladder.isRanked()) {
                ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
                this.leaderboardInventory.setItem(ladder.id()+9 ,item);	
        	}
        }
        ItemStack item = ItemSerializer.serialize(new ItemStack(Material.BAKED_POTATO), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Top " + ChatColor.AQUA + "Global" + ChatColor.GRAY + " * ");
        this.leaderboardInventory.setItem(4 ,item);
	}
	
	public void refreshLeaderboard() {
    	CompletableFuture<Void> refresh = CompletableFuture.runAsync(() -> {
            Practice.getInstance().getRegisterObject().getLeaderboard().refresh();
    	});
    	refresh.whenCompleteAsync((t, u) -> {
            Top[] top = Practice.getInstance().getRegisterObject().getLeaderboard().getTop();
            Top global_top = Practice.getInstance().getRegisterObject().getLeaderboard().getGlobal();
            for (Ladders ladder : Practice.getInstance().getLadder()) {
            	if (ladder.isRanked()) {
                    ItemStack current = this.leaderboardInventory.getItem(ladder.id() + 9);
                    ItemMeta meta = current.getItemMeta();

                    meta.setLore(top[ladder.id()].getLore());
                    current.setItemMeta(meta);	
            	}
            }

            ItemStack current = this.leaderboardInventory.getItem(4);
            ItemMeta meta = current.getItemMeta();

            meta.setLore(global_top.getLore());
            current.setItemMeta(meta);

    	});
	}

	private void setUtilsInventory() {
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        final ItemStack party = ItemSerializer.serialize(new ItemStack(Material.CAKE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Party", Arrays.asList(new String(ChatColor.WHITE + " Create your party!")));
        final ItemStack tourney = ItemSerializer.serialize(new ItemStack(Material.FERMENTED_SPIDER_EYE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Tournaments (BETA)", Arrays.asList(new String(ChatColor.WHITE + " Create your tournaments!")));
        final ItemStack host = ItemSerializer.serialize(new ItemStack(Material.PAPER),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Host (BETA)", Arrays.asList(new String(ChatColor.WHITE + " Create your host")));
        final ItemStack leaderboard = ItemSerializer.serialize(new ItemStack(Material.DIAMOND),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Leaderboard", Arrays.asList(new String(ChatColor.WHITE + " View the leaderboard")));
        final ItemStack spectate = ItemSerializer.serialize(new ItemStack(Material.ENDER_PORTAL),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Spectate", Arrays.asList(new String(ChatColor.WHITE + " See the current fights")));
        this.utilsInventory.setItem(0, party);
        this.utilsInventory.setItem(1, glass);
        this.utilsInventory.setItem(2, tourney);
        this.utilsInventory.setItem(3, glass);
        this.utilsInventory.setItem(4, leaderboard);
        this.utilsInventory.setItem(5, glass);
        this.utilsInventory.setItem(6, host);
        this.utilsInventory.setItem(7, glass);
        this.utilsInventory.setItem(8, spectate);
	}

	public Inventory getUtilsInventory() {
		return utilsInventory;
	}
	
	public Inventory getLeaderboardInventory() {
		return leaderboardInventory;
	}
}
