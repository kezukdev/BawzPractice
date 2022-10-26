package kezuk.practice.player.personnal.listener;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class PersonnalListener implements Listener {
	
	public void onClickInventory(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (event.getCurrentItem() == null) return;
		if (profile.getSubState().equals(SubState.BUILD)) {
			event.setCancelled(false);
			return;
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getPersonnalInventory().getPersonnalInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)){
			if (event.getCurrentItem().getType().equals(Material.NAME_TAG)) {
				event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getTagInventory().getTagTypeInventory());
			}
			event.setCancelled(true);
		}
	}

}
