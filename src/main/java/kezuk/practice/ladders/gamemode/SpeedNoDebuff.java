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

public class SpeedNoDebuff extends Ladders implements Kit {

    @Override
    public String name() {
        return "speedndb";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Speed NoDebuff";
    }

    @Override
    public Material material() {
        return Material.BLAZE_POWDER;
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
        return 8;
    }

    @Override
    public ItemStack[] armor() {
    	ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
    	ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
    	ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
    	ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack[] Armor = {boots, leggings, chestplate, helmet};
        return Armor;
    }
    
    public ItemStack potionNdb = new ItemStack(Material.POTION, 1, (short)16421);

    @Override
    public ItemStack[] content() {
        ItemStack attackItem = new ItemStack(Material.DIAMOND_SWORD);
        attackItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        attackItem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        attackItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        attackItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack[] Contents = {
        		attackItem,
        		new ItemStack(Material.ENDER_PEARL, 16, (short)0),
        		potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,

                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,

                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,
                air,

                air,
                air,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,

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

	@Override
	public boolean isRanked() {
		return false;
	}

}
