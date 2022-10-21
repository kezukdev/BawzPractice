package kezuk.practice.ladders.inventory.listener;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.request.Requesting;

public class LadderInventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryLadderClick(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedInventory().getName())) {
				event.getWhoClicked().closeInventory();
				List<UUID> uuid = Lists.newArrayList(event.getWhoClicked().getUniqueId());
				Practice.getInstance().getRegisterObject().getQueueSystem().addPlayerToQueue(uuid, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false);
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getRankedInventory().getName())) {
				event.getWhoClicked().closeInventory();
				List<UUID> uuid = Lists.newArrayList(event.getWhoClicked().getUniqueId());
				Practice.getInstance().getRegisterObject().getQueueSystem().addPlayerToQueue(uuid, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), true, false);
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory().getName())) {
				if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
				event.getWhoClicked().closeInventory();
				Practice.getInstance().getRegisterObject().getEvent().startHost(event.getWhoClicked().getUniqueId(), EventType.FFA, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()));
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getDuelInventory().getName())) {
				if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
				event.getWhoClicked().closeInventory();
				new Requesting(event.getWhoClicked().getUniqueId(), profile.getTargetDuel().getUniqueId(), event.getCurrentItem().getItemMeta().getDisplayName());
			}
			event.setCancelled(true);
		}
		if (profile.getGlobalState().equals(GlobalState.PARTY) && profile.getGlobalState().getSubState().equals(SubState.NOTHING)) {
			if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory().getName())) {
				event.getWhoClicked().closeInventory();
				if (Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() == 1) {
					final Player player = (Player) event.getWhoClicked();
					player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You need more players in your party!");
					return;
				}
				new StartMatch(null, null, Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList(), Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false);	
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getSplitInventory().getName())) {
				event.getWhoClicked().closeInventory();
				if (Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() == 1) {
					final Player player = (Player) event.getWhoClicked();
					player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You need more players in your party!");
					return;
				}
				final int size = Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size();
				List<UUID> firstList = Lists.newArrayList();
				List<UUID> secondList = Lists.newArrayList();
				Collections.shuffle(Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList());
				for (int i = 0; i < size; i++) {
					if (i < (size + 1)/2) {
						firstList.add(Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().get(i));
					}
					else {
						secondList.add(Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().get(i));
					}
				}
				new StartMatch(firstList, secondList, null, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false);
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedPartyInventory().getName())) {
				event.getWhoClicked().closeInventory();
				if (Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() > 2) {
					final Player player = (Player) event.getWhoClicked();
					player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You must be a maximum of two members for this!");
					return;
				}
				event.getWhoClicked().closeInventory();
				List<UUID> uuid = Lists.newArrayList(Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList());
				Practice.getInstance().getRegisterObject().getQueueSystem().addPlayerToQueue(uuid, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, true);
			}
			event.setCancelled(true);
		}
	}

}
