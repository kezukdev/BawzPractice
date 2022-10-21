package kezuk.practice.party.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class PartyListener implements Listener {
	
	@EventHandler
	public void onInteractWithPartyItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (!event.hasItem()) return;
		if (profile.getGlobalState().equals(GlobalState.PARTY)) {
			if (profile.getGlobalState().getSubState().equals(SubState.NOTHING)) {
				if (event.getItem().getType().equals(Material.GOLD_AXE) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getPartyMatchInventory().getMatchInventory());
				}
				if (event.getItem().getType().equals(Material.PAPER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					if (Practice.getInstance().getRegisterCollections().getPartys().get(event.getPlayer().getUniqueId()) != null) {
						event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getPartyManageInventory().getManageInventory());
					}
					else {
						event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Currently in developpement!");
					}
				}
				if (event.getItem().getType().equals(Material.BLAZE_POWDER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Party.getPartyMap().get(event.getPlayer().getUniqueId()).removeToParty(event.getPlayer().getUniqueId(), false);
				}
			}
			if (profile.getGlobalState().getSubState().equals(SubState.QUEUE)) {
				if (event.getItem().getType().equals(Material.BLAZE_POWDER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					if (Practice.getInstance().getRegisterCollections().getPartys().get(event.getPlayer().getUniqueId()) != null) {
						Practice.getInstance().getRegisterObject().getQueueSystem().leaveQueue(event.getPlayer().getUniqueId());	
					}
					else {
						event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You cannot do this as a party member.");
					}
				}
			}
			if (profile.getGlobalState().getSubState().equals(SubState.STARTING) || profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
				return;
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteractWithPartyInventory(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.PARTY) && profile.getGlobalState().getSubState().equals(SubState.NOTHING)) {
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getPartyMatchInventory().getMatchInventory().getName())) {
				if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory());
				}
				if (event.getCurrentItem().getType().equals(Material.EMERALD)) {
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedPartyInventory());
				}
				if (event.getCurrentItem().getType().equals(Material.DIAMOND_CHESTPLATE)) {
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getSplitInventory());
				}
			}
			event.setCancelled(true);
		}
	}

}
