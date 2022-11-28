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

public class Comboxing extends Ladders implements Kit {

    @Override
    public String name() {
        return "combo";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Comboxing";
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
        ItemStack[] Armor = {air, air, air, air};
        return Armor;
    }

    @Override
    public ItemStack[] content() {
    	final ItemStack attackItem = new ItemStack(Material.DIAMOND_SWORD);
    	attackItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemStack[] Contents = {
        		attackItem,
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

        };
        return Contents;
    }

	@Override
	public int damageTicks() {
		return 5;
	}

	@Override
	public KnockbackProfile knockback() {
		return KnockbackModule.INSTANCE.profiles.get("combo");
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
		return false;
	}
}
