package kezuk.practice.event.inventory.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventSubType;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;

public class JoinInventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryInteractWithJoin(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getJoinInventory().getJoinInventory().getName())) {
				if (event.getCurrentItem().getType().equals(Material.WATER_BUCKET)) {
					if (Practice.getInstance().getRegisterObject().getEvent().isLaunched() && Practice.getInstance().getRegisterObject().getEvent().getEventType().getSubType().equals(EventSubType.WAITTING)) {
						event.getWhoClicked().closeInventory();
						Practice.getInstance().getRegisterObject().getEvent().addMemberToEvent(event.getWhoClicked().getUniqueId());
					}
					else {
						event.getWhoClicked().closeInventory();
						final Player player = (Player) event.getWhoClicked();
						player.sendMessage(ChatColor.AQUA + "Sorry but no events are currently available! Either it is already in progress or nobody has created one!");
					}
				}	
				event.setCancelled(true);
				return;
			}
		}
	}

}
