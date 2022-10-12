package kezuk.bawz.ladders;

import org.bukkit.*;

import kezuk.bawz.Practice;
import kezuk.bawz.arena.ArenaType;
import kezuk.knockback.KnockbackProfile;

public abstract class Ladders {

    public abstract String name();
    public abstract String displayName();
    public abstract Material material();
    public abstract int damageTicks();
    public abstract ArenaType arenaType();
    public abstract KnockbackProfile knockback();
    public abstract short data();
    public abstract int id();

    // Kit editor
    public abstract boolean isAlterable();
    public abstract boolean additionalInventory();

    public static Ladders getLadder(String displayName)
    {
        return Practice.getInstance().getLadder().stream().filter(ladder -> ladder.displayName().equals(displayName)).findFirst().orElse(null);
    }

}