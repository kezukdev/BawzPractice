package kezuk.practice.party.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.party.Party;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.ItemSerializer;

public class PartyItems {
	
	public PartyItems(final Player player) {
        player.getInventory().clear();
		if (Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState().getSubState().equals(SubState.NOTHING)) {
	        final ItemStack match = ItemSerializer.serialize(new ItemStack(Material.GOLD_AXE),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Match", null, true);
	        final ItemStack management = ItemSerializer.serialize(new ItemStack(Material.PAPER),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Party Management", null, true);
	        final ItemStack info = ItemSerializer.serialize(new ItemStack(Material.PAPER),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Party Info", null, true);
	        final ItemStack leave = ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Leave Party", null, true);
	        if (Practice.getInstance().getRegisterCollections().getPartys().get(player.getUniqueId()) != null) {
	            player.getInventory().setItem(0, match);
	            player.getInventory().setItem(4, management);
	        }
	        if (Practice.getInstance().getRegisterCollections().getPartys().get(player.getUniqueId()) == null) {
	        	player.getInventory().setItem(0, info);
	        }
	        player.getInventory().setItem(8, leave);	
		}
		if (Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState().getSubState().equals(SubState.QUEUE)) {
	        final ItemStack leave = ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Leave Party Queue", null, true);
	        player.getInventory().setItem(8, leave);
		}
        player.updateInventory();
	}

}
