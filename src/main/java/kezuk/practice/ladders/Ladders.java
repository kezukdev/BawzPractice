package kezuk.practice.ladders;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import kezuk.bspigot.knockback.KnockbackProfile;
import kezuk.practice.Practice;
import kezuk.practice.arena.type.ArenaType;

public abstract class Ladders {

    public abstract String name();
    public abstract String displayName();
    public abstract Material material();
    public abstract int damageTicks();
    public abstract ArenaType arenaType();
    public abstract KnockbackProfile knockback();
    public abstract short data();
    public abstract int id();
    public abstract boolean isRanked();
    public abstract List<PotionEffect> potionEffect();
    public abstract boolean privateGame();

    // Kit editor
    public abstract boolean isAlterable();
    public abstract boolean additionalInventory();

    public static Ladders getLadder(String displayName)
    {
        return Practice.getInstance().getLadder().stream().filter(ladder -> ladder.displayName().equals(displayName)).findFirst().orElse(null);
    }
    
    public static Ladders getLadderByID(Integer id)
    {
        return Practice.getInstance().getLadder().stream().filter(ladder -> ladder.id() == id).findFirst().orElse(null);
    }

}
