package kezuk.practice.match.spectate.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;

public class SpectateListener implements Listener {
	
	@EventHandler
	public void onInteractWithSpectateItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPECTATE)) {
			if(!event.hasItem()) return;
			if (event.getItem().getType().equals(Material.BLAZE_POWDER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				GameUtils.getMatchManagerBySpectator(event.getPlayer().getUniqueId()).getSpectator().remove(event.getPlayer().getUniqueId());
        		new SpawnItems(event.getPlayer().getUniqueId());
        		Bukkit.getPlayer(event.getPlayer().getUniqueId()).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
                Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId()).setGlobalState(GlobalState.SPAWN);
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClickOfSpectate(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN) && profile.getGlobalState().getSubState().equals(SubState.NOTHING)) {
        	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE|| event.getCurrentItem().getType() == Material.COMPASS || event.getCurrentItem().getType() == Material.PAPER) return;
        	final Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem().getType() == Material.ARROW) {
                String str = event.getCurrentItem().getItemMeta().getLore().get(0).substring(7);
                Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().open(player, Integer.parseInt(str));
                return;
            }
            else if (event.getCurrentItem().getType() == Material.LEVER) {
                String str = event.getCurrentItem().getItemMeta().getLore().get(0).substring(7);
                Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().open(player, Integer.parseInt(str));
                return;
            }
        	player.closeInventory();
            String title = event.getCurrentItem().getItemMeta().getDisplayName();
            String arr[] = title.split(" ", 2);
            String first = arr[0];
            GameUtils.addSpectatorToMatch(event.getWhoClicked().getUniqueId(), Bukkit.getPlayer(ChatColor.stripColor(first)).getUniqueId());
            event.setCancelled(true);
		}
	}

}
