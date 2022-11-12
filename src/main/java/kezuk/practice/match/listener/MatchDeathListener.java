package kezuk.practice.match.listener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;
import kezuk.practice.event.tournament.Tournament;
import kezuk.practice.event.tournament.TournamentMatch;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;

public class MatchDeathListener implements Listener {
	
	public static HandlerList handlerList = new HandlerList();
	
    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        event.setDeathMessage((String)null);
        event.getDrops().clear();
        final Player player = event.getEntity();
        final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        if (profile.getSubState().equals(SubState.PLAYING)) {
            final Location deathLocation = player.getLocation();
            new BukkitRunnable() {
                public void run() {
                    try {
                        final Object nmsPlayer = event.getEntity().getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(event.getEntity(), new Object[0]);
                        final Object con = nmsPlayer.getClass().getDeclaredField("playerConnection").get(nmsPlayer);
                        final Class<?> EntityPlayer = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EntityPlayer");
                        final Field minecraftServer = con.getClass().getDeclaredField("minecraftServer");
                        minecraftServer.setAccessible(true);
                        final Object mcserver = minecraftServer.get(con);
                        final Object playerlist = mcserver.getClass().getDeclaredMethod("getPlayerList", (Class<?>[])new Class[0]).invoke(mcserver, new Object[0]);
                        final Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", EntityPlayer, Integer.TYPE, Boolean.TYPE);
                        moveToWorld.invoke(playerlist, nmsPlayer, 0, false);
                        player.teleport(deathLocation);
                        player.setAllowFlight(true);
                    }
                    catch (Exception ex) {
                        player.spigot().respawn();
                        player.teleport(deathLocation);
                        player.setAllowFlight(true);
                        ex.printStackTrace();
                    }
                }
            }.runTaskLater((Plugin)Practice.getInstance(), 2L);
            final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID());
            if (!(player.getKiller() instanceof Player)) {
            	if (match != null && match.isTournament()) {
                    for (final Tournament tournament : Tournament.getTournaments()) {
                        if (tournament != null) {
                            final Iterator<TournamentMatch> iterator = tournament.getCurrentMatches().iterator();
                            while (iterator.hasNext()) {
                                final TournamentMatch tmatch = iterator.next();
                                if (tmatch.getFirstTeam().getPlayers().equals(match.getFirstList()) && tmatch.getSecondTeam().getPlayers().equals(match.getSecondList())) {
                                    final String winningTeamOne = ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + "Tournament" + ChatColor.GRAY + ") " + ChatColor.WHITE + Bukkit.getOfflinePlayer(GameUtils.getOpponent(player.getUniqueId())).getName() + ChatColor.AQUA + " have eliminated " + ChatColor.WHITE + Bukkit.getOfflinePlayer(player.getUniqueId()).getName();
                                    Bukkit.broadcastMessage(winningTeamOne);
                                    tmatch.setWinndingId(match.getFirstList().contains(player.getUniqueId()) ? 2 : 1);
                                    profile.setSubState(SubState.FINISHED);
                                    tournament.getTeams().remove(tmatch.getFirstTeam().getPlayers().contains(player.getUniqueId()) ? tmatch.getSecondTeam() : tmatch.getFirstTeam());
                                    tournament.getCurrentQueue().remove(match.getFirstList().contains(player.getUniqueId()) ? match.getSecondList() : match.getFirstList());
                                    iterator.remove();
                                    player.sendMessage(ChatColor.AQUA + "You are now eliminated from the tournament, maybe one more time. Who knows.");
                                }
                            }
                            for (final TournamentMatch tournmatch : tournament.getCurrentMatches()) {
                            	tournmatch.getMatchPlayers().remove(player.getUniqueId());
                            	tournmatch.getMatchPlayers().remove(GameUtils.getOpponent(player.getUniqueId()));
                            	tournament.generateRoundMatches();
                            }
                        }
                    }
            	}
            	GameUtils.addKill(player.getUniqueId(), (profile.getPlayerCache().getMatchStats().getLastAttacker() != null ? profile.getPlayerCache().getMatchStats().getLastAttacker() : null));
            	return;
            }
        	if (match != null && match.isTournament()) {
                for (final Tournament tournament : Tournament.getTournaments()) {
                    if (tournament != null) {
                        final Iterator<TournamentMatch> iterator = tournament.getCurrentMatches().iterator();
                        while (iterator.hasNext()) {
                            final TournamentMatch tmatch = iterator.next();
                            if (tmatch.getFirstTeam().getPlayers().equals(match.getFirstList()) && tmatch.getSecondTeam().getPlayers().equals(match.getSecondList())) {
                                final String winningTeamOne = ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + "Tournament" + ChatColor.GRAY + ") " + ChatColor.WHITE + Bukkit.getOfflinePlayer(player.getKiller().getUniqueId()).getName() + ChatColor.AQUA + " have eliminated " + ChatColor.WHITE + Bukkit.getOfflinePlayer(player.getUniqueId()).getName();
                                Bukkit.broadcastMessage(winningTeamOne);
                                tmatch.setWinndingId(match.getFirstList().contains(player.getUniqueId()) ? 2 : 1);
                                profile.setSubState(SubState.FINISHED);
                                tournament.getTeams().remove(tmatch.getFirstTeam().getPlayers().contains(player.getUniqueId()) ? tmatch.getSecondTeam() : tmatch.getSecondTeam());
                                tournament.getCurrentQueue().remove(match.getFirstList().contains(player.getUniqueId()) ? match.getSecondList() : match.getFirstList());
                                iterator.remove();
                                player.sendMessage(ChatColor.AQUA + "You are now eliminated from the tournament, maybe one more time. Who knows.");
                            }
                        }
                        for (final TournamentMatch tournmatch : tournament.getCurrentMatches()) {
                        	tournmatch.getMatchPlayers().remove(player.getUniqueId());
                        	tournmatch.getMatchPlayers().remove(GameUtils.getOpponent(player.getUniqueId()));
                        	tournament.generateRoundMatches();
                        }
                    }
                }
        	}
            GameUtils.addKill(player.getUniqueId(), player.getKiller().getUniqueId());
        }
    }
    
    public static HandlerList getHandlerList() {
		return handlerList;
	}

}
