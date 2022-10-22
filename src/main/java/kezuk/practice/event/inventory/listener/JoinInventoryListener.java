package kezuk.practice.event.inventory.listener;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventSubType;
import kezuk.practice.event.tournament.Tournament;
import kezuk.practice.event.tournament.TournamentTeam;
import kezuk.practice.event.tournament.items.TournamentItems;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;

public class JoinInventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryInteractWithJoin(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (event.getCurrentItem() == null) return;
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getJoinInventory().getJoinInventory().getName())) {
				if (event.getCurrentItem().getType().equals(Material.WATER_BUCKET)) {
					if (Practice.getInstance().getRegisterObject().getEvent().isLaunched() && Practice.getInstance().getRegisterObject().getEvent().getEventType().getSubType().equals(EventSubType.WAITTING)) {
						event.getWhoClicked().closeInventory();
						Practice.getInstance().getRegisterObject().getEvent().addMemberToEvent(event.getWhoClicked().getUniqueId());
					}
					else {
						event.getWhoClicked().closeInventory();
						final Player player = (Player) event.getWhoClicked();
						player.sendMessage(ChatColor.AQUA + "Sorry but no events are currently available! Either it is already in progress or nobody has created one!");
					}
				}	
				if (event.getCurrentItem().getType().equals(Material.LAVA_BUCKET)) {
					final Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
					player.closeInventory();
					if (Practice.getInstance().getRegisterCollections().getRunningTournaments().size() == 1) {
						Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).setGlobalState(GlobalState.TOURNAMENT);
						new TournamentItems(player.getUniqueId());
		                final TournamentTeam tournamentTeam = new TournamentTeam();
		                tournamentTeam.setPlayers(Collections.singletonList(player.getUniqueId()));
		                Tournament tournament = Tournament.getTournaments().get(0);
		                tournament.getTeams().add(tournamentTeam);
		                tournament.sendMessage(ChatColor.AQUA + player.getName() + " has joined the tournament. (" + tournament.getTotalPlayersInTournament() + "/" + tournament.getPlayersLimit() + ")");
					}
					else {
						player.sendMessage(ChatColor.AQUA + "Sorry but no tournaments are currently available! Either it is already in progress or nobody has created one!");
					}
				}
				event.setCancelled(true);
				return;
			}
		}
	}

}
