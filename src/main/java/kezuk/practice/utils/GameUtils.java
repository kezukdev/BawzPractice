package kezuk.practice.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.event.host.Event;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.match.StartMatch;
import kezuk.practice.match.inventory.MatchSeeInventory;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;

public class GameUtils {
	
	public static void displayMatchPlayer(final Player player) {
		for (StartMatch match : Practice.getInstance().getRegisterCollections().getMatchs().values()) {
			final List<UUID> allPlayers = Lists.newArrayList();
			if (match.getFirstList() != null) {
				allPlayers.addAll(match.getFirstList());
				allPlayers.addAll(match.getSecondList());
			}
			if (match.getPlayers() != null) {
				if (Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState().equals(GlobalState.PARTY)) {
				}
				allPlayers.addAll(match.getPlayers());
			}
			allPlayers.addAll(match.getSpectator());
			for (UUID lotOfPlayer : allPlayers) {
				Bukkit.getPlayer(lotOfPlayer).hidePlayer(player);
				player.hidePlayer(Bukkit.getPlayer(lotOfPlayer));
			}
			for (UUID uuid : Party.getPartyMap().get(player.getUniqueId()).getPartyList()) {
				if (!Bukkit.getPlayer(uuid).canSee(player)) {
					Bukkit.getPlayer(uuid).showPlayer(player);
				}
				if (!player.canSee(Bukkit.getPlayer(uuid))) {
					player.showPlayer(Bukkit.getPlayer(uuid));
				}
			}
		}
	}
	
    public static UUID getOpponent(final UUID uuid) {
    	UUID opponentUUID = null;
    	final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
    	if (Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getFirstList().contains(uuid)) {
    		for (UUID uuid1 : Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getSecondList()) {
    			opponentUUID = uuid1;
    		}
    	}
    	if (Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getSecondList().contains(uuid)) {
    		for (UUID uuid1 : Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getFirstList()) {
    			opponentUUID = uuid1;
    		}
    	}
    	return opponentUUID;
    }
    
