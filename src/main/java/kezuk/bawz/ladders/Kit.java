package kezuk.bawz.ladders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Kit {

    ItemStack[] content();

    ItemStack[] armor();

    ItemStack air = new ItemStack(Material.AIR);

}
