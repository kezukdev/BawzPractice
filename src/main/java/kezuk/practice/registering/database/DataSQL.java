package kezuk.practice.registering.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import co.aikar.idb.DB;
import co.aikar.idb.DbStatement;
import kezuk.practice.Practice;

public class DataSQL {

    public boolean createPlayerManagerTable() {
        return DB.createTransaction(stm -> createPlayerManagerTable(stm));
    }

    private boolean createPlayerManagerTable(DbStatement stm) {
        String player_manager = "CREATE TABLE playersdata ("
                + "ID INT(64) NOT NULL AUTO_INCREMENT,"
                + "name VARCHAR(16) NOT NULL,"
                + "uuid VARCHAR(64) NOT NULL,"
                + "unrankedPlayed INT(9999) DEFAULT 0,"
                + "unrankedWin INT(9999) DEFAULT 0,"
                + "rankedPlayed INT(9999) DEFAULT 0,"
                + "rankedWin INT(9999) DEFAULT 0,"
                + "scoreboard VARCHAR(16) DEFAULT 'true',"
                + "duel VARCHAR(16) DEFAULT 'true',"
                + "pm VARCHAR(16) DEFAULT 'true',"
                + "banned VARCHAR(16) DEFAULT 'false',"
                + "banExpires VARCHAR(255) DEFAULT 'null',"
                + "banReason VARCHAR(255) DEFAULT 'null',"
                + "muted VARCHAR(16) DEFAULT 'false',"
                + "muteExpires VARCHAR(255) DEFAULT 'null',"
                + "muteReason VARCHAR(255) DEFAULT 'null',"
                + "rank VARCHAR(16) DEFAULT 'Player',"
                + "tag VARCHAR(16) DEFAULT 'Normal',"
                + "elos VARCHAR(255) DEFAULT '1200:1200:1200:1200:1200',"
                + "mostPlayed VARCHAR(255) DEFAULT '0:0:0:0:0:0:0:0:0:0:0',"
                + "PRIMARY KEY (`ID`))";
        try {
            DatabaseMetaData dbm = Practice.getInstance().connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "playersdata", null);
            if (tables.next()) {
                //table exist
                return false;
            } else {
                //table doesn't exist
                stm.executeUpdateQuery(player_manager);
                System.out.println("The SQL database was sucessfuly installed with tables.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("An error was occured with the Practice database!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean createPlayerManager(UUID uuid, String name) {
        return DB.createTransaction(stm -> createPlayerManager(uuid, name, stm));
    }
	
    public boolean existPlayerManager(UUID uuid) {
        return DB.createTransaction(stm -> existPlayerManager(uuid, stm));
    }
    
    private boolean existPlayerManager(UUID uuid, DbStatement stm) {
        String query = "SELECT * FROM playersdata WHERE uuid=?";
        try {
            return stm.executeQueryGetFirstRow(query, uuid.toString()) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean createPlayerManager(UUID uuid, String name, DbStatement stm) {
        String query = "INSERT INTO playersdata (uuid, name) " +
                "VALUES (?, ?)";
        try {
            return stm.executeUpdateQuery(query, uuid.toString(), name) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public boolean updatePlayerManager(String name, UUID uuid) {
    	return DB.createTransaction(stm -> updatePlayerManager(name, uuid, stm));
    }
    

    private boolean updatePlayerManager(String name, UUID uuid, DbStatement stm) {
        String query = "UPDATE playersdata SET name=? WHERE uuid=?";
        try {
            return stm.executeUpdateQuery(query, name, uuid.toString()) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}