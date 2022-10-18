package kezuk.bawz.manager;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import kezuk.bawz.party.PartyManager;
import kezuk.bawz.utils.*;

import org.bukkit.*;

public class ItemsManager
{
    public void giveSpawnItems(final Player player) {
        final ItemStack unranked = ItemSerializer.serialize(new ItemStack(Material.STONE_SWORD),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Unranked Queue", null, true);
        final ItemStack ranked = ItemSerializer.serialize(new ItemStack(Material.DIAMOND_SWORD),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Ranked Queue", null, true);
        final ItemStack utils = ItemSerializer.serialize(new ItemStack(Material.BOOK),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Utils", null, true);
        final ItemStack personnal = ItemSerializer.serialize(new ItemStack(Material.NETHER_STAR),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Personnal Management", null, true);
        player.getInventory().clear();
        player.getInventory().setItem(0, ranked);
        player.getInventory().setItem(1, unranked);
        player.getInventory().setItem(8, utils);
        player.getInventory().setItem(4, personnal);
        player.getInventory().setArmorContents((ItemStack[])null);
        player.updateInventory();
    }
    
    public void giveLeaveItems(final Player player, final String type) {
        final ItemStack leave = ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Leave " + type, null, true);
        player.getInventory().clear();
        player.getInventory().setItem(8, leave);
        player.updateInventory();
    }
    
    public void givePartyItems(final Player player) {
        final ItemStack match = ItemSerializer.serialize(new ItemStack(Material.GOLD_AXE),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Match", null, true);
        final ItemStack management = ItemSerializer.serialize(new ItemStack(Material.PAPER),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Party Management", null, true);
        final ItemStack leave = ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Leave Party", null, true);
        player.getInventory().clear();
        if (PartyManager.getPartyMap().get(player.getUniqueId()).getLeader().equals(player.getUniqueId())) {
            player.getInventory().setItem(0, match);	
        }
        player.getInventory().setItem(4, management);
        player.getInventory().setItem(8, leave);
        player.updateInventory();
    }
}
