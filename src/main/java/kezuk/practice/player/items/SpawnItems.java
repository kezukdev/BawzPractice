package kezuk.practice.player.items;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.ItemSerializer;

public class SpawnItems {
	
	public SpawnItems(final UUID uuid, boolean afterMatch) {
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setGlobalState(GlobalState.SPAWN);
		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setSubState(SubState.NOTHING);
		final Player player = Bukkit.getPlayer(uuid);
        final ItemStack queue = ItemSerializer.serialize(new ItemStack(Material.ANVIL),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Queue Selector", null, true);
        final ItemStack utils = ItemSerializer.serialize(new ItemStack(Material.BOOK),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Utils", null, true);
        final ItemStack personnal = ItemSerializer.serialize(new ItemStack(Material.WATCH),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Personnal Management", null, true);
        final ItemStack rematch = ItemSerializer.serialize(new ItemStack(Material.WATCH),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Re-queue", Arrays.asList("", ChatColor.DARK_GRAY + "(Shift-Click) To playing again in your most played mode"),true);
        player.getInventory().clear();
        player.getInventory().setItem(2, personnal);
        player.getInventory().setItem(4, queue);
        player.getInventory().setItem(6, utils);
        if (afterMatch) {
        	player.getInventory().setItem(0, rematch);
        	new BukkitRunnable() {
				@Override
				public void run() {
					if (player.getInventory().getItem(0).getType().equals(Material.PAPER)) {
						player.getInventory().setItem(0, new ItemStack(Material.AIR));	
					}
				}
			}.runTaskLaterAsynchronously(Practice.getInstance(), 1200L*2L);
        }
        player.getInventory().setArmorContents((ItemStack[])null);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setLevel(0);
		player.setExp(0.0f);
		player.setSaturation(20.0f);
		player.setHealth(player.getMaxHealth());
		player.extinguish();
        player.updateInventory();
	}

}
