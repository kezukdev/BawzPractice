package kezuk.practice.match.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerPickupItemEvent;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;

public class MatchEntityListener implements Listener {
	
	public static HandlerList handlerList = new HandlerList();
	
	@EventHandler
	public void onReceiveDamageGlobal(final EntityDamageEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		if (profile.getSubState().equals(SubState.PLAYING)) {
			if (profile.getPlayerCache().isFrozen()) {
				event.setCancelled(true);
				return;
			}
			if (profile.getGlobalState().equals(GlobalState.MOD)) {
				event.setDamage(0.0);
				return;
			}
			final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID());
			if (match != null && match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo") || match != null && match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing") || match != null && match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Comboxing")) {
				event.setDamage(0.0);
			}
			if (profile.getGlobalState().equals(GlobalState.EVENT)) {
				if (Practice.getInstance().getRegisterObject().getEvent().getMembers() != null && Practice.getInstance().getRegisterObject().getEvent().getMembers().contains(event.getEntity().getUniqueId()) && Practice.getInstance().getRegisterObject().getEvent().getEventType().equals(EventType.SUMO)) {
					event.setDamage(0.0);
					return;
				}
				if (Practice.getInstance().getRegisterObject().getEvent().getMembers() != null && Practice.getInstance().getRegisterObject().getEvent().getMembers().contains(event.getEntity().getUniqueId()) && Practice.getInstance().getRegisterObject().getEvent().getEventType().equals(EventType.OITC)) {
					if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) event.setCancelled(true);
					return;
				}	
			}
			return;
		}
		if (event.getCause().equals(DamageCause.VOID)) {
			event.getEntity().teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void entityVsEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) return;
		final Profile dmgd = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		final Profile dmr = Practice.getInstance().getRegisterCollections().getProfile().get(event.getDamager().getUniqueId());
		if (dmgd.getSubState().equals(SubState.PLAYING) || dmr.getGlobalState().equals(GlobalState.MOD)) {
			if (dmgd.getGlobalState().equals(GlobalState.EVENT) && (Practice.getInstance().getRegisterObject().getEvent().getMembers().contains(event.getEntity().getUniqueId()) && Practice.getInstance().getRegisterObject().getEvent().getMembers().contains(event.getDamager().getUniqueId()))) {
				if (Practice.getInstance().getRegisterObject().getEvent().getEventType().equals(EventType.SUMO)) {
					event.setDamage(0.0);
					return;
				}
				if (Practice.getInstance().getRegisterObject().getEvent().getEventType().equals(EventType.OITC)) {
					Player damager;
					final Player damaged = (Player) event.getEntity();
			        if (event.getDamager() instanceof Player) {
			            damager = (Player)event.getDamager();
		                if (damager.getItemInHand().getType().equals(Material.GOLD_SWORD)) {
		                	event.setDamage(6.5D);
		                }
			        }
			        else {

			            damager = (Player)((Projectile)event.getDamager()).getShooter();
			            if (event.getDamager() instanceof Arrow) {
			                final Arrow arrow2 = (Arrow)event.getDamager();
			                if (arrow2.getShooter() instanceof Player) {
			                    final Player shooter = (Player)arrow2.getShooter();
			                    if (!damaged.getName().equals(shooter.getName())) {
			                        final double health = 0.0d;
		                        	damaged.setHealth(health);
			                    }
			                }
			            }
			        }
					return;
				}	
			}
			final Player dmgr = (Player) event.getDamager();
			if (dmr.getGlobalState().equals(GlobalState.MOD) && dmgr.getItemInHand().getType().equals(Material.BONE)) {
				event.setDamage(0.0D);
				return;
			}
			final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(dmr.getMatchUUID());
			if (match != null) {
				if (match.getFirstList() != null) {
					if (match.getFirstList().contains(event.getDamager().getUniqueId()) && match.getFirstList().contains(event.getEntity().getUniqueId()) || match.getSecondList().contains(event.getDamager().getUniqueId()) && match.getSecondList().contains(event.getEntity().getUniqueId())) {
						event.setCancelled(true);
						return;
					}	
				}
				if (match.getLadder().displayName() != ChatColor.DARK_AQUA + "Comboxing") {
	                if (dmgd.getPlayerCache().getMatchStats().getNextHitTick() != 0L && dmgd.getPlayerCache().getMatchStats().getNextHitTick() > System.currentTimeMillis()) {
	                    return;
	                }
	                dmgd.getPlayerCache().getMatchStats().updateNextHitTick();	
				}
	        	dmgd.getPlayerCache().getMatchStats().setLastAttacker(event.getDamager().getUniqueId());
	            dmr.getPlayerCache().getMatchStats().setHits(dmr.getPlayerCache().getMatchStats().getHits() + 1);
	            if (dmgd.getPlayerCache().getMatchStats().getCombo() > 0) {
	            	dmgd.getPlayerCache().getMatchStats().setCombo(0);
	            }
	            dmr.getPlayerCache().getMatchStats().setCombo(dmr.getPlayerCache().getMatchStats().getCombo() + 1);
	            if (dmr.getPlayerCache().getMatchStats().getCombo() > dmr.getPlayerCache().getMatchStats().getLongestCombo()) {
	            	dmr.getPlayerCache().getMatchStats().setLongestCombo(dmr.getPlayerCache().getMatchStats().getCombo());
	            }
	        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo")) {
	                event.setDamage(0.0D);
	            }
	        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing") || match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Comboxing") ) {
	                event.setDamage(0.0D);
	                final Player damager = (Player) event.getDamager();
	                if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
	                	final float hitExp = dmr.getPlayerCache().getMatchStats().getHits() / 100.0f;
	                	damager.setExp(hitExp);
	                }
	                if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Comboxing")) {
	                	final float hitExp = dmr.getPlayerCache().getMatchStats().getHits() / 300.0f;
		                damager.setExp(hitExp);
	                }
	                damager.setLevel(dmr.getPlayerCache().getMatchStats().getCombo());
	                dmgd.getPlayerCache().getMatchStats().setCombo(0);
	                if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing") && dmr.getPlayerCache().getMatchStats().getHits() == 100 || match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Comboxing") && (dmr.getPlayerCache().getMatchStats().getCombo() == 40 || dmr.getPlayerCache().getMatchStats().getHits() == 300)) {
	                	if (match.getFirstList().size() == 1) {
	                    	match.endMatch(event.getEntity().getUniqueId(), damager.getUniqueId(), dmr.getMatchUUID(), true);	
	                    	return;
	                	}
	                	GameUtils.addKill(event.getEntity().getUniqueId(), damager.getUniqueId());
	                }
	                return;
	            }
	            return;
			}
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onReceiveDroppedItems(PlayerPickupItemEvent event) {
		final Player player = event.getPlayer();
		final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
		if (pm.getSubState().equals(SubState.PLAYING)) {
			return;
		}
		event.setCancelled(true);
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}

}
