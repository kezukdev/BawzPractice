package kezuk.bawz.listener;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import com.google.common.collect.Lists;

import co.aikar.idb.DB;
import kezuk.bawz.*;
import kezuk.bawz.core.Tag;
import kezuk.bawz.host.HostManager;
import kezuk.bawz.host.HostType;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.match.manager.FfaMatchManager;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.party.PartyState;
import kezuk.bawz.party.manager.PartyManager;
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
                Practice.getInstance().getQueueManager().addPlayerToQueue(event.getWhoClicked().getUniqueId(), false, event.getCurrentItem().getItemMeta().getDisplayName(), false);
                event.getWhoClicked().closeInventory();
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getRankedInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
                Practice.getInstance().getQueueManager().addPlayerToQueue(event.getWhoClicked().getUniqueId(), true, event.getCurrentItem().getItemMeta().getDisplayName(), false);
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
            	if (event.getCurrentItem().getType() == Material.CAKE) {
            		event.getWhoClicked().closeInventory();
            		new PartyManager(event.getWhoClicked().getUniqueId());
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
            if (event.getClickedInventory().getName().equals(ChatColor.DARK_GRAY + "Event")) {
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
            	final UUID hostUUID = UUID.fromString(event.getCurrentItem().getItemMeta().getLore().get(3));
            	Practice.getHosts().get(hostUUID).addMember(event.getWhoClicked().getUniqueId(), hostUUID);
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getStartHostInventory().getName())) {
            	if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;
            	if (event.getCurrentItem().getType().equals(Material.FLINT)) {
            		event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getFfaHostInventory());
            		return;
            	}
                HostType type = HostType.valueOf(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                Practice.getInstance().getHostManager().startHost(event.getWhoClicked().getUniqueId(), 20, type);
                event.getWhoClicked().closeInventory();
            }
            if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getDuelInventory().getName())) {
                final Player target = pm.getTargetDuel();
                new RequestManager(event.getWhoClicked().getUniqueId(), target.getUniqueId(), event.getCurrentItem().getItemMeta().getDisplayName());
                event.getWhoClicked().closeInventory();
                pm.setDuelRequest(DuelRequestStatus.CANNOT);
            }
        }
        if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getFfaHostInventory().getName())) {
        	final Ladders ladder = Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName());
        	HostManager host = new HostManager();
        	host.startHost(event.getWhoClicked().getUniqueId(), 20, HostType.FFA, ladder);
        }
        if (Practice.getMatchs().get(pm.getMatchUUID()) != null && Practice.getMatchs().get(pm.getMatchUUID()).getStatus() != MatchStatus.FINISHED) {
        	return;
        }
        if (pm.getPlayerStatus().equals(Status.PARTY) && PartyManager.getPartyMap().get(event.getWhoClicked().getUniqueId()).getStatus().equals(PartyState.SPAWN)) {
        	event.setCancelled(true);
        	if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getPartyMatchInventory().getName())) {
        		if (PartyManager.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() > 1) {
            		if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
            			event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getFfaInventory());
            		}
            		if (event.getCurrentItem().getType().equals(Material.EMERALD)) {
            			if (PartyManager.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList().size() > 2) {
                			event.getWhoClicked().closeInventory();
                			final Player player = (Player) event.getWhoClicked();
                			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You can't join the 2v2 you just have need of 2 players.");
            				return;
            			}
            			event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getQueueInventory());
            		}
            		if (event.getCurrentItem().getType().equals(Material.DIAMOND_CHESTPLATE)) {
            			event.getWhoClicked().openInventory(Practice.getInstance().getInventoryManager().getSplitInventory());
            		}
            		return;
        		}
        		else {
        			event.getWhoClicked().closeInventory();
        			final Player player = (Player) event.getWhoClicked();
        			player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You need minimum two players in party for made that!");
        		}
        	}
        	if (event.getClickedInventory().getName().equals(Practice.getInstance().getInventoryManager().getFfaInventory().getName())) {
        		FfaMatchManager ffaMatch = new FfaMatchManager();
        		event.getWhoClicked().closeInventory();
        		ffaMatch.startMatch(PartyManager.getPartyMap().get(event.getWhoClicked().getUniqueId()).getPartyList(), Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()));
        	}
        }
        if (pm.getPlayerStatus().equals(Status.PARTY) && PartyManager.getPartyMap().get(event.getWhoClicked().getUniqueId()).getStatus().equals(PartyState.FIGHT)) {
        	return;
        }
        if (event.getClickedInventory().getName().contains(ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Preview of")) {
        	event.setCancelled(true);
        	if (event.getCurrentItem().getType().equals(Material.PISTON_STICKY_BASE)) {
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
