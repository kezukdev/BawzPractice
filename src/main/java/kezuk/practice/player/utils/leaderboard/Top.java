package kezuk.practice.player.utils.leaderboard;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.utils.MathUtils;
import kezuk.practice.utils.NPCUtils;
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
    	int[] arrayInt = null;
        for(Map.Entry<String, int[]> entry : map.entrySet()) {
            int global_elo=0;
            for(int elo : entry.getValue()) {
                global_elo+=elo;
            }
            global_elo = global_elo/entry.getValue().length;
            topboard.put(entry.getKey(), global_elo);
            arrayInt = new int[] {global_elo};
        }
        OptionalInt max = Arrays.stream(arrayInt).max();
        arrayInt = MathUtils.removeElementUsingCollection(arrayInt, max.getAsInt());
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
            for (int i = 0; i < 1; i++) {
            	if (x == 1) {
                    NPCUtils.createNPC(entry.getKey(), Practice.getInstance().getRegisterCommon().getTopNPC().get(0), ChatColor.DARK_AQUA + entry.getKey(), 0);	
            	}	
            	if (x == 2) {
                    NPCUtils.createNPC(entry.getKey(), Practice.getInstance().getRegisterCommon().getTopNPC().get(1), ChatColor.DARK_AQUA + entry.getKey(), 1);
            	}
            	if (x == 3) {
                    NPCUtils.createNPC(entry.getKey(), Practice.getInstance().getRegisterCommon().getTopNPC().get(2), ChatColor.DARK_AQUA + entry.getKey(), 2);
            	}
            }
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
