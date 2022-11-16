package kezuk.practice.queue.listener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.queue.QueueSystem;
import kezuk.practice.queue.QueueSystem.QueueEntry;
import kezuk.practice.utils.GameUtils;
import kezuk.practice.utils.ItemSerializer;

public class QueueListener implements Listener {
	
	@EventHandler
	public void onInteractWithQueueItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getSubState().equals(SubState.BUILD)) {
			event.setCancelled(false);
			return;
		}
		if (profile.getGlobalState().equals(GlobalState.QUEUE)) {
			if (!event.hasItem()) return;
			if (event.getItem().getType().equals(Material.BLAZE_POWDER)) {
				if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Practice.getInstance().getRegisterObject().getQueueSystem().leaveQueue(event.getPlayer().getUniqueId());
				}
			}
			if (event.getItem().getType().equals(Material.PAPER)) {
				if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					List<ItemStack> queue = Lists.newArrayList();
			        for (QueueEntry queueEntry : Practice.getInstance().getRegisterObject().getQueueSystem().getQueue().values()) {
			        	for (UUID uuid : queueEntry.getUuid()) {
			        		final ItemStack item = ItemSerializer.serialize(new ItemStack(queueEntry.getLadder().material()), queueEntry.getLadder().data(), ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + Bukkit.getPlayer(uuid).getName(), Arrays.asList("", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.WHITE + ": " + ChatColor.stripColor(queueEntry.getLadder().displayName()) + ChatColor.GRAY + " [" + (queueEntry.isRanked() ? "§5Ranked" : "§eCasual") + ChatColor.GRAY + "]" , "", ChatColor.DARK_GRAY + "(Left-Click) Join his queue!", ChatColor.DARK_GRAY + "(Right-Click) Display his profile!"));
			        		queue.add(item);
			        	}
			        }
			        Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().refresh(queue);
			        Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().open(event.getPlayer(), 1);
				}
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteractWithInventory(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.QUEUE)) {
			if (event.getClickedInventory().getName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Potential Opponents:")) {
	        	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE|| event.getCurrentItem().getType() == Material.CACTUS || event.getCurrentItem().getType() == Material.PAPER) return;
	        	final Player player = (Player) event.getWhoClicked();
	            if (event.getCurrentItem().getType() == Material.ARROW) {
	                String str = event.getCurrentItem().getItemMeta().getLore().get(0).substring(7);
	                Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().open(player, Integer.parseInt(str));
	                return;
	            }
	            else if (event.getCurrentItem().getType() == Material.LEVER) {
	                String str = event.getCurrentItem().getItemMeta().getLore().get(0).substring(7);
	                Practice.getInstance().getRegisterObject().getQueuePotential().getPotential().open(player, Integer.parseInt(str));
	                return;
	            }
	        	player.closeInventory();
	            String title = event.getCurrentItem().getItemMeta().getDisplayName();
	            String arr[] = title.split(" ", 2);
	            String first = arr[0];
	            final Player target = Bukkit.getPlayer(ChatColor.stripColor(first));
	            if (event.getClick().equals(ClickType.LEFT)) {
	            	if (target == player) return;
	            	final QueueSystem queue = Practice.getInstance().getRegisterObject().getQueueSystem();
	            	queue.leaveQueue(player.getUniqueId());
	            	queue.addPlayerToQueue(Arrays.asList(player.getUniqueId()), queue.getQueue().get(target.getUniqueId()).getLadder(), queue.getQueue().get(target.getUniqueId()).isRanked(), queue.getQueue().get(target.getUniqueId()).isTo2());
	            }
	            if (event.getClick().equals(ClickType.RIGHT)) {
	            	final Profile targetProfile = Practice.getInstance().getRegisterCollections().getProfile().get(target.getUniqueId());
	            	player.openInventory(targetProfile.getPersonnalInventory().getPersonnalInventory());
	            }
			}
			event.setCancelled(true);
			return;
		}
	}

}
