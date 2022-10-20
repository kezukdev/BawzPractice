package kezuk.practice.player.utils.subinventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.utils.leaderboard.Top;
import kezuk.practice.utils.ItemSerializer;

public class LeaderboardInventory implements Runnable {

    @Override
    public void run() {
        for(Ladders ladder : Practice.getInstance().getLadder()) {
            ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
            Practice.getInstance().getRegisterObject().getUtilsInventory().getLeaderboardInventory().setItem(ladder.id()+9 ,item);
        }
        ItemStack item = ItemSerializer.serialize(new ItemStack(Material.BAKED_POTATO), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Top " + ChatColor.AQUA + "Global" + ChatColor.GRAY + " * ");
        Practice.getInstance().getRegisterObject().getUtilsInventory().getLeaderboardInventory().setItem(4 ,item);

            while(true) {
                if (Bukkit.getOnlinePlayers().size() >= 1) {
                Practice.getInstance().getRegisterObject().getLeaderboard().refresh();
                Top[] top = Practice.getInstance().getRegisterObject().getLeaderboard().getTop();
                Top global_top = Practice.getInstance().getRegisterObject().getLeaderboard().getGlobal();
                for (Ladders ladder : Practice.getInstance().getLadder()) {
                    ItemStack current = Practice.getInstance().getRegisterObject().getUtilsInventory().getLeaderboardInventory().getItem(ladder.id() + 9);
                    ItemMeta meta = current.getItemMeta();

                    meta.setLore(top[ladder.id()].getLore());
                    current.setItemMeta(meta);
                }

                ItemStack current = Practice.getInstance().getRegisterObject().getUtilsInventory().getLeaderboardInventory().getItem(4);
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
  