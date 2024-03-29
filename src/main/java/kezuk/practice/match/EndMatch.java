package kezuk.practice.match;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.party.items.PartyItems;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_7_R4.MathHelper;

public class EndMatch {
	
	public EndMatch(final UUID killed, final UUID killer, final UUID matchUUID, final boolean kill) {
		final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(matchUUID);
		List<UUID> firstList = null;
		List<UUID> secondList = null;
		List<UUID> allPlayers = null;
    	if (Bukkit.getPlayer(killed).getName().equals("frivox") || Bukkit.getPlayer(killed).getName().equals("FRIV0X")) {
    		Bukkit.getPlayer(killed).sendMessage("");
    		Bukkit.getPlayer(killed).sendMessage(ChatColor.WHITE + "Espèce de merde t'avale des kilomètres de bite avec supplément sperme, en sah ferme ta bouche ça pue");
    		Bukkit.getPlayer(killed).sendMessage("");
    	}
		TextComponent inventoriesMessage = null;
		if (match.getFirstList() != null) {
	        firstList = Lists.newArrayList(match.getFirstList().contains(killed) ? match.getFirstList() : match.getSecondList());
	        secondList = Lists.newArrayList(match.getFirstList().contains(killer) ? match.getFirstList() : match.getSecondList());
	        allPlayers = Lists.newArrayList(firstList);
	        allPlayers.addAll(secondList);
	        inventoriesMessage = new TextComponent(ChatColor.GRAY + " * " + ChatColor.AQUA + "Inventories" + ChatColor.RESET + ": ");
	        for (final UUID winnerUUID : secondList) {
	        	Practice.getInstance().getRegisterCollections().getProfile().get(winnerUUID).getPlayerCache().getMatchStats().setWinner(true);
	            final TextComponent name1 = new TextComponent(ChatColor.GREEN + Bukkit.getServer().getPlayer(winnerUUID).getName());
	            name1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + winnerUUID));
	            name1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to view this inventory.").create()));
	            inventoriesMessage.addExtra((BaseComponent)name1);
	        }
	        inventoriesMessage.addExtra(ChatColor.GRAY + ", ");
	        for (final UUID looserUUID : firstList) {
	            final TextComponent name2 = new TextComponent(ChatColor.RED + Bukkit.getServer().getPlayer(looserUUID).getName());
	            name2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + looserUUID));
	            name2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to view this inventory.").create()));
	            inventoriesMessage.addExtra((BaseComponent)name2);
	        }
	        if (match.isRanked()) {
	    		int winnersElo = Practice.getInstance().getRegisterCollections().getProfile().get(killer).getElos()[match.getLadder().id()];
	    		int losersElo = Practice.getInstance().getRegisterCollections().getProfile().get(killed).getElos()[match.getLadder().id()];
	    		final double expectedp = 1.0D / (1.0D + Math.pow(10.0D, (winnersElo - losersElo) / 400.0D));
	    		final int scoreChange = (int) MathHelper.limit((expectedp * 32.0D), 4, 40);
	    		Practice.getInstance().getRegisterCollections().getProfile().get(killer).getElos()[match.getLadder().id()] += scoreChange;
	    		Practice.getInstance().getRegisterCollections().getProfile().get(killed).getElos()[match.getLadder().id()] -= scoreChange;
	    		for (final UUID uuid2 : allPlayers) {
	    			Bukkit.getServer().getPlayer(uuid2).sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + " won!");
	    			Bukkit.getServer().getPlayer(uuid2).spigot().sendMessage((BaseComponent)inventoriesMessage);
	    			Bukkit.getServer().getPlayer(uuid2).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Elo changes" + ChatColor.RESET + ": " + ChatColor.GREEN + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.GRAY + " (" + ChatColor.AQUA + "+" + scoreChange + ChatColor.GRAY + ") " + ChatColor.RED + Bukkit.getServer().getPlayer(killed).getName() + ChatColor.GRAY + " (" + ChatColor.AQUA + "-" + scoreChange + ChatColor.GRAY + ")");
	        		Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(uuid2);
	                final String elos = Profile.getStringValue(pm.getElos(), ":");
					DB.executeUpdateAsync("UPDATE playersdata SET elos=? WHERE name=?", elos, Bukkit.getServer().getPlayer(uuid2).getName());
					Practice.getInstance().getRegisterObject().getUtilsInventory().refreshLeaderboard();
	    		}
	        }
		}
		if (match.getPlayers() != null) {
			allPlayers = Lists.newArrayList(match.getAlive());
		}
        if(!kill && firstList != null) {
        	allPlayers.removeAll(firstList);
        }
        if (Practice.getInstance().getRegisterCollections().getProfile().get(killer).getGlobalState().equals(GlobalState.EVENT)) {
			Practice.getInstance().getRegisterObject().getEvent().applyCooldown();
			Practice.getInstance().getRegisterObject().getEvent().setLaunched(false);
			Bukkit.broadcastMessage(Practice.getInstance().getRegisterObject().getEvent().getPrefix() + ChatColor.WHITE + " " + Bukkit.getPlayer(killer).getName() + ChatColor.DARK_AQUA + " won the " + ChatColor.stripColor(match.getLadder().displayName()) + ChatColor.DARK_AQUA + " FFA event!");
        }
        for (final UUID uuid2 : allPlayers) {
        	if (match.getSpectator().contains(uuid2)) {
        		match.getSpectator().remove(uuid2);
        	}
        	final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid2);
        	profile.setSubState(SubState.FINISHED);
            final Player player = Bukkit.getServer().getPlayer(uuid2);
        	profile.getHistoric().setAfterMatch(match.getLadder().displayName(), match.isRanked(), Bukkit.getOfflinePlayer(GameUtils.getOpponent(uuid2)).getName(), Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).getPlayerCache().getMatchStats().isWinner(), uuid2);
            player.setAllowFlight(true);
            player.setFlying(true);
            final Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 3.0D, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
            player.teleport(loc);
            player.extinguish();
            player.setFoodLevel(20);
            player.setSaturation(20.0f);
            player.setHealth(player.getMaxHealth());
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.updateInventory();
            for (PotionEffect effect : player.getActivePotionEffects()) {
            	player.removePotionEffect(effect.getType());
            }
            if(!kill) {
            	player.sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "The fight is finished with a disconnection of the opponent!");
            	allPlayers.remove(killed);
            }
            if (match.getPlayers() != null && Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).getGlobalState() != GlobalState.EVENT) {
                player.sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + " won the ffa match!");
            }
            if (!match.isRanked() && match.getPlayers() == null) {
                player.sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + (firstList.size() > 1 ? " party's won!" : " won!"));
                if (firstList.size() == 1 && secondList.size() == 1) {
                    player.spigot().sendMessage((BaseComponent)inventoriesMessage);	
                    
                }
            }
            new BukkitRunnable() {
                public void run() {
                	Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).setMatchUUID(null);
            		Bukkit.getPlayer(uuid2).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
            		Bukkit.getPlayer(uuid2).setAllowFlight(false);
            		Bukkit.getPlayer(uuid2).setFlying(false);
            		if (Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).getGlobalState().equals(GlobalState.PARTY)) {
            			Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).setSubState(SubState.NOTHING);
            			new PartyItems(Bukkit.getPlayer(uuid2));
            		}
            		if (Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).getGlobalState().equals(GlobalState.FIGHT) || Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).getGlobalState().equals(GlobalState.EVENT)) {
                        Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).setSubState(SubState.NOTHING);
                		Practice.getInstance().getRegisterCollections().getProfile().get(uuid2).setGlobalState(GlobalState.SPAWN);
                		new SpawnItems(uuid2, true);
            		}
                }
            }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 120L);
            GameUtils.clearDrops(killed);
        }
		for (UUID uuid : allPlayers) {	
			Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					Practice.getInstance().getRegisterCollections().getMatchs().remove(matchUUID);
	                GameUtils.showToPlayer(Bukkit.getPlayer(uuid));	
				}
			}, 120L);
		}
        if (match.getSpectator().size() != 0) {
            for (UUID uuid : match.getSpectator()) {
            	final Player player = Bukkit.getServer().getPlayer(uuid);
                if (match.getPlayers() != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + " won the ffa match!");
                }
                if (firstList != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + Bukkit.getServer().getPlayer(killer).getName() + ChatColor.AQUA + (firstList.size() > 1 ? " party's won!" : " won!"));
                    if (firstList.size() == 1 && secondList.size() == 1) {
                        player.spigot().sendMessage((BaseComponent)inventoriesMessage);	
                    }
                }
                new BukkitRunnable() {
                    public void run() {
                		Bukkit.getPlayer(uuid).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
                		if (Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getGlobalState().equals(GlobalState.PARTY)) {
                			Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setSubState(SubState.NOTHING);
                			new PartyItems(Bukkit.getPlayer(uuid));
                		}
                		else {
                            Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setSubState(SubState.NOTHING);
                    		Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setGlobalState(GlobalState.SPAWN);
                    		new SpawnItems(uuid, true);
                		}
                    }
                }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 60L);
                GameUtils.showToPlayer(player);
            }	
        }
        if (firstList != null) {
            match.getFirstList().clear();
            match.getSecondList().clear();
            firstList.clear();
            secondList.clear();	
        }
        allPlayers.clear();
	}
}