	public static void addKill(final UUID uuid, final UUID killer) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		if(Practice.getInstance().getRegisterObject().getEvent().isLaunched()) {
			if (Practice.getInstance().getRegisterObject().getEvent().getEventType().equals(EventType.OITC) && Practice.getInstance().getRegisterObject().getEvent().getMembers().contains(uuid)) {
				Practice.getInstance().getRegisterObject().getEvent().getOitcEvent().addKill(killer, uuid);
				return;
			}	
		}
		StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID());
		match.getAlive().remove(uuid);
		if(match.getFirstList() != null) {
			if (match.getFirstList().contains(uuid)) {
				match.setAliveFirst(match.getAliveFirst() - 1);
			}
			else {
				match.setAliveSecond(match.getAliveSecond() - 1);
			}
		}
		if (match.getPlayers() != null) {
			match.getPlayers().remove(uuid);	
		}
		match.getSpectator().add(uuid);
		for (UUID ig : match.getAlive()) {
			Bukkit.getPlayer(ig).hidePlayer(Bukkit.getPlayer(uuid));
		}
		if (match.getSpectator().size() != 0) {
			for (UUID spectator : match.getSpectator()) {
				Bukkit.getPlayer(uuid).showPlayer(Bukkit.getPlayer(spectator));
			}	
		}
		for (PotionEffect effect : Bukkit.getPlayer(uuid).getActivePotionEffects()) {
			Bukkit.getPlayer(uuid).removePotionEffect(effect.getType());
		}
		Bukkit.getPlayer(uuid).setSaturation(100000.0f);
		Bukkit.getPlayer(uuid).setAllowFlight(true);
		Bukkit.getPlayer(uuid).setFlying(true);
		if (Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getGlobalState().equals(GlobalState.PARTY)) {
	        Bukkit.getPlayer(uuid).getInventory().clear();
	        Bukkit.getPlayer(uuid).getInventory().setItem(8, ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Leave Party Spectate."));
	        Bukkit.getPlayer(uuid).updateInventory();	
		}
		List<UUID> allInMatch = Lists.newArrayList(match.getAlive());
		allInMatch.addAll(match.getSpectator());
		if (match.getPlayers() != null) {
			match.getPlayers().remove(uuid);
		}
		for (UUID all : allInMatch) {
			if (killer != null) {
				Bukkit.getPlayer(all).sendMessage(Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have got killed by " + ChatColor.WHITE + Bukkit.getPlayer(killer).getName());	
			}
			else {
				Bukkit.getPlayer(all).sendMessage(Bukkit.getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " has slain!");	
			}
		}
		if (match.getPlayers() != null) {
			if (match.getAlive().size() == 1) {
				for (UUID winner : match.getAlive()) {
					match.endMatch(uuid, winner, Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID(), true);
				}
			}	
		}
		if (match.getFirstList() != null) {
			if (match.getAliveFirst() == 0 || match.getAliveSecond() == 0) {
				for (UUID winner : match.getAlive()) {
					match.endMatch(uuid, winner, Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID(), true);
				}
			}
		}
		allInMatch.clear();
	}
	
	public static void addDisconnected(final UUID uuid) {
		if (Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID()) != null) {
			StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID());
			if (match.getPlayers() != null) {
				match.getPlayers().remove(uuid);	
			}
			if (match.getAlive().contains(uuid)) {
				for (UUID uuidIG : match.getAlive()) {
					Bukkit.getPlayer(uuidIG).sendMessage(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(uuid).getName() + ChatColor.AQUA + " have disconnected.");
				}
				match.getAlive().remove(uuid);
				if (match.getAlive().size() == 1) {
					for (UUID alive : match.getAlive()) {
						match.endMatch(uuid, alive, Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID(), false);
					}
				}
			}
			if (match.getSpectator().contains(uuid)) {
				match.getSpectator().remove(uuid);	
			}	
		}
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		final Event event = Practice.getInstance().getRegisterObject().getEvent();
		if (profile.getGlobalState().equals(GlobalState.EVENT)) {
			if (profile.getSubState().equals(SubState.PLAYING)) {
				if (event.getEventType().equals(EventType.SUMO)) {
					final UUID winner = event.getSumoEvent().getFirstUUID() == uuid ? event.getSumoEvent().getSecondUUID() : Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().getFirstUUID();
					event.getSumoEvent().newRound(winner, uuid);
				}
				if (event.getEventType().equals(EventType.OITC)) {
					event.getOitcEvent().getAlive().remove(uuid);
				}
			}
			else {
				if (Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().getAlives().contains(uuid)) {
					Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().getAlives().remove(uuid);	
				}
				if (Practice.getInstance().getRegisterObject().getEvent().getOitcEvent().getAlive().contains(uuid)) {
					Practice.getInstance().getRegisterObject().getEvent().getOitcEvent().getAlive().remove(uuid);	
				}
				event.removeMemberToEvent(uuid, true);
			}
		}
		if (profile.getGlobalState().equals(GlobalState.EVENT) && event.getEventType().equals(EventType.FFA)) {
			if (profile.getSubState().equals(SubState.PLAYING)) {
				if (event.getEventType().equals(EventType.FFA)) {
					event.getOitcEvent().getAlive().remove(uuid);
				}
			}
			else {
				event.removeMemberToEvent(uuid, true);
			}
		}
		if (profile.getGlobalState().equals(GlobalState.QUEUE)) {
			Practice.getInstance().getRegisterObject().getQueueSystem().leaveQueue(uuid);
		}
		if (profile.getGlobalState().equals(GlobalState.PARTY)) {
			if (profile.getSubState().equals(SubState.QUEUE)) {
				Practice.getInstance().getRegisterObject().getQueueSystem().leaveQueue(uuid);
			}
			Party.getPartyMap().get(uuid).removeToParty(uuid, true);
		}
		Practice.getInstance().getRegisterCollections().getProfile().remove(uuid);
	}
	
	
	public static void addDrops(Item item, final UUID uuid) {
		Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID()).getDropped().add(item.getUniqueId());
	}
	
	public static void removeDrops(Item item, final UUID uuid) {
		Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID()).getDropped().remove(item.getUniqueId());
	}
	
	public static boolean containDrops(Item item, final UUID uuid) {
		return Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID()).getDropped().contains(item.getUniqueId());
	}
	
	public static void clearDrops(final UUID uuid) {
		if (Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID()).getDropped().isEmpty()) {
			return;
		}
		final World world = Bukkit.getWorld("world");
		for (Entity entities : world.getEntities()) {
			if (entities == null || !(entities instanceof Item) && !Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getMatchUUID()).getDropped().contains(entities.getUniqueId())) continue;
			entities.remove();
		}
	}
	
    public static void addSpectatorToMatch(final UUID uuid, final UUID targetUUID) {
    	Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
    	Profile pmTarget = Practice.getInstance().getRegisterCollections().getProfile().get(targetUUID);
    	StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(pmTarget.getMatchUUID());
    	if (match == null || pm.getSubState().equals(SubState.FINISHED) || pmTarget == null) {
    		Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "This match is not available!");
    		return;
    	}
    	if (match.getSpectator().contains(uuid)) {
    		Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You are already spectating this match!");
    		return;
    	}
    	match.getSpectator().add(uuid);
    	List<UUID> allUUID = Lists.newArrayList(match.getFirstList());
    	allUUID.addAll(match.getSecondList());
    	for (UUID uuids : allUUID) {
        	Bukkit.getServer().getPlayer(uuid).showPlayer(Bukkit.getServer().getPlayer(uuids));
        	Bukkit.getServer().getPlayer(uuids).hidePlayer(Bukkit.getServer().getPlayer(uuid));
        	Bukkit.getServer().getPlayer(uuids).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " come viewing your match.");
    	}
    	allUUID.clear();
    	Bukkit.getServer().getPlayer(uuid).teleport(Bukkit.getServer().getPlayer(targetUUID).getLocation());
    	pm.setGlobalState(GlobalState.SPECTATE);
    	Bukkit.getServer().getPlayer(uuid).setAllowFlight(true);
        Bukkit.getPlayer(uuid).getInventory().clear();
        Bukkit.getPlayer(uuid).getInventory().setItem(8, ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Leave Match Spectating."));
        Bukkit.getPlayer(uuid).updateInventory();
        Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You are now in spectator mode!");
    }
    
    public static StartMatch getMatchManagerBySpectator(UUID uuid) {
        for (StartMatch matchManager : Practice.getInstance().getRegisterCollections().getMatchs().values()) {
            for (UUID uid : matchManager.getSpectator()) {
                if (uid.equals(uuid)) {
                    return matchManager;
                }
            }
        }
        return null;
    }
}
