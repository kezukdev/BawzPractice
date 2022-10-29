package kezuk.practice.core.staff.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.core.staff.items.ModItems;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;

public class StaffListeners implements Listener {
	
	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.MOD)) {
			if (event.getItem().getType().equals(Material.INK_SACK) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				profile.getPlayerCache().getStaffCache().setVanish(profile.getPlayerCache().getStaffCache().isVanish() ? false : true);
				new ModItems(event.getPlayer().getUniqueId());
				event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been " + (profile.getPlayerCache().getStaffCache().isVanish() ? ChatColor.WHITE + "enabled" : ChatColor.RED + "disabled") + ChatColor.AQUA + " the vanish mode!");
			}
			if (event.getItem().getType().equals(Material.GLOWSTONE_DUST) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				profile.getPlayerCache().getStaffCache().setSilent(profile.getPlayerCache().getStaffCache().isSilent() ? false : true);
				new ModItems(event.getPlayer().getUniqueId());
				event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been " + (profile.getPlayerCache().getStaffCache().isSilent() ? ChatColor.WHITE + "enabled" : ChatColor.RED + "disabled") + ChatColor.AQUA + " the silent mode!");
			}
			if (event.getItem().getType().equals(Material.MELON) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				new SpawnItems(event.getPlayer().getUniqueId(), false);
				profile.setGlobalState(GlobalState.SPAWN);
				profile.getPlayerCache().getStaffCache().setVanish(false);
				profile.getPlayerCache().getStaffCache().setSilent(false);
				event.getPlayer().teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
				event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been left the mod mode!");
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.MOD)) {
			event.setCancelled(true);
		}
	}

}
