package kezuk.practice.ladders.gamemode;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import kezuk.bspigot.knockback.KnockbackModule;
import kezuk.bspigot.knockback.KnockbackProfile;
import kezuk.practice.arena.type.ArenaType;
import kezuk.practice.ladders.Kit;
import kezuk.practice.ladders.Ladders;
import net.md_5.bungee.api.ChatColor;

public class Sumo extends Ladders implements Kit {

    @Override
    public String name() {
        return "sumo";
    }

    @Override
    public String displayName() {
        return ChatColor.DARK_AQUA + "Sumo";
    }

    @Override
    public Material material() {
        return Material.ANVIL;
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
        return 8;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {
        };
        return Armor;
    }

    @Override
    public ItemStack[] content() {
        ItemStack[] Contents = {

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
		return ArenaType.SUMO;
	}

	@Override
	public boolean isRanked() {
		return true;
	}
}