package kezuk.bawz.utils;

import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

public class ItemSerializer
{
    public static ItemStack serialize(final ItemStack itemStack, short typeID, final String displayName) {
        return serialize(itemStack, typeID, displayName, null, false);
    }
    
    public static ItemStack serialize(final ItemStack itemStack, short typeID, final String displayName, final List<String> lore) {
        return serialize(itemStack, typeID, displayName, lore, false);
    }
    
    public static ItemStack serialize(final ItemStack itemStack, short typeID,  final String displayName, final List<String> lore, final boolean unbreakable) {
        final ItemStack item = new ItemStack(itemStack.getType(), 1, typeID);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.spigot().setUnbreakable(unbreakable);
        item.setItemMeta(itemMeta);
        return item;
    }
}
