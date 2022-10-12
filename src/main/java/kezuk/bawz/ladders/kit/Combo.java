package kezuk.bawz.ladders.kit;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import kezuk.bawz.arena.ArenaType;
import kezuk.bawz.ladders.Kit;
import kezuk.bawz.ladders.Ladders;
import kezuk.knockback.KnockbackModule;
import kezuk.knockback.KnockbackProfile;
import net.md_5.bungee.api.ChatColor;

public class Combo extends Ladders implements Kit {

    @Override
    public String name() {
        return "combo";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Combo";
    }

    @Override
    public Material material() {
        return Material.RAW_FISH;
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
        return 6;
    }

    @Override
    public ItemStack[] armor() {
    	ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
    	ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
    	ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
    	ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack[] Armor = {boots, leggings, chestplate, helmet};
        return Armor;
    }

    @Override
    public ItemStack[] content() {
    	final ItemStack attackItem = new ItemStack(Material.DIAMOND_SWORD);
    	attackItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
    	attackItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
    	attackItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        ItemStack[] Contents = {attackItem,
                new ItemStack(Material.GOLDEN_APPLE, 64, (short)1),
                air,
                air,
                air,
                air,
                new ItemStack(Material.POTION, 1, (short)8226),
                new ItemStack(Material.POTION, 1, (short)8233),
                new ItemStack(Material.COOKED_BEEF, 64, (short)0),

                new ItemStack(Material.POTION, 1, (short)8226),
                new ItemStack(Material.POTION, 1, (short)8233),

                
                new ItemStack(Material.POTION, 1, (short)8226),
                new ItemStack(Material.POTION, 1, (short)8233),


                new ItemStack(Material.POTION, 1, (short)8226),
                new ItemStack(Material.POTION, 1, (short)8233),

        };
        return Contents;
    }

	@Override
	public int damageTicks() {
		return 4;
	}

	@Override
	public KnockbackProfile knockback() {
		return KnockbackModule.INSTANCE.profiles.get("combo");
	}
	
	@Override
	public ArenaType arenaType() {
		return ArenaType.NORMAL;
	}

}
