package kezuk.practice.match.spectate;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.utils.ItemSerializer;
import kezuk.practice.utils.MultipageSerializer;

public class SpectateInventory {
	
    private MultipageSerializer spectateInventory;
    
    public SpectateInventory() {
        this.spectateInventory = new MultipageSerializer(Lists.newArrayList(), ChatColor.DARK_GRAY + "Spectate", ItemSerializer.serialize(new ItemStack(Material.COMPASS), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Current Fight" + ChatColor.GRAY + " * "));
    }
	
	public MultipageSerializer getSpectateInventory() {
		return spectateInventory;
	}
}
