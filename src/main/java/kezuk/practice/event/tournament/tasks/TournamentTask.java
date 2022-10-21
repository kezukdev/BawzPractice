package kezuk.practice.event.tournament.tasks;

import org.bukkit.scheduler.*;

import kezuk.practice.event.tournament.Tournament;

public class TournamentTask extends BukkitRunnable
{
    private Tournament tournament;
    
    public TournamentTask(final Tournament tournament) {
        this.tournament = tournament;
    }
    
    public void run() {
        this.tournament.generateRoundMatches();
    }
}
