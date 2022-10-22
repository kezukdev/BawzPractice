package kezuk.practice.event.host.items.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class HostInteractItemListener implements Listener {
	
	@EventHandler
	public void onClickWithLeave(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.EVENT)) {
			if (!event.hasItem()) return;
			if (profile.getSubState().equals(SubState.NOTHING)) {
				if (event.getItem().getType().equals(Material.BLAZE_POWDER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Practice.getInstance().getRegisterObject().getEvent().removeMemberToEvent(event.getPlayer().getUniqueId(), false);
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryReactEvent(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.EVENT)) {
			if (profile.getSubState().equals(SubState.PLAYING)) {
				return;
			}
			event.setCancelled(true);
		}
	}

}
