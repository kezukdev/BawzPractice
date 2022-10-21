package kezuk.practice.registering;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;

import kezuk.practice.Practice;
import kezuk.practice.arena.command.ArenaCommand;
import kezuk.practice.core.rank.command.RankCommand;
import kezuk.practice.ladders.inventory.listener.LadderInventoryListener;
import kezuk.practice.match.inventory.command.InventoryCommand;
import kezuk.practice.match.spectate.command.SpectateCommand;
import kezuk.practice.party.command.PartyCommand;
import kezuk.practice.player.listener.PlayerListener;
import kezuk.practice.queue.listener.QueueListener;
import kezuk.practice.request.command.AcceptCommand;
import kezuk.practice.request.command.DenyCommand;
import kezuk.practice.request.command.DuelCommand;

public class RegisterCommon {
	
	private Location spawnLocation;
	private Location firstLocation;
	private Location secondLocation;
	private Location spectatorLocation;
	
	public RegisterCommon() {
		this.spawnLocation = new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f);
		this.firstLocation = new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f);
		this.secondLocation = new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f);
		this.spectatorLocation = new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f);
		this.registerCommand();
		this.registerListener();
	}

	private void registerListener() {
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new QueueListener(), Practice.getInstance());
		pm.registerEvents(new LadderInventoryListener(), Practice.getInstance());
		pm.registerEvents(new PlayerListener(), Practice.getInstance());
	}

	private void registerCommand() {
		Practice.getInstance().getCommand("arena").setExecutor(new ArenaCommand());
		Practice.getInstance().getCommand("party").setExecutor(new PartyCommand());
		Practice.getInstance().getCommand("duel").setExecutor(new DuelCommand());
		Practice.getInstance().getCommand("accept").setExecutor(new AcceptCommand());
		Practice.getInstance().getCommand("deny").setExecutor(new DenyCommand());
		Practice.getInstance().getCommand("spectate").setExecutor(new SpectateCommand());
		Practice.getInstance().getCommand("inventory").setExecutor(new InventoryCommand());
		Practice.getInstance().getCommand("rank").setExecutor(new RankCommand());
	}
	
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	public Location getFirstLocation() {
		return firstLocation;
	}
	
	public Location getSecondLocation() {
		return secondLocation;
	}
	
	public Location getSpectatorLocation() {
		return spectatorLocation;
	}
}
