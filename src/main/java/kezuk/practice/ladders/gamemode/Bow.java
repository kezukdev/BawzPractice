package kezuk.practice.ladders.gamemode;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import kezuk.bspigot.knockback.KnockbackModule;
import kezuk.bspigot.knockback.KnockbackProfile;
import kezuk.practice.arena.type.ArenaType;
import kezuk.practice.ladders.Kit;
import kezuk.practice.ladders.Ladders;
import net.md_5.bungee.api.ChatColor;

public class Bow extends Ladders implements Kit {

    @Override
    public String name() {
        return "bow";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Bow";
    }

    @Override
    public Material material() {
        return Material.BOW;
    }

    @Override
    public short data() {
        return (short)0;
    }

    @Override
    public boolean isAlterable() {
        return true;
    }

    @Override
    public boolean additionalInventory() {
        return true;
    }

    @Override
    public int id() {
        return 4;
    }

    @Override
    public ItemStack[] armor() {
    	ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
    	ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
    	ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
    	ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack[] Armor = {boots, leggings, chestplate, helmet};
        return Armor;
    }

    @Override
    public ItemStack[] content() {
    	ItemStack attackItem = new ItemStack(Material.BOW);
    	attackItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    	attackItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
    	attackItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemStack[] Contents = {attackItem,
                new ItemStack(Material.ENDER_PEARL, 16, (short)0),
                air,
                air,
                air,
                air,
                air,
                air,
                new ItemStack(Material.COOKED_BEEF, 64, (short)0),
                
                new ItemStack(Material.ARROW, 1, (short)0),
                

        };
        return Contents;
    }

	@Override
	public int damageTicks() {
		return 20;
	}

	@Override
	public KnockbackProfile knockback() {
		return KnockbackModule.INSTANCE.profiles.get("default");
	}
	
	@Override
	public ArenaType arenaType() {
		return ArenaType.NORMAL;
	}

}
