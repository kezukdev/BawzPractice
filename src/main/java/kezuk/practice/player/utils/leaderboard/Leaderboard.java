package kezuk.practice.player.utils.leaderboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.Profile;

public class Leaderboard {

    Top[] top = new Top[99];
    Top global;

    public static int getRowNumber(final String table) {
        try {
            final PreparedStatement sts = Practice.getInstance().connection.prepareStatement("select count(*) from " + table);
            final ResultSet rs = sts.executeQuery();
            if (rs.next()) {
                return rs.getInt("count(*)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Error while getting row numbers");
    }

   public Map<String, int[]> getTopElo() {
       final Map<String, int[]> top_elo = new HashMap<>();
       try {
           final PreparedStatement sts = Practice.getInstance().connection.prepareStatement("SELECT * FROM playersdata");
           final ResultSet rs = sts.executeQuery();
           //final ResultSetMetaData resultSetMetaData = rs.getMetaData();
           if (getRowNumber("playersdata") == 0) return null;
           if (rs.next()) {
               for (int i = 1; i <= getRowNumber("playersdata"); ++i) {
                   int[] elos = Profile.getSplitValue(DB.getFirstRow("SELECT elos FROM playersdata WHERE ID=?", i).getString("elos"), ":");
                   String player_name = DB.getFirstRow("SELECT name FROM playersdata WHERE ID=?", i).getString("name");
                   top_elo.put(player_name, elos);
               }
               return top_elo;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       throw new NullPointerException("The player does not have any information in the table");
   }

   public void refresh() {
       Map<String, int[]> map = getTopElo();
       for(Ladders ladder : Practice.getInstance().getLadder()) {
    	   if (ladder.isRanked()) {
               top[ladder.id()] = new Top(ladder.id(), map);   
    	   }
       }
       global = new Top(map);
   }

    public Top[] getTop() {
        return top;
    }

    public Top getGlobal() {
        return global;
    }

    public Leaderboard() {}
}