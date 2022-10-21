package kezuk.practice.match.listener;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.match.stats.MatchStats;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.SubState;

public class MatchInteractListener implements Listener {
	
	public static HandlerList handlerList = new HandlerList();
	
	@EventHandler
	public void onMatchInteractItems(final PlayerInteractEvent event) {
		if (!event.hasItem()) return;
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.STARTING) || profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			event.setCancelled(false);
		}
		if (profile.getGlobalState().getSubState().equals(SubState.STARTING) || profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			final ItemStack item = event.getItem();
			if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && item.getType() == Material.ENDER_PEARL && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				if (profile.getGlobalState().getSubState() != SubState.PLAYING) {
					event.setUseItemInHand(Result.DENY);
					event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Please wait until the fight is fully launched!");
					event.getPlayer().updateInventory();
					return;
				}
				MatchStats matchStats = profile.getMatchStats();
				if (!matchStats.isEnderPearlCooldownActive()) {
					matchStats.applyEnderPearlCooldown();
					return;
				}
				event.setUseItemInHand(Result.DENY);
				final double time = matchStats.getEnderPearlCooldown() / 1000.0D;
				final DecimalFormat df = new DecimalFormat("#.#");
				event.getPlayer().sendMessage(ChatColor.WHITE + "You can launch a pearl again in " + ChatColor.DARK_AQUA + df.format(time) + ChatColor.WHITE + " second" + (time > 1.0D ? "s" : ""));
				event.getPlayer().updateInventory();
				return;
			}
		}
	}
	
	@EventHandler
	public void onInventorySelected(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.STARTING) || profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			event.setCancelled(false);
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			final Player player = event.getPlayer();
			final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
			if (pm.getGlobalState().getSubState() != SubState.PLAYING) event.setCancelled(true);
		}
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
}
