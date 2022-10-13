package kezuk.bawz.match;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.*;

import kezuk.bawz.*;
import kezuk.bawz.match.manager.FfaMatchManager;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.player.*;
import org.bukkit.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.UUID;

public class MatchListener implements Listener {
    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        final Player player = (Player)event.getEntity();
        final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
    	final MatchManager match = Practice.getMatchs().get(pm.getMatchUUID());
    	final FfaMatchManager ffaMatch = Practice.getFfaMatchs().get(pm.getMatchUUID());
        if (match != null && pm.getPlayerStatus() == Status.FIGHT && match.getStatus().equals(MatchStatus.PLAYING) || match != null && pm.getPlayerStatus() == Status.PARTY && ffaMatch.getStatus().equals(MatchStatus.PLAYING) || match != null && pm.getPlayerStatus() == Status.HOST && ffaMatch.getStatus().equals(MatchStatus.STARTING)) {
        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
        		event.setDamage(0.0D);
        		return;
        	}
        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo")) {
        		event.setDamage(0.0D);
        		return;
        	}
            return;
        }
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
    	if (!(event.getDamager() instanceof Player)) return;
        final Player damaged = (Player)event.getEntity();
        final Player damager = (Player)event.getDamager();
        final MatchManager match = Practice.getMatchs().get(PlayerManager.getPlayers().get(damaged.getUniqueId()).getMatchUUID());
        final FfaMatchManager ffaMatch = Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(damaged.getUniqueId()).getMatchUUID());
        if (match != null && damaged.canSee(damager) && PlayerManager.getPlayers().get(damaged.getUniqueId()).getPlayerStatus().equals(Status.FIGHT) && match.getStatus().equals(MatchStatus.PLAYING) || ffaMatch != null && damaged.canSee(damager) && PlayerManager.getPlayers().get(damaged.getUniqueId()).getPlayerStatus().equals(Status.PARTY) && ffaMatch.getStatus().equals(MatchStatus.PLAYING) ||  ffaMatch != null && damaged.canSee(damager) && PlayerManager.getPlayers().get(damaged.getUniqueId()).getPlayerStatus().equals(Status.HOST) && ffaMatch.getStatus().equals(MatchStatus.PLAYING)) {
        	if (match.getLadder().displayName() != ChatColor.DARK_AQUA + "Combo" || ffaMatch.getLadder().displayName() != ChatColor.DARK_AQUA + "Combo") {
                if (PlayerManager.getPlayers().get(damaged.getUniqueId()).getNextHitTick() != 0L && PlayerManager.getPlayers().get(damaged.getUniqueId()).getNextHitTick() > System.currentTimeMillis()) {
                    return;
                }
                PlayerManager.getPlayers().get(damaged.getUniqueId()).updateNextHitTick();
            }
            final PlayerManager dmr = PlayerManager.getPlayers().get(damager.getUniqueId());
            dmr.getMatchStats().setHits(dmr.getMatchStats().getHits() + 1);
            if (PlayerManager.getPlayers().get(damaged.getUniqueId()).getMatchStats().getCombo() > 0) {
            	PlayerManager.getPlayers().get(damaged.getUniqueId()).getMatchStats().setCombo(0);
            }
            dmr.getMatchStats().setCombo(dmr.getMatchStats().getCombo() + 1);
            if (dmr.getMatchStats().getCombo() > dmr.getMatchStats().getLongestCombo()) {
            	dmr.getMatchStats().setLongestCombo(dmr.getMatchStats().getCombo());
            }
        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo") || ffaMatch.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo")) {
                event.setDamage(0.0D);
                return;
            }
        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing") || ffaMatch.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
                event.setDamage(0.0D);
                final float hitExp = dmr.getMatchStats().getHits() / 100.0f;
                damager.setExp(hitExp);
                damager.setLevel(dmr.getMatchStats().getCombo());
                PlayerManager.getPlayers().get(damaged.getUniqueId()).getMatchStats().setCombo(0);
                if (dmr.getMatchStats().getHits() == 100) {
                	if (match != null) {
                    	Practice.getMatchs().get(dmr.getMatchUUID()).endMatch(damaged.getUniqueId(), damager.getUniqueId(), dmr.getMatchUUID(), true);
                	}
                	if (ffaMatch != null) {
                    	Practice.getFfaMatchs().get(dmr.getMatchUUID()).addKill(damaged.getUniqueId(), damager.getUniqueId());	
                	}
                }
                return;
            }
            return;
        }
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        final PlayerManager playerManager = PlayerManager.getPlayers().get(e.getPlayer().getUniqueId());
        if (playerManager.getPlayerStatus() == Status.FIGHT) {
            if (e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD || e.getItemDrop().getItemStack().getType() == Material.IRON_AXE || e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD || e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD) {
                e.setCancelled(true);
                return;
            }
        }
        else {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onSplash(ProjectileLaunchEvent event) {
    	final Player player = (Player) event.getEntity().getShooter();
    	final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
    	if (event.getEntity() instanceof ThrownPotion) {
    		pm.getMatchStats().setThrownPotions(pm.getMatchStats().getThrownPotions() + 1);
    	}
    }
    
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerManager playerData = (PlayerManager)PlayerManager.getPlayers().get(player.getUniqueId());
		Location from = event.getFrom();
	    Location to = event.getTo();
	    MatchManager match = Practice.getMatchs().get(playerData.getMatchUUID());
	    if (match != null && playerData.getPlayerStatus() == Status.FIGHT && match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo")) {
	    	if (match != null && match.getStatus().equals(MatchStatus.STARTING)) {
	    		if (from.getX() != to.getX() || from.getZ() != to.getZ())
	    			event.setTo(from.setDirection(to.getDirection())); 
	    		}
	    		else if (match.getStatus() == MatchStatus.PLAYING) {
	    			Location loc = player.getLocation();
	    			loc.setY(loc.getY() - 2.0D);
	    			if (player.getWorld().getBlockAt(loc).getType() == Material.STATIONARY_WATER || player.getWorld().getBlockAt(loc).getType() == Material.WATER || player.getWorld().getBlockAt(loc).getType() == Material.LAVA || player.getWorld().getBlockAt(loc).getType() == Material.STATIONARY_LAVA)
	    				match.endMatch(player.getUniqueId(),match.getOpponent(player.getUniqueId()),playerData.getMatchUUID(), true); 
	    			} 
	    	} 
	  }
	 
	@EventHandler
	public void onPlayerInteractSoup(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final Player player = event.getPlayer();
			if (!player.isDead() && player.getItemInHand().getType() == Material.MUSHROOM_SOUP && player.getHealth() < player.getMaxHealth()) {
				final double newHealth = Math.min(player.getHealth() + 7.0D, player.getMaxHealth());
				player.setHealth(newHealth);
				player.updateInventory();
				player.getItemInHand().setType(Material.BOWL);
				player.updateInventory();
			}
		}
	}
	
	@EventHandler
	public void onFailedPotion(PotionSplashEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			final Player shooter = (Player) event.getEntity().getShooter();
			final PlayerManager sm = PlayerManager.getPlayers().get(shooter.getUniqueId());	
			if ((!event.getAffectedEntities().contains(shooter))) {
				final MatchStats stats = sm.getMatchStats();
				final int cacheFailedPotions = stats.getMissedPotions() + 1;
				stats.setMissedPotions(cacheFailedPotions);
			}
		}
	}
	
	@EventHandler
	public void onReceiveDroppedItems(PlayerPickupItemEvent event) {
		final Player player = event.getPlayer();
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.getPlayerStatus().equals(Status.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onSpawnInWorldEntity(EntitySpawnEvent event) {
		if (event.getEntity() instanceof Item) {
			final Item itemDropped = (Item) event.getEntity();
			
			if (itemDropped.getItemStack().getType() == Material.GLASS_BOTTLE || itemDropped.getItemStack().getType() == Material.BOWL) return;
			if (itemDropped.getOwner() != null && itemDropped.getOwner() instanceof Player) {
				final UUID playerUUID = itemDropped.getOwner().getUniqueId();
				final MatchManager duel = Practice.getMatchs().get(PlayerManager.getPlayers().get(playerUUID).getMatchUUID());
				if (duel == null) return;
				
				duel.getDropped().add(itemDropped.getUniqueId());
			}
		}
	}
	
    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        event.setDeathMessage((String)null);
        event.getDrops().clear();
        final Player player = event.getEntity();
        if (PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.FIGHT) || PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.PARTY) || PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.HOST)) {
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
            	if (PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.FIGHT)) {
                	MatchManager match = Practice.getMatchs().get(PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID());
                	List<UUID> opponentList = match.getFirstList().contains(player.getUniqueId()) ? match.getSecondList() : match.getFirstList();
                	for (UUID uuid : opponentList) {
                        match.endMatch(player.getUniqueId(), uuid, PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID(), true);
                	}	
            	}
            	if (PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.PARTY) || PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.HOST) && Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID()) != null) {
            		Practice.getFfaMatchs().get(PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID()).addKill(player.getUniqueId(), null);
            	}
            	return;
            }
            if (PlayerManager.getPlayers().get(player.getUniqueId()).getPlayerStatus().equals(Status.FIGHT)) {
                Practice.getMatchs().get(PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID()).endMatch(player.getUniqueId(), player.getKiller().getUniqueId(), PlayerManager.getPlayers().get(player.getUniqueId()).getMatchUUID(), true);	
            }
        }
    }
}
