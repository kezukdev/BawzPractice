package kezuk.bawz;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.*;

import com.bizarrealex.aether.Aether;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import kezuk.bawz.arena.*;
import kezuk.bawz.board.PracticeBoard;
import kezuk.bawz.database.DataSQL;
import kezuk.bawz.host.HostManager;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.ladders.kit.Axe;
import kezuk.bawz.ladders.kit.Bow;
import kezuk.bawz.ladders.kit.Boxing;
import kezuk.bawz.ladders.kit.Combo;
import kezuk.bawz.ladders.kit.Debuff;
import kezuk.bawz.ladders.kit.NoDebuff;
import kezuk.bawz.ladders.kit.Soup;
import kezuk.bawz.ladders.kit.Sumo;
import kezuk.bawz.leaderboard.LeaderboardInventory;
import kezuk.bawz.leaderboard.LeaderboardManager;
import kezuk.bawz.manager.*;
import kezuk.bawz.match.*;
import kezuk.bawz.party.PartyManager;
import kezuk.bawz.queue.*;
import kezuk.bawz.register.*;
import net.luckperms.api.LuckPerms;
import net.minecraft.util.com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Practice extends JavaPlugin {
	
    static Practice instance;
    private String configPath;
    public File arenaFile;
    public YamlConfiguration arenaConfig;
    public DataSQL databaseSQL = new DataSQL();
    private ArenaManager arenaManager;
    private ItemsManager itemsManager;
    private InventoryManager inventoryManager;
    private QueueManager queueManager;
    public Inventory leaderboardInventory;
    private LeaderboardManager leaderboardManager;
    private HostManager hostManager;
    public Connection connection;
    private Map<UUID, MatchSeeInventory> offlineInventories;
    private Thread leaderboardThread;
    public LuckPerms luckPerms;
    private List<Ladders> ladder;
    public static HashMap<UUID, PartyManager> partys;
    public static HashMap<UUID, MatchManager> matchs;
    
    public Practice() {
    	this.ladder = Arrays.asList(new NoDebuff(), new Debuff(), new Axe(), new Bow(), new Combo(), new Soup(), new Sumo(), new Boxing());
        this.offlineInventories = new WeakHashMap<UUID, MatchSeeInventory>();
        matchs = Maps.newHashMap();
    }
    
    public void onEnable() {
        Practice.instance = this;
        this.luckPerms = (LuckPerms)this.getServer().getServicesManager().load((Class)LuckPerms.class);
        this.configPath = this.getDataFolder() + "/hikari.properties";
        this.saveResource("hikari.properties", false);
        this.saveResource("arenas.yml", false);
        this.arenaFile = new File(this.getDataFolder() + "/arenas.yml");
        this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
		this.setupHikariCP();
		this.setupDatabase();
        this.leaderboardManager = new LeaderboardManager();
        (this.leaderboardThread = new Thread(new LeaderboardInventory())).start();
        this.itemsManager = new ItemsManager();
        this.queueManager = new QueueManager();
        this.inventoryManager = new InventoryManager();
        this.hostManager = new HostManager();
        new Aether(this, new PracticeBoard());
        new Register();
    }
    
    public void onDisable() {
        ArenaManager.getAll().forEach(arenaManager -> arenaManager.save());
        try {
            this.arenaConfig.save(this.arenaFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setupDatabase() {
        if (this.connection != null) {
            this.databaseSQL.createPlayerManagerTable();
            return;
        }
        System.out.println("WARNING enter valid database information (" + this.configPath + ") \n You will not be able to access many features");
    }
    
    private void setupHikariCP() {
        try {
            final HikariConfig config = new HikariConfig(this.configPath);
            @SuppressWarnings("resource")
			final HikariDataSource ds = new HikariDataSource(config);
            final String passwd = (config.getDataSourceProperties().getProperty("password") == null) ? "" : config.getDataSourceProperties().getProperty("password");
            final Database db = BukkitDB.createHikariDatabase(this, config.getDataSourceProperties().getProperty("user"), passwd, config.getDataSourceProperties().getProperty("databaseName"), config.getDataSourceProperties().getProperty("serverName") + ":" + config.getDataSourceProperties().getProperty("portNumber"));
            DB.setGlobalDatabase(db);
            this.connection = ds.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Error could not connect to SQL database.");
            e.printStackTrace();
        }
        System.out.println("Successfully connected to the SQL database.");
    }
    
    public static HashMap<UUID, MatchManager> getMatchs() {
		return matchs;
	}
    
    public LuckPerms getLuckPerms() {
		return luckPerms;
	}
    
    public Connection getConnection() {
		return connection;
	}
    
    public String getConfigPath() {
		return configPath;
	}
    
    public Thread getLeaderboardThread() {
		return leaderboardThread;
	}
    
    public DataSQL getDatabaseSQL() {
		return databaseSQL;
	}
    
    public LeaderboardManager getLeaderboardManager() {
		return leaderboardManager;
	}
    
    public ArenaManager getArenaManager() {
        return this.arenaManager;
    }
    
    public ItemsManager getItemsManager() {
        return this.itemsManager;
    }
    
    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
    
    public QueueManager getQueueManager() {
        return this.queueManager;
    }
    
    public Inventory getLeaderboardInventory() {
		return leaderboardInventory;
	}
    
    public Map<UUID, MatchSeeInventory> getOfflineInventories() {
        return this.offlineInventories;
    }
    
    public HostManager getHostManager() {
		return hostManager;
	}
    
    public static Practice getInstance() {
        return Practice.instance;
    }
    
    public List<Ladders> getLadder() {
		return ladder;
	}
    
    public static HashMap<UUID, PartyManager> getPartys() {
		return partys;
	}
}
