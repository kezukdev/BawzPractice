package kezuk.practice.player.utils.leaderboard;

import java.util.*;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;

public class Top {

    private int elo_id;

    private Map<String, Integer> topboard = new HashMap<>();
    private ArrayList<String> lore = new ArrayList<>();

    Top(int elo_id, Map<String, int[]> map) {
        this.elo_id = elo_id;
        extirpate(map);
        organise();
    }

    Top(Map<String, int[]> map) {
        for(Map.Entry<String, int[]> entry : map.entrySet())
        {
            int global_elo=0;
            for(int elo : entry.getValue())
            {
                global_elo+=elo;
            }
            global_elo = global_elo/entry.getValue().length;
            topboard.put(entry.getKey(), global_elo);
        }
        organise();
    }

    private void extirpate(Map<String, int[]> map) {
        map.entrySet().forEach(stringEntry -> topboard.put(stringEntry.getKey(), stringEntry.getValue()[elo_id]));
    }

    private void organise() {
        List<Map.Entry<String, Integer>> entries = topboard.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(topboard.size()).collect(Collectors.toList());
        Collections.reverse(entries);
        int x=1;
        for(Map.Entry<String, Integer> entry : entries) {
            if(x > 10) break;
            lore.add(ChatColor.DARK_GRAY + "#" + x + " " + ChatColor.DARK_AQUA + entry.getKey() + ChatColor.GRAY + " (" + ChatColor.AQUA + entry.getValue() + ChatColor.GRAY + ")");
            x++;
        }
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public Map<String, Integer> getTopboard() {
        return topboard;
    }
}
