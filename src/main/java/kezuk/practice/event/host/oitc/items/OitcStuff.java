package kezuk.practice.event.host.oitc.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OitcStuff {
	
	public OitcStuff(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		player.getInventory().clear();
		player.getInventory().setItem(0, new ItemStack(Material.BOW));
		player.getInventory().setItem(1, new ItemStack(Material.ARROW));
		player.getInventory().setItem(8, new ItemStack(Material.GOLD_SWORD));
		player.updateInventory();
	}

}
