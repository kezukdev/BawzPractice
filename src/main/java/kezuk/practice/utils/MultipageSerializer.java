package kezuk.practice.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MultipageSerializer {

    private Inventory[] inventories;
    private String inventoryName;
    private ItemStack icon;

    public MultipageSerializer(List<ItemStack> itemStacks, String inventoryName, ItemStack icon) {
        this.inventoryName = inventoryName;
        this.icon = icon;
        refresh(itemStacks);
    }

    public void refresh(List<ItemStack> itemStacks) {
        int var0 = itemStacks.size() <= 36 ? 1 : (itemStacks.size()/36)+1;
        inventories = new Inventory[var0];
        for(int page = 1; page <= var0; page++) {
            int start = page == 1 ? 0 : (page-1) * 36;
            Inventory inventory = Bukkit.createInventory(null, 9*6, inventoryName);

            // add base inventory
            inventory.setItem(45, ItemSerializer.serialize(new ItemStack(Material.LEVER), (short)0, ChatColor.AQUA + "Previous Page", Arrays.asList(new String(ChatColor.GRAY + "Click here for pack to the previous page."))));
            inventory.setItem(49, ItemSerializer.serialize(new ItemStack(Material.PAPER), (short)0, ChatColor.AQUA + "Actual Page: ", Arrays.asList(ChatColor.GRAY + "The actual page is: " + ChatColor.DARK_AQUA + page)));
            int nextPage = page + 1;
            inventory.setItem(53, ItemSerializer.serialize(new ItemStack(Material.ARROW), (short)0, ChatColor.AQUA + "Next Page" , Arrays.asList(ChatColor.GRAY + "The next page is: " + ChatColor.AQUA + nextPage)));
            for (int i = 0; i <= 8; i++) inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3));
            inventory.setItem(4, icon);

            if(itemStacks.size() != 0) {
                // add page content
                for (int slot = 9; slot <= 44; slot++) {
                    if (start >= itemStacks.size()) break;
                    inventory.setItem(slot, itemStacks.get(start));
                    start++;
                }
            }

            // save page
            inventories[page-1] = inventory;
        }
    }

    public void open(Player player, int page) {
        if(page <= 0 || page > inventories.length) return;
        if(page == 1) inventories[page-1].setItem(45, new ItemStack(Material.AIR));
        if(inventories.length == page) inventories[page-1].setItem(53, new ItemStack(Material.AIR));
        player.openInventory(inventories[page-1]);
    }

}
