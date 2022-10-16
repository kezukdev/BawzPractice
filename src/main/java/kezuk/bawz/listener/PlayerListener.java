package kezuk.bawz.listener;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;

import kezuk.bawz.*;
import kezuk.bawz.host.HostManager;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.party.PartyState;
import kezuk.bawz.party.manager.PartyManager;
import kezuk.bawz.player.*;
import net.luckperms.api.cacheddata.CachedMetaData;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        event.setJoinMessage((String)null);
        final Player player = event.getPlayer();
        PlayerManager pm = new PlayerManager(player.getUniqueId());
        pm.sendToSpawn();
        pm.setPlayerStatus(Status.SPAWN);
		final CachedMetaData metaData = Practice.getInstance().getLuckPerms().getPlayerAdapter(Player.class).getMetaData(event.getPlayer());
    	player.setPlayerListName(ChatColor.translateAlternateColorCodes('&',metaData.getSuffix() + player.getName()));
    }
    
    @EventHandler
    public void onPlayerLeft(final PlayerQuitEvent event) {
        if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.QUEUE)) {
            Practice.getInstance().getQueueManager().leaveQueue(event.getPlayer().getUniqueId());
        }
        if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.FIGHT) && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()) != null && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).getStatus() != MatchStatus.FINISHED && Practice.getMatchs().containsKey(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID())) {
        	Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).endMatch(event.getPlayer().getUniqueId(), Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).getOpponent(event.getPlayer().getUniqueId()), PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID(), false);
        }
        if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.HOST) && Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()) != null && Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).getStatus() != MatchStatus.FINISHED && Practice.getFfaMatchs().containsKey(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID())) {
        	Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).addDisconnected(event.getPlayer().getUniqueId());
        }
        if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.PARTY) && Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()) != null && Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).getStatus() != MatchStatus.FINISHED && Practice.getFfaMatchs().containsKey(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID())) {
        	Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).addDisconnected(event.getPlayer().getUniqueId());
        }
        if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.SPECTATE)) {
        	Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getMatchUUID()).getSpectator().remove(event.getPlayer().getUniqueId());
        }
        if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.PARTY)) {
        	PartyManager.getPartyMap().get(event.getPlayer().getUniqueId()).removeToParty(event.getPlayer().getUniqueId(), true);
        }
        event.setQuitMessage((String)null);
    }
    
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        onPlayerLeft(new PlayerQuitEvent(event.getPlayer(), null));
    }
    
    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event) {
    	if (Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()) != null && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()).getLadder().displayName().equals(ChatColor.DARK_AQUA + "Soup") || Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()) != null && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()).getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo") || Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()) != null && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()).getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
    		event.setCancelled(true);
    		return;
    	}
    	if (PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getPlayerStatus().equals(Status.FIGHT) && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()) != null && Practice.getMatchs().get(PlayerManager.getPlayers().get(event.getEntity().getUniqueId()).getMatchUUID()).getStatus().equals(MatchStatus.PLAYING)) {
    		return;
    	}
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
    	if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.BUILD)) {
    		return;
    	}
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
    	if (PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerStatus().equals(Status.BUILD)) {
    		return;
    	}
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        final Action action = event.getAction();
        final PlayerManager pm = PlayerManager.getPlayers().get(event.getPlayer().getUniqueId());
        if (pm.getPlayerStatus().equals(Status.SPAWN)) {
            event.setCancelled(true);
            if (item == null) {
                return;
            }
            if (item.getType().equals((Object)Material.STONE_SWORD) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                event.getPlayer().openInventory(Practice.getInstance().getInventoryManager().getUnrankedInventory());
                return;
            }
            if (item.getType().equals((Object)Material.DIAMOND_SWORD) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                event.getPlayer().openInventory(Practice.getInstance().getInventoryManager().getRankedInventory());
                return;
            }
            if (item.getType().equals((Object)Material.NETHER_STAR) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                event.getPlayer().openInventory(Practice.getInstance().getInventoryManager().getPersonnalInventory());
                return;
            }
            if (item.getType().equals((Object)Material.BOOK) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                event.getPlayer().openInventory(Practice.getInstance().getInventoryManager().getUtilsInventory());
                return;
            }
        }
        if (pm.getPlayerStatus().equals(Status.PARTY) && PartyManager.getPartyMap().get(event.getPlayer().getUniqueId()).getStatus() != PartyState.FIGHT) {
            event.setCancelled(true);
            if (item == null) {
                return;
            }
            if (item.getType().equals((Object)Material.GOLD_AXE) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
            	event.getPlayer().openInventory(Practice.getInstance().getInventoryManager().getPartyMatchInventory());
            }
            if (item.getType().equals((Object)Material.BLAZE_POWDER) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                PartyManager.getPartyMap().get(event.getPlayer().getUniqueId()).removeToParty(event.getPlayer().getUniqueId(), false);
            }
        }
        if (pm.getPlayerStatus().equals(Status.QUEUE)) {
            if (item == null) {
                return;
            }
            if (item.getType().equals((Object)Material.BLAZE_POWDER) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                event.setCancelled(true);
                Practice.getInstance().getQueueManager().leaveQueue(event.getPlayer().getUniqueId());
            }
        }
        if (pm.getPlayerStatus().equals(Status.SPECTATE)) {
            if (item == null) {
                return;
            }
            if (item.getType().equals((Object)Material.BLAZE_POWDER) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
                event.setCancelled(true);
                MatchManager match = Practice.getMatchs().get(pm.getMatchUUID());
                match.getSpectator().remove(event.getPlayer().getUniqueId());
                pm.setMatchUUID(null);
                event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You don't wan't to viewing this match more time.");
                PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).sendToSpawn();
            }
        }
        if (pm.getPlayerStatus().equals(Status.HOST)) {
            if (item == null) {
                return;
            }
            if (item.getType().equals((Object)Material.BLAZE_POWDER) && (action.equals((Object)Action.RIGHT_CLICK_AIR) ^ action.equals((Object)Action.RIGHT_CLICK_BLOCK))) {
            	event.setCancelled(true);
            	Practice.getHosts().get(pm.getHostUUID()).removeMember(event.getPlayer().getUniqueId());
            }
        }
    }
}
