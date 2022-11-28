package kezuk.practice.player.personnal.subinventory;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.player.cache.StatisticsCache;
import kezuk.practice.utils.ItemSerializer;

public class StatisticsInventory {

	private Inventory stats;
	private UUID uuid;
	
	public StatisticsInventory(final UUID uuid) {
		this.uuid = uuid;
		this.stats = Bukkit.createInventory(null, InventoryType.MERCHANT, ChatColor.DARK_GRAY + Bukkit.getOfflinePlayer(uuid).getName() + "'s statistics: ");
	}
	
	public void setStatsInventory() {
		final StatisticsCache h = Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getHistoric();
		final ItemStack matchPlayed = ItemSerializer.serialize(new ItemStack(Material.EXP_BOTTLE), (short)0, ChatColor.DARK_GRAY + " » " + ChatColor.DARK_AQUA + "Match Statistics", Arrays.asList("", ChatColor.GRAY + " * " + ChatColor.AQUA + "Ranked Played" + ChatColor.WHITE + ": " + h.getRankedPlayed(), ChatColor.GRAY + " * " + ChatColor.AQUA + "Casual Played" + ChatColor.WHITE + ": " + h.getUnrankedPlayed()));
		this.stats.setItem(0, matchPlayed);
	}
	
}
