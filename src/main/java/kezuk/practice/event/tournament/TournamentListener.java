package kezuk.practice.event.tournament;

import org.bukkit.event.player.*;

import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class TournamentListener implements Listener
{
    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (Tournament.getTournaments().size() == 0) {
            return;
        }
        for (final Tournament tournament : Tournament.getTournaments()) {
            if (tournament.isInTournament(player)) {
                final TournamentTeam tournamentTeam = tournament.getTournamentTeam(player);
                if (tournamentTeam == null) {
                    continue;
                }
                final String reason = (tournamentTeam.getPlayers().size() > 1) ? "(Tournament) Someone in your party left the server" : "(Tournament) You have left the server";
                tournamentTeam.sendMessage(ChatColor.RED + reason);
                final TournamentMatch match = tournament.getTournamentMatch(player);
                if (match == null) {
                    continue;
                }
                final List<UUID> opponentList = (match.getFirstTeam().getPlayers().contains(player.getUniqueId()) ? match.getSecondTeam().getPlayers() : match.getFirstTeam().getPlayers());
                match.getMatch().endMatch(player.getUniqueId(), opponentList.get(0), match.getMatch().getMatchUUID(), false);
            }
        }
    }
}
