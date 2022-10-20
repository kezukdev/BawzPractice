package kezuk.practice.match.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import kezuk.practice.Practice;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.MatchUtils;

public class MatchEntityListener implements Listener {
	
	@EventHandler
	public void onReceiveDamageGlobal(final EntityDamageEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID());
			if (match.getLadder().name().equals("sumo") || match.getLadder().name().equals("boxing")) {
				event.setDamage(0.0);
			}
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void entityVsEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) return;
		final Profile dmgd = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		final Profile dmr = Practice.getInstance().getRegisterCollections().getProfile().get(event.getDamager().getUniqueId());
		if (dmgd.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(dmr.getMatchUUID());
			if (match.getFirstList().contains(event.getDamager().getUniqueId()) && match.getFirstList().contains(event.getEntity().getUniqueId()) || match.getSecondList().contains(event.getDamager().getUniqueId()) && match.getSecondList().contains(event.getEntity().getUniqueId())) {
				event.setCancelled(true);
				return;
			}
        	if (match.getLadder().displayName() != ChatColor.DARK_AQUA + "Combo") {
                if (dmgd.getMatchStats().getNextHitTick() != 0L && dmgd.getMatchStats().getNextHitTick() > System.currentTimeMillis()) {
                    return;
                }
                dmgd.getMatchStats().updateNextHitTick();
            }
        	dmgd.getMatchStats().setLastAttacker(event.getDamager().getUniqueId());
            dmr.getMatchStats().setHits(dmr.getMatchStats().getHits() + 1);
            if (dmgd.getMatchStats().getCombo() > 0) {
            	dmgd.getMatchStats().setCombo(0);
            }
            dmr.getMatchStats().setCombo(dmr.getMatchStats().getCombo() + 1);
            if (dmr.getMatchStats().getCombo() > dmr.getMatchStats().getLongestCombo()) {
            	dmr.getMatchStats().setLongestCombo(dmr.getMatchStats().getCombo());
            }
        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Sumo")) {
                event.setDamage(0.0D);
                return;
            }
        	if (match.getLadder().displayName().equals(ChatColor.DARK_AQUA + "Boxing")) {
                event.setDamage(0.0D);
                final float hitExp = dmr.getMatchStats().getHits() / 100.0f;
                final Player damager = (Player) event.getDamager();
                damager.setExp(hitExp);
                damager.setLevel(dmr.getMatchStats().getCombo());
                dmgd.getMatchStats().setCombo(0);
                if (dmr.getMatchStats().getHits() == 100) {
                	if (match.getFirstList().size() == 1) {
                    	match.endMatch(event.getEntity().getUniqueId(), damager.getUniqueId(), dmr.getMatchUUID(), true);	
                    	return;
                	}
                	MatchUtils.addKill(event.getEntity().getUniqueId(), damager.getUniqueId());
                }
                return;
            }
            return;
		}
		event.setCancelled(true);
	}

}
