package kezuk.practice.queue.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class QueueListener implements Listener {
	
	@EventHandler
	public void onInteractWithQueueItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getSubState().equals(SubState.BUILD)) {
			event.setCancelled(false);
			return;
		}
		if (profile.getGlobalState().equals(GlobalState.QUEUE)) {
			if (!event.hasItem()) return;
			if (event.getItem().getType().equals(Material.BLAZE_POWDER)) {
				if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Practice.getInstance().getRegisterObject().getQueueSystem().leaveQueue(event.getPlayer().getUniqueId());
				}
			}
			event.setCancelled(true);
		}
	}

}
