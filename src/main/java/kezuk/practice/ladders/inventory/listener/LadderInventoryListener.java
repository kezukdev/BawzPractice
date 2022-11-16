package kezuk.practice.ladders.inventory.listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.queue.QueueSystem.QueueEntry;
import kezuk.practice.request.Requesting;
import kezuk.practice.utils.ItemSerializer;

public class LadderInventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryLadderClick(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getSubState().equals(SubState.BUILD)) { 
			event.setCancelled(false);
			return;
		}
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getQueueInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)) {
			final Player player = (Player) event.getWhoClicked();
			if (event.getCurrentItem().getType().equals(Material.STONE_SWORD)) {
				if (event.getClick().equals(ClickType.MIDDLE)) {
					List<ItemStack> queue = Lists.newArrayList();
			        for (QueueEntry queueEntry : Practice.getInstance().getRegisterObject().getQueueSystem().getQueue().values()) {
			        	for (UUID uuid : queueEntry.getUuid()) {
			        		final ItemStack item = ItemSerializer.serialize(new ItemStack(queueEntry.getLadder().material()), queueEntry.getLadder().data(), ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + Bukkit.getPlayer(uuid).getName(), Arrays.asList("", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.WHITE + ": " + ChatColor.stripColor(queueEntry.getLadder().displayName()) + ChatColor.GRAY + " [" + (queueEntry.isRanked() ? "§5Ranked" : "§eCasual") + ChatColor.GRAY + "]" , "", ChatColor.DARK_GRAY + "(Left-Click) Join his queue!"));
			        		queue.add(item);
			        	}
			        }
			        Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().refresh(queue);
			        Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().open(player, 1);
					return;
				}
				event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedInventory());
			}
			if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
				if (event.getClick().equals(ClickType.MIDDLE)) {
					List<ItemStack> queue = Lists.newArrayList();
			        for (QueueEntry queueEntry : Practice.getInstance().getRegisterObject().getQueueSystem().getQueue().values()) {
			        	for (UUID uuid : queueEntry.getUuid()) {
			        		final ItemStack item = ItemSerializer.serialize(new ItemStack(queueEntry.getLadder().material()), queueEntry.getLadder().data(), ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + Bukkit.getPlayer(uuid).getName(), Arrays.asList("", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.WHITE + ": " + ChatColor.stripColor(queueEntry.getLadder().displayName()) + ChatColor.GRAY + " [" + (queueEntry.isRanked() ? "§5Ranked" : "§eCasual") + ChatColor.GRAY + "]" , "", ChatColor.DARK_GRAY + "(Left-Click) Join his queue!"));
			        		queue.add(item);
			        	}
			        }
			        Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().refresh(queue);
			        Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().open(player, 1);
					return;
				}
				event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getRankedInventory());
			}
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)) {
			event.getWhoClicked().closeInventory();
			List<UUID> uuid = Lists.newArrayList(event.getWhoClicked().getUniqueId());
			Practice.getInstance().getRegisterObject().getQueueSystem().addPlayerToQueue(uuid, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false);	
			event.setCancelled(true);
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getRankedInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)) {
			event.getWhoClicked().closeInventory();
			List<UUID> uuid = Lists.newArrayList(event.getWhoClicked().getUniqueId());
			Practice.getInstance().getRegisterObject().getQueueSystem().addPlayerToQueue(uuid, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), true, false);	
			event.setCancelled(true);
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getRankedInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)) {
			event.getWhoClicked().closeInventory();
			List<UUID> uuid = Lists.newArrayList(event.getWhoClicked().getUniqueId());
			Practice.getInstance().getRegisterObject().getQueueSystem().addPlayerToQueue(uuid, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), true, false);	
			event.setCancelled(true);
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
			event.getWhoClicked().closeInventory();
			Practice.getInstance().getRegisterObject().getEvent().startHost(event.getWhoClicked().getUniqueId(), EventType.FFA, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()));
			event.setCancelled(true);
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getDuelInventory().getName()) && profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
			event.getWhoClicked().closeInventory();
			new Requesting(event.getWhoClicked().getUniqueId(), profile.getTargetDuel().getUniqueId(), event.getCurrentItem().getItemMeta().getDisplayName());	
			event.setCancelled(true);
		}
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory().getName()) && profile.getGlobalState().equals(GlobalState.PARTY) && profile.getSubState().equals(SubState.NOTHING)) {
			event.getWhoClicked().closeInventory();
			if (Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() == 1) {
				final Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You need more players in your party!");
				return;
			}
			new StartMatch(null, null, Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList(), Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false, false);	
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory().getName()) && profile.getGlobalState().equals(GlobalState.PARTY) && profile.getSubState().equals(SubState.NOTHING)) {
			event.getWhoClicked().closeInventory();
			if (Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() == 1) {
				final Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You need more players in your party!");
				return;
			}
			new StartMatch(null, null, Party.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList(), Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false, false);	
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getSplitInventory().getName()) && profile.getGlobalState().equals(GlobalState.PARTY) && profile.getSubState().equals(SubState.NOTHING)) {
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
			new StartMatch(firstList, secondList, null, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), false, false, false);
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedPartyInventory().getName()) && profile.getGlobalState().equals(GlobalState.PARTY) && profile.getSubState().equals(SubState.NOTHING)) {
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
	}

}
