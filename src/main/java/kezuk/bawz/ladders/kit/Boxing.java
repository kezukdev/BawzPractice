package kezuk.bawz.ladders.kit;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import kezuk.bawz.arena.ArenaType;
import kezuk.bawz.ladders.Kit;
import kezuk.bawz.ladders.Ladders;
import kezuk.bspigot.knockback.KnockbackModule;
import kezuk.bspigot.knockback.KnockbackProfile;
import net.md_5.bungee.api.ChatColor;

public class Boxing extends Ladders implements Kit {

    @Override
    public String name() {
        return "boxing";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Boxing";
    }

    @Override
    public Material material() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public short data() {
        return (short)0;
    }

    @Override
    public boolean isAlterable() {
        return false;
    }

    @Override
    public boolean additionalInventory() {
        return true;
    }

    @Override
    public int id() {
        return 5;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {
        };
        return Armor;
    }

    @Override
    public ItemStack[] content() {
    	final ItemStack attackItem = new ItemStack(Material.DIAMOND_SWORD);
    	attackItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    	attackItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack[] Contents = {
        		attackItem
        };
        return Contents;
    }
    

	@Override
	public int damageTicks() {
		return 20;
	}

	@Override
	public KnockbackProfile knockback() {
		return KnockbackModule.INSTANCE.profiles.get("sumo");
	}
	
	@Override
	public ArenaType arenaType() {
		return ArenaType.NORMAL;
	}

}