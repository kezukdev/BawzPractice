package kezuk.practice.player.personnal.subinventory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.player.cache.PlayerCache;
import kezuk.practice.utils.ItemSerializer;

public class SettingsInventory {
	
	private Inventory settings;
	
	public SettingsInventory() {
		this.settings = Bukkit.createInventory(null, InventoryType.DISPENSER, ChatColor.DARK_GRAY + "Settings:");
	}
	
	public void setPreviewSettings(final UUID uuid) {
		this.settings.clear();
		final PlayerCache cache = Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getPlayerCache();
		final List<String> enable = Arrays.asList(ChatColor.GRAY + " » " + ChatColor.WHITE + "Enabled", ChatColor.WHITE + " Disabled");
		final List<String> disable = Arrays.asList(ChatColor.WHITE + " Enabled", ChatColor.GRAY + " » " + ChatColor.WHITE + "Disabled");
		final ItemStack scoreboard = ItemSerializer.serialize(new ItemStack(Material.PAINTING), (short)0 , ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Scoreboard", cache.isScoreboard() ? enable : disable);
		final ItemStack pm = ItemSerializer.serialize(new ItemStack(Material.PAPER), (short)0 , ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Private-Message", cache.isPm() ? enable : disable);
		final ItemStack duel = ItemSerializer.serialize(new ItemStack(Material.IRON_SWORD), (short)0 , ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Duel-Request", cache.isDuel() ? enable : disable);
		final ItemStack deatheffect = ItemSerializer.serialize(new ItemStack(Material.FIREWORK), (short)0 , ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Kill-Effect", Arrays.asList(ChatColor.WHITE + "Choose from a list of effects you want when you kill your opponent!"));
		this.settings.setItem(0, scoreboard);
		this.settings.setItem(3, pm);
		this.settings.setItem(5, deatheffect);
		this.settings.setItem(6, duel);
	}
	public Inventory getSettings() {
		return settings;
	}
}
