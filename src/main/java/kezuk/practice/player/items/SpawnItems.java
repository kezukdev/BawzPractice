package kezuk.practice.player.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.ItemSerializer;

public class SpawnItems {
	
	public SpawnItems(final UUID uuid) {
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setGlobalState(GlobalState.SPAWN);
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getGlobalState().setSubState(SubState.NOTHING);
		final Player player = Bukkit.getPlayer(uuid);
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

}
