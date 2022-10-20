package kezuk.practice.player.utils.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import kezuk.practice.Practice;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;

public class UtilsListener implements Listener {
	
	@EventHandler
	public void onInventoryClickOfUtils(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getUtilsInventory().getUtilsInventory().getName())) {
				if (event.getCurrentItem().getType().equals(Material.DIAMOND)) {
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getUtilsInventory().getLeaderboardInventory());
				}
				if (event.getCurrentItem().getType().equals(Material.CAKE)) {
					event.getWhoClicked().closeInventory();
					new Party(event.getWhoClicked().getUniqueId());
				}
				if (event.getCurrentItem().getType().equals(Material.ENDER_PORTAL)) {
					event.getWhoClicked().closeInventory();
					Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().open((Player) event.getWhoClicked(), 1);
				}
				event.setCancelled(true);
			}
		}
	}

}
