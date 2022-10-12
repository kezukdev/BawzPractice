package kezuk.bawz.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import kezuk.bawz.Practice;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.utils.ItemSerializer;

public class LeaderboardInventory implements Runnable {

    @Override
    public void run() {
        Practice.getInstance().leaderboardInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Leaderboard:");
        for(Ladders ladder : Practice.getInstance().getLadder())
        {
            ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
            Practice.getInstance().leaderboardInventory.setItem(ladder.id()+9 ,item);
        }
        ItemStack item = ItemSerializer.serialize(new ItemStack(Material.BAKED_POTATO), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Top " + ChatColor.AQUA + "Global" + ChatColor.GRAY + " * ");
        Practice.getInstance().leaderboardInventory.setItem(4 ,item);

            while(true) {
                if (Bukkit.getOnlinePlayers().size() >= 1) {
                Practice.getInstance().getLeaderboardManager().refresh();
                Top[] top = Practice.getInstance().getLeaderboardManager().getTop();
                Top global_top = Practice.getInstance().getLeaderboardManager().getGlobal();
                for (Ladders ladder : Practice.getInstance().getLadder()) {
                    ItemStack current = Practice.getInstance().leaderboardInventory.getItem(ladder.id() + 9);
                    ItemMeta meta = current.getItemMeta();

                    meta.setLore(top[ladder.id()].getLore());
                    current.setItemMeta(meta);
                }

                ItemStack current = Practice.getInstance().leaderboardInventory.getItem(4);
                ItemMeta meta = current.getItemMeta();

                meta.setLore(global_top.getLore());
                current.setItemMeta(meta);

                try {
                    Thread.sleep(1000 * 15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
  