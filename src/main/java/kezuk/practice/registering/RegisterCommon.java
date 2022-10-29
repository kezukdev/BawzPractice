package kezuk.practice.registering;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;

import kezuk.practice.Practice;
import kezuk.practice.arena.command.ArenaCommand;
import kezuk.practice.core.listener.ServerListener;
import kezuk.practice.core.rank.command.RankCommand;
import kezuk.practice.core.staff.command.BanCommand;
import kezuk.practice.core.staff.command.FreezeCommand;
import kezuk.practice.core.staff.command.ModCommand;
import kezuk.practice.core.staff.command.MuteCommand;
import kezuk.practice.core.tag.inventory.listener.TagInventoryListener;
import kezuk.practice.event.command.JoinCommand;
import kezuk.practice.event.host.items.listener.HostInteractItemListener;
import kezuk.practice.event.inventory.listener.JoinInventoryListener;
import kezuk.practice.event.tournament.TournamentListener;
import kezuk.practice.event.tournament.items.listener.TournamentInteractItemListener;
import kezuk.practice.ladders.inventory.listener.LadderInventoryListener;
import kezuk.practice.match.inventory.command.InventoryCommand;
import kezuk.practice.match.inventory.listener.InventoryListener;
import kezuk.practice.match.listener.MatchDeathListener;
import kezuk.practice.match.listener.MatchEntityListener;
import kezuk.practice.match.listener.MatchInteractListener;
import kezuk.practice.match.listener.MatchMoveEvent;
import kezuk.practice.match.spectate.command.SpectateCommand;
import kezuk.practice.match.spectate.listener.SpectateListener;
import kezuk.practice.party.command.PartyCommand;
import kezuk.practice.party.listener.PartyListener;
import kezuk.practice.player.command.BuildCommand;
import kezuk.practice.player.listener.PlayerListener;
import kezuk.practice.player.personnal.listener.PersonnalListener;
import kezuk.practice.player.utils.listener.UtilsListener;
import kezuk.practice.queue.listener.QueueListener;
import kezuk.practice.request.command.AcceptCommand;
import kezuk.practice.request.command.DenyCommand;
import kezuk.practice.request.command.DuelCommand;

public class RegisterCommon {
	
	private Location spawnLocation;
	private Location firstLocation;
	private Location secondLocation;
	private Location oitcFirstLocation;
	private Location oitcSecondLocation;
	private Location oitcThirdLocation;
	private Location oitcFourthLocation;
	private Location oitcFifthLocation;
	private Location oitcSixthLocation;
	private Location oitcSeventhLocation;
	private Location spectatorLocation;
	private List<Location> oitcLocations;
	
	public RegisterCommon() {
		this.spawnLocation = new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f);
		this.firstLocation = new Location(Bukkit.getWorld("world"), 986.668D ,60.0D, 1000.450D, -88.4f, 5.4f);
		this.secondLocation = new Location(Bukkit.getWorld("world"), 1000.317D, 60.0D, 1000.442D, 90.9f, 1.3f);
		this.spectatorLocation = new Location(Bukkit.getWorld("world"), 1010.366D, 63.0D, 1000.488D, 91.6f, 10.4f);
		this.oitcFirstLocation = new Location(Bukkit.getWorld("world"), 3501.440D, 94.0D, 3559.429D, -178.6f, 3.5f);
		this.oitcSecondLocation = new Location(Bukkit.getWorld("world"), 3562.357D, 91.0D, 3508.461D, 87.7f, 4.1f);
		this.oitcThirdLocation = new Location(Bukkit.getWorld("world"), 3460.450D, 99.0D, 3410.413D, 31.8f, 2.8f);
		this.oitcFourthLocation = new Location(Bukkit.getWorld("world"), 3416.024D, 85.0D, 3497.418D, -89.8f, -6.4f);
		this.oitcFifthLocation = new Location(Bukkit.getWorld("world"), 3531.695D, 84.0D, 3471.675D, -31.2f, 12.6f);
		this.oitcSixthLocation = new Location(Bukkit.getWorld("world"), 3500.342D, 72.0D, 3494.811D, 179.0f, -6.9f);
		this.oitcSeventhLocation = new Location(Bukkit.getWorld("world"), 3446.674D, 84.0D, 3419.450D, -26.0f, -3.4f);
		this.oitcLocations = Arrays.asList(this.oitcFirstLocation, this.oitcSecondLocation, this.oitcThirdLocation, this.oitcFourthLocation, this.oitcFifthLocation, this.oitcSixthLocation, this.oitcSeventhLocation);
		this.registerCommand();
		this.registerListener();
	}

	private void registerListener() {
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new QueueListener(), Practice.getInstance());
		pm.registerEvents(new InventoryListener(), Practice.getInstance());
		pm.registerEvents(new MatchEntityListener(), Practice.getInstance());
		pm.registerEvents(new MatchInteractListener(), Practice.getInstance());
		pm.registerEvents(new MatchMoveEvent(), Practice.getInstance());
		pm.registerEvents(new MatchDeathListener(), Practice.getInstance());
		pm.registerEvents(new PartyListener(), Practice.getInstance());
		pm.registerEvents(new LadderInventoryListener(), Practice.getInstance());
		pm.registerEvents(new JoinInventoryListener(), Practice.getInstance());
		pm.registerEvents(new UtilsListener(), Practice.getInstance());
		pm.registerEvents(new HostInteractItemListener(), Practice.getInstance());
		pm.registerEvents(new TournamentListener(), Practice.getInstance());
		pm.registerEvents(new TournamentInteractItemListener(), Practice.getInstance());
		pm.registerEvents(new SpectateListener(), Practice.getInstance());
		pm.registerEvents(new PersonnalListener(), Practice.getInstance());
		pm.registerEvents(new TagInventoryListener(), Practice.getInstance());
		pm.registerEvents(new PlayerListener(), Practice.getInstance());
		pm.registerEvents(new ServerListener(), Practice.getInstance());
	}

	private void registerCommand() {
		Practice.getInstance().getCommand("arena").setExecutor(new ArenaCommand());
		Practice.getInstance().getCommand("build").setExecutor(new BuildCommand());
		Practice.getInstance().getCommand("party").setExecutor(new PartyCommand());
		Practice.getInstance().getCommand("duel").setExecutor(new DuelCommand());
		Practice.getInstance().getCommand("accept").setExecutor(new AcceptCommand());
		Practice.getInstance().getCommand("deny").setExecutor(new DenyCommand());
		Practice.getInstance().getCommand("spectate").setExecutor(new SpectateCommand());
		Practice.getInstance().getCommand("inventory").setExecutor(new InventoryCommand());
		Practice.getInstance().getCommand("rank").setExecutor(new RankCommand());
		Practice.getInstance().getCommand("join").setExecutor(new JoinCommand());
		Practice.getInstance().getCommand("ban").setExecutor(new BanCommand());
		Practice.getInstance().getCommand("unban").setExecutor(new BanCommand());
		Practice.getInstance().getCommand("mute").setExecutor(new MuteCommand());
		Practice.getInstance().getCommand("unmute").setExecutor(new MuteCommand());
		Practice.getInstance().getCommand("freeze").setExecutor(new FreezeCommand());
		Practice.getInstance().getCommand("mod").setExecutor(new ModCommand());
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
	
	public List<Location> getOitcLocations() {
		return oitcLocations;
	}
}
