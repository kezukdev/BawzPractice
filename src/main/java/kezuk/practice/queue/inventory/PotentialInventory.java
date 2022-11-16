package kezuk.practice.queue.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.utils.ItemSerializer;
import kezuk.practice.utils.MultipageSerializer;

public class PotentialInventory {
	
	private MultipageSerializer potential;
	
	public PotentialInventory() {
        this.potential = new MultipageSerializer(Lists.newArrayList(), ChatColor.DARK_GRAY + "Potential Opponents:", ItemSerializer.serialize(new ItemStack(Material.CACTUS), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Currently in queue." + ChatColor.GRAY + " * "));
	}

	public MultipageSerializer getPotential() {
		return potential;
	}
}
