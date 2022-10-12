package kezuk.bawz.listener;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import com.google.common.collect.Lists;

import co.aikar.idb.DB;
import kezuk.bawz.*;
import kezuk.bawz.core.Tag;
import kezuk.bawz.match.MatchManager;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.player.*;
import kezuk.bawz.request.DuelRequestStatus;
import kezuk.bawz.request.RequestManager;
import kezuk.bawz.utils.ItemSerializer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

public class InventoryListener implements Listener
{
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final PlayerManager pm = PlayerManager.getPlayers().get(event.getWhoClicked().getUniqueId());
        if (pm.getPlayerStatus().equals(Status.SPAWN)) {
            event.setCancelled(true);
        	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getUnrankedInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
                Practice.getInstance().getQueueManager().addPlayerToQueue(event.getWhoClicked().getUniqueId(), false, event.getCurrentItem().getItemMeta().getDisplayName());
                event.getWhoClicked().closeInventory();
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getRankedInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
                Practice.getInstance().getQueueManager().addPlayerToQueue(event.getWhoClicked().getUniqueId(), true, event.getCurrentItem().getItemMeta().getDisplayName());
                event.getWhoClicked().closeInventory();
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getPersonnalInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.GLASS) return;
            	if (event.getCurrentItem().getType() == Material.NAME_TAG) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getTagTypeInventory());
            	}
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getUtilsInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.GLASS) return;
            	if (event.getCurrentItem().getType() == Material.ENCHANTMENT_TABLE) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getEditableInventory());
            	}
            	if (event.getCurrentItem().getType() == Material.DIAMOND) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getLeaderboardInventory());
            	}
            	if (event.getCurrentItem().getType() == Material.ENDER_PORTAL) {
                    List<ItemStack> matchs = Lists.newArrayList();
                    for (MatchManager matchManager : Practice.getMatchs().values()) {
                    	for (UUID uuid : matchManager.getFirstList()) {
                    		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getLadder().displayName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Type" + ChatColor.RESET + ": " + ChatColor.AQUA + (matchManager.isRanked() ? "Ranked" : "Unranked"), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Arena" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getArena().getName(), " "};
                            ItemStack item = ItemSerializer.serialize(new ItemStack(matchManager.getLadder().material()), matchManager.getLadder().data(), ChatColor.GREEN + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.AQUA + " vs " + ChatColor.RED + Bukkit.getServer().getPlayer(matchManager.getOpponent(uuid)).getName(), Arrays.asList(lore));
                            matchs.add(item);
                    	}
                    }
                    Practice.getInstance().getInventoryManager().getSpectateInventory().refresh(matchs);
                    final Player player = (Player) event.getWhoClicked();
                    Practice.getInstance().getInventoryManager().getSpectateInventory().open(player, 1);
            	}
            	if (event.getCurrentItem().getType() == Material.PAPER) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getStartHostInventory());
            	}
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getTagTypeInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;
            	if (event.getCurrentItem().getType().equals(Material.NAME_TAG)) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getTagClassicInventory());
            	}
            	if (event.getCurrentItem().getType().equals(Material.BOOK)) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getTagJapanInventory());
            	}
            	if (event.getCurrentItem().getType().equals(Material.COMPASS)) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getTagRegionInventory());
            	}
            	if (event.getCurrentItem().getType().equals(Material.BUCKET)) {
            		pm.setTag(Tag.getTagByName("Normal"));
            		event.getWhoClicked().closeInventory();
            		final Player player = (Player) event.getWhoClicked();
            		player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You have been reset your tags!");
                	try {
    					DB.executeUpdate("UPDATE playersdata SET tag=? WHERE name=?", "Normal", player.getName());
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            	}
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getTagClassicInventory().getName()) || event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getTagJapanInventory().getName()) || event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getTagRegionInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            	final Tag tag = Tag.getTagByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            	pm.setTag(tag);
            	event.getWhoClicked().closeInventory();
            	final Player player = (Player) event.getWhoClicked();
            	player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You've been set the " + ChatColor.AQUA + tag.getDisplay() + ChatColor.DARK_AQUA + " tag!");
            	try {
					DB.executeUpdate("UPDATE playersdata SET tag=? WHERE name=?", ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()), player.getName());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            if (event.getClickedInventory().getName().equals(ChatColor.DARK_GRAY + "Spectate")) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE|| event.getCurrentItem().getType() == Material.COMPASS || event.getCurrentItem().getType() == Material.PAPER) return;
            	final Player player = (Player) event.getWhoClicked();
	            if (event.getCurrentItem().getType() == Material.ARROW) {
	                String str = event.getCurrentItem().getItemMeta().getLore().get(0).substring(7);
	                Practice.getInstance().getInventoryManager().getSpectateInventory().open(player, Integer.parseInt(str));
	                return;
	            }
	            else if (event.getCurrentItem().getType() == Material.LEVER) {
	                String str = event.getCurrentItem().getItemMeta().getLore().get(0).substring(7);
	                Practice.getInstance().getInventoryManager().getSpectateInventory().open(player, Integer.parseInt(str));
	                return;
	            }
            	player.closeInventory();
                String title = event.getCurrentItem().getItemMeta().getDisplayName();
                String arr[] = title.split(" ", 2);
                String first = arr[0];
                Practice.getMatchs().get(PlayerManager.getPlayers().get(Bukkit.getServer().getPlayer(ChatColor.stripColor(first)).getUniqueId()).getMatchUUID()).addSpectatorToMatch(player.getUniqueId(), Bukkit.getServer().getPlayer(ChatColor.stripColor(first)).getUniqueId());
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getStartHostInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;
                String title = event.getCurrentItem().getItemMeta().getDisplayName();
                String arr[] = title.split(" ", 2);
                int number = Integer.parseInt(ChatColor.stripColor(arr[0]));
                Practice.getInstance().getHostManager().startHost(event.getWhoClicked().getUniqueId(), number);
                event.getWhoClicked().closeInventory();
                Bukkit.broadcastMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + event.getWhoClicked().getName() + ChatColor.DARK_AQUA + " have launch a sumo host event with " + ChatColor.AQUA + number + ChatColor.DARK_AQUA + " size!");
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getDuelInventory().getName())) {
                final Player target = pm.getTargetDuel();
                new RequestManager(event.getWhoClicked().getUniqueId(), target.getUniqueId(), event.getCurrentItem().getItemMeta().getDisplayName(), false);
                event.getWhoClicked().closeInventory();
                pm.setDuelRequest(DuelRequestStatus.CANNOT);
            }
        }
        if (Practice.getMatchs().get(pm.getMatchUUID()) != null && Practice.getMatchs().get(pm.getMatchUUID()).getStatus() != MatchStatus.FINISHED) {
        	return;
        }
        if (event.getInventory().getName().contains(ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Preview of")) {
        	event.setCancelled(true);
        	if (event.getCurrentItem().getItemMeta().getDisplayName().contains("go to")) {
        		final Player player = (Player) event.getWhoClicked();
        		player.closeInventory();
        		String name = event.getCurrentItem().getItemMeta().getLore().get(1);
        		String uuidWithoutColor = ChatColor.stripColor(name);
        		player.chat("/inventory " + uuidWithoutColor);
        		return;
        	}
        	else {
        		return;
        	}
        }
        if (pm.getPlayerStatus().equals(Status.BUILD)) {
        	return;
        }
        event.setCancelled(true);
    }
}
