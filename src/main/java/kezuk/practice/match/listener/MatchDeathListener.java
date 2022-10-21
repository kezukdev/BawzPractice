package kezuk.practice.match.listener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.MatchUtils;

public class MatchDeathListener implements Listener {
	
	public static HandlerList handlerList = new HandlerList();
	
    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        event.setDeathMessage((String)null);
        event.getDrops().clear();
        final Player player = event.getEntity();
        final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        if (profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
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
            if (!(player.getKiller() instanceof Player)) {
            	MatchUtils.addKill(player.getUniqueId(), (profile.getMatchStats().getLastAttacker() != null ? profile.getMatchStats().getLastAttacker() : null));
            	return;
            }
            MatchUtils.addKill(player.getUniqueId(), player.getKiller().getUniqueId());
        }
    }
    
    public static HandlerList getHandlerList() {
		return handlerList;
	}

}
