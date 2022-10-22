package kezuk.practice.player.personnal.listener;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;

public class PersonnalListener implements Listener {
	
	public void onClickInventory(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (event.getCurrentItem() == null) return;
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getPersonnalInventory().getPersonnalInventory().getName())){
				if (event.getCurrentItem().getType().equals(Material.NAME_TAG)) event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getTagInventory().getTagTypeInventory());
				event.setCancelled(true);
			}	
		}
	}

}
