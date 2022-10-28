package kezuk.practice.match.listener;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;

public class MatchMoveEvent implements Listener {

	@EventHandler
	public void onSumoMove(final PlayerMoveEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.isFrozen()) {
    		if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
    			event.getPlayer().sendMessage(ChatColor.GRAY + " ⚠ " + ChatColor.RED + "You are currently frozen, join our discord for a possible verification." + ChatColor.WHITE + " discord.gg/bawz");
    			event.setTo(event.getFrom().setDirection(event.getTo().getDirection()));
    			return;
    		}
		}
		final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID());
		if (match != null && match.getLadder().name().equalsIgnoreCase("sumo") || profile.getGlobalState().equals(GlobalState.EVENT) && Practice.getInstance().getRegisterObject().getEvent().getEventType().equals(EventType.SUMO)) {
			if (profile.getSubState().equals(SubState.PLAYING) || profile.getSubState().equals(SubState.STARTING)) {
				if (profile.getSubState().equals(SubState.STARTING)) {
		    		if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
		    			event.setTo(event.getFrom().setDirection(event.getTo().getDirection())); 
		    		}
				}
				if (profile.getSubState().equals(SubState.PLAYING)) {
					if (event.getPlayer().isInFluid() || event.getPlayer().isInWater() || event.getPlayer().isInLava()) {
						if (profile.getGlobalState().equals(GlobalState.EVENT)) {
							final UUID winner = Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().getFirstUUID() == event.getPlayer().getUniqueId() ? Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().getSecondUUID() : Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().getFirstUUID();
							Practice.getInstance().getRegisterObject().getEvent().getSumoEvent().newRound(winner, event.getPlayer().getUniqueId());
							return;
						}
						GameUtils.addKill(event.getPlayer().getUniqueId(), profile.getMatchStats().getLastAttacker());
					}
				}
			}	
		}
	}
}
