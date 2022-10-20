package kezuk.practice.match.spectate.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;

public class SpectateListener implements Listener {
	
	@EventHandler
	public void onInteractWithSpectateItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPECTATE)) {
			if(!event.hasItem()) return;
			if (event.getItem().getType().equals(Material.BLAZE_POWDER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        		new SpawnItems(event.getPlayer().getUniqueId());
        		Bukkit.getPlayer(event.getPlayer().getUniqueId()).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
                Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId()).setGlobalState(GlobalState.SPAWN);
			}
			event.setCancelled(true);
		}
	}

}
