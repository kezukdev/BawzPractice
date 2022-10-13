package kezuk.bawz.register;

import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

import kezuk.bawz.*;
import kezuk.bawz.arena.ArenaManager;
import kezuk.bawz.command.*;
import kezuk.bawz.core.ChatListener;
import kezuk.bawz.listener.*;
import kezuk.bawz.match.*;

public class Register
{
    public Register() {
    	this.registerArena();
        this.registerListener();
        this.registerCommand();
    }
    
    private void registerCommand() {
        Practice.getInstance().getCommand("arena").setExecutor((CommandExecutor)new ArenaCommand());
        Practice.getInstance().getCommand("inventory").setExecutor((CommandExecutor)new InventoryCommand());
        Practice.getInstance().getCommand("duel").setExecutor((CommandExecutor)new DuelCommand());
        Practice.getInstance().getCommand("spectate").setExecutor((CommandExecutor)new SpectateCommand());
        Practice.getInstance().getCommand("build").setExecutor((CommandExecutor)new BuildCommand());
        Practice.getInstance().getCommand("accept").setExecutor((CommandExecutor)new AcceptCommand());
        Practice.getInstance().getCommand("party").setExecutor((CommandExecutor)new PartyCommand());
    }
    
    private void registerListener() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new EnderDelayListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new InventoryListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new MatchListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new PlayerListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new ServerListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new ChatListener(), (Plugin)Practice.getInstance());
    }
    
    
    private void registerArena() {
        final ConfigurationSection cs = Practice.getInstance().arenaConfig.getConfigurationSection("arenas");
        if (cs != null) {
            for (final String s : cs.getKeys(false)) {
                if (s != null) {
                    final ConfigurationSection cs2 = cs.getConfigurationSection(s);
                    if (cs2 == null) {
                        continue;
                    }
                    final ArenaManager arena = new ArenaManager(cs2.getName());
                    arena.load();
                }
            }
        }
    }
    
}
