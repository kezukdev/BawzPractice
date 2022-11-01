package kezuk.practice.core.staff.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.utils.ItemSerializer;

public class ModItems {
	
	public ModItems(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		final ItemStack knockback = new ItemStack(Material.BONE);
		final ItemMeta kbMeta = knockback.getItemMeta();
		kbMeta.setDisplayName(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Knockback VI");
		knockback.setItemMeta(kbMeta);
		knockback.addUnsafeEnchantment(Enchantment.KNOCKBACK, 6);
        final ItemStack vanish = ItemSerializer.serialize(new ItemStack(Material.INK_SACK),(short) (profile.getPlayerCache().getStaffCache().isVanish() ? 10 : 8), ChatColor.GRAY + "» " + ChatColor.AQUA + "Vanish: " + (profile.getPlayerCache().getStaffCache().isVanish() ? ChatColor.WHITE + "On" : ChatColor.RED + "Off"), null, true);
        final ItemStack silent = ItemSerializer.serialize(new ItemStack(Material.GLOWSTONE_DUST),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Silent: " + (profile.getPlayerCache().getStaffCache().isSilent() ? ChatColor.WHITE + "On" : ChatColor.RED + "Off"), null, true);
        final ItemStack randomtp = ItemSerializer.serialize(new ItemStack(Material.SPIDER_EYE),(short) 0, ChatColor.GRAY + "» " + ChatColor.AQUA + "Random TP (In Fight)", null, true);
        final ItemStack leave = ItemSerializer.serialize(new ItemStack(Material.MELON),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Leave Mod Mode", null, true);
        player.getInventory().clear();
        player.getInventory().setItem(0, knockback);
        player.getInventory().setItem(3, vanish);
        player.getInventory().setItem(4, silent);
        player.getInventory().setItem(5, randomtp);
        player.getInventory().setItem(8, leave);
        player.getInventory().setArmorContents(new ItemStack[] {null, null, null ,new ItemStack(Material.GLASS)});
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
