package kezuk.practice.player.personnal.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class PersonnalListener implements Listener {
	
	@EventHandler
	public void onClickInventory(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (event.getCurrentItem() == null) return;
		if (profile.getSubState().equals(SubState.BUILD)) {
			event.setCancelled(false);
			return;
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(profile.getPersonnalInventory().getPersonnalInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)){
			if (event.getCurrentItem().getType().equals(Material.NAME_TAG)) {
				event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getTagInventory().getTagTypeInventory());
			}
			if (event.getCurrentItem().getType().equals(Material.DAYLIGHT_DETECTOR)) {
				event.getWhoClicked().openInventory(profile.getPlayerCache().getSettings().getSettings());
			}
			event.setCancelled(true);
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(profile.getPlayerCache().getSettings().getSettings().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)){
			if (event.getCurrentItem().getType().equals(Material.PAINTING)) {
				profile.getPlayerCache().setScoreboard(profile.getPlayerCache().isScoreboard() ? false : true);
				profile.getPlayerCache().getSettings().setPreviewSettings(event.getWhoClicked().getUniqueId());
				DB.executeUpdateAsync("UPDATE playersdata SET scoreboard=? WHERE name=?", Boolean.toString(profile.getPlayerCache().isScoreboard()) , event.getWhoClicked().getName());
			}
			if (event.getCurrentItem().getType().equals(Material.PAPER)) {
				profile.getPlayerCache().setPm(profile.getPlayerCache().isPm() ? false : true);
				profile.getPlayerCache().getSettings().setPreviewSettings(event.getWhoClicked().getUniqueId());
				DB.executeUpdateAsync("UPDATE playersdata SET pm=? WHERE name=?", Boolean.toString(profile.getPlayerCache().isPm()) , event.getWhoClicked().getName());
			}
			if (event.getCurrentItem().getType().equals(Material.IRON_SWORD)) {
				profile.getPlayerCache().setDuel(profile.getPlayerCache().isDuel() ? false : true);
				profile.getPlayerCache().getSettings().setPreviewSettings(event.getWhoClicked().getUniqueId());
				DB.executeUpdateAsync("UPDATE playersdata SET duel=? WHERE name=?", Boolean.toString(profile.getPlayerCache().isDuel()) , event.getWhoClicked().getName());
			}
		}
	}

}
