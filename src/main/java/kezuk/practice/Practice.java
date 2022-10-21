package kezuk.practice;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import kezuk.practice.arena.Arena;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.ladders.gamemode.Axe;
import kezuk.practice.ladders.gamemode.Bow;
import kezuk.practice.ladders.gamemode.Boxing;
import kezuk.practice.ladders.gamemode.Combo;
import kezuk.practice.ladders.gamemode.Debuff;
import kezuk.practice.ladders.gamemode.NoDebuff;
import kezuk.practice.ladders.gamemode.Soup;
import kezuk.practice.ladders.gamemode.Sumo;
import kezuk.practice.registering.RegisterCollections;
import kezuk.practice.registering.RegisterCommon;
import kezuk.practice.registering.RegisterObject;
import kezuk.practice.registering.RegisterThread;
import kezuk.practice.registering.database.DataSQL;

public class Practice extends JavaPlugin {

	static Practice instance;
	
	private RegisterCollections registerCollections;
	private RegisterObject registerObject;
	public String configPath;
    public File arenaFile;
    public YamlConfiguration arenaConfig;
    public File kitFile;
    public YamlConfiguration kitConfig;
	private RegisterCommon registerCommon;
	private RegisterThread registerThread;
	private List<Ladders> ladder;
    public Connection connection;
    private DataSQL databaseSQL = new DataSQL();
	
	public Practice() {
		this.ladder = Arrays.asList(new NoDebuff(), new Debuff(), new Axe(), new Bow(), new Combo(), new Soup(), new Sumo(), new Boxing());
	}
	
	public void onEnable() {
		Practice.instance = this;
		this.registerCollections = new RegisterCollections();
		this.registerFile();
		this.registerObject = new RegisterObject();
		this.registerCommon = new RegisterCommon();
		this.setupHikariCP();
		this.setupDatabase();
		this.registerThread = new RegisterThread();
        this.registerArena();
	}
	

	private void registerFile() {
        this.configPath = Practice.getInstance().getDataFolder() + "/hikari.properties";
        this.saveResource("hikari.properties", false);
        this.saveResource("arenas.yml", false);
        this.saveResource("kits.yml", false);
        this.arenaFile = new File(Practice.getInstance().getDataFolder() + "/arenas.yml");
        this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
        this.kitFile = new File(Practice.getInstance().getDataFolder() + "/kits.yml");
        this.kitConfig = YamlConfiguration.loadConfiguration(this.kitFile);
	}
	
    private void registerArena() {
        final ConfigurationSection cs = arenaConfig.getConfigurationSection("arenas");
        if (cs != null) {
            for (final String s : cs.getKeys(false)) {
                if (s != null) {
                    final ConfigurationSection cs2 = cs.getConfigurationSection(s);
                    if (cs2 == null) {
                        continue;
                    }
                    final Arena arena = new Arena(cs2.getName());
                    arena.load();
                }
            }
        }
    }  
	
    private void setupDatabase() {
        if (this.connection != null) {
            this.databaseSQL.createPlayerManagerTable();
            return;
        }
        System.out.println("WARNING enter valid database information (" + Practice.getInstance().configPath + ") \n You will not be able to access many features");
    }
    
    private void setupHikariCP() {
        try {
            final HikariConfig config = new HikariConfig(Practice.getInstance().configPath);
            @SuppressWarnings("resource")
			final HikariDataSource ds = new HikariDataSource(config);
            final String passwd = (config.getDataSourceProperties().getProperty("password") == null) ? "" : config.getDataSourceProperties().getProperty("password");
            final Database db = BukkitDB.createHikariDatabase(Practice.getInstance(), config.getDataSourceProperties().getProperty("user"), passwd, config.getDataSourceProperties().getProperty("databaseName"), config.getDataSourceProperties().getProperty("serverName") + ":" + config.getDataSourceProperties().getProperty("portNumber"));
            DB.setGlobalDatabase(db);
            this.connection = ds.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Error could not connect to SQL database.");
            e.printStackTrace();
        }
        System.out.println("Successfully connected to the SQL database.");
    }
	
    public DataSQL getDatabaseSQL() {
		return databaseSQL;
	}
    
	public RegisterObject getRegisterObject() {
		return registerObject;
	}
	
	public RegisterCollections getRegisterCollections() {
		return registerCollections;
	}
	
	public RegisterCommon getRegisterCommon() {
		return registerCommon;
	}
	
	public RegisterThread getRegisterThread() {
		return registerThread;
	}
	
	public List<Ladders> getLadder() {
		return ladder;
	}
	
	public static Practice getInstance() {
		return Practice.instance;
	}
}
