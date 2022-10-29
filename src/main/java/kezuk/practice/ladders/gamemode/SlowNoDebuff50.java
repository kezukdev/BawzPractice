package kezuk.practice.ladders.gamemode;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import kezuk.bspigot.knockback.KnockbackModule;
import kezuk.bspigot.knockback.KnockbackProfile;
import kezuk.practice.arena.type.ArenaType;
import kezuk.practice.ladders.Kit;
import kezuk.practice.ladders.Ladders;
import net.md_5.bungee.api.ChatColor;

public class SlowNoDebuff50 extends Ladders implements Kit {

    @Override
    public String name() {
        return "slowndb";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Slow NoDebuff 50%";
    }

    @Override
    public Material material() {
        return Material.SOUL_SAND;
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
        return 9;
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
        attackItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        attackItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack[] Contents = {
        		attackItem,
        		new ItemStack(Material.ENDER_PEARL, 16, (short)0),
        		potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                new ItemStack(Material.POTION, 1, (short)8259),
                new ItemStack(Material.POTION, 1, (short)8226),
                new ItemStack(Material.COOKED_BEEF, 64),

                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                new ItemStack(Material.POTION, 1, (short)8226),

                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                potionNdb,
                new ItemStack(Material.POTION, 1, (short)8226),

                potionNdb,
                potionNdb,
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
		return 40;
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

	@Override
	public List<PotionEffect> potionEffect() {
		return null;
	}
	
	@Override
	public boolean privateGame() {
		return true;
	}
}
