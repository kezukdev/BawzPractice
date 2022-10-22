package kezuk.practice.event.tournament.items.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import kezuk.practice.Practice;
import kezuk.practice.event.tournament.Tournament;
import kezuk.practice.event.tournament.TournamentTeam;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import net.md_5.bungee.api.ChatColor;

public class TournamentInteractItemListener implements Listener {
	
	@EventHandler
	public void onClickWithLeave(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.TOURNAMENT)) {
			if (!event.hasItem()) return;
			if (profile.getGlobalState().getSubState().equals(SubState.NOTHING)) {
				if (event.getItem().getType().equals(Material.BLAZE_POWDER) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					new SpawnItems(event.getPlayer().getUniqueId());
					profile.setGlobalState(GlobalState.SPAWN);
		            final Tournament tournament = Tournament.getTournaments().get(0);
		            final TournamentTeam tournamentTeam = tournament.getTournamentTeam(event.getPlayer());
		            tournament.getCurrentQueue().remove(tournamentTeam);
		            tournament.getTeams().remove(tournamentTeam);
	                tournament.sendMessage(ChatColor.AQUA + event.getPlayer().getName() + " has left the tournament. (" + tournament.getTotalPlayersInTournament() + "/" + tournament.getPlayersLimit() + ")");
				}
				event.setCancelled(true);
			}
		}
	}

}
