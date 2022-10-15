package kezuk.bawz.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import kezuk.bawz.Practice;

public class ArenaManager {
    static List<ArenaManager> all;
    private String name;
    private ArenaType arenaType;
    private Location loc1;
    private Location middle;
    private Location loc2;
    
    public ArenaManager(final String name, final ArenaType arenaType, final Location loc1, final Location middle, final Location loc2) {
        this.name = name;
        this.arenaType = arenaType;
        this.loc1 = loc1;
        this.middle = middle;
        this.loc2 = loc2;
        ArenaManager.all.add(this);
    }
    
    public ArenaManager(final String name, final ArenaType arenaType) {
        this(name, arenaType, null, null, null);
    }
    
    public ArenaManager(final String name) {
        this(name, null, null, null, null);
    }
    
    public static ArenaManager getArena(final String name) {
        return ArenaManager.all.stream().filter(arenaManager -> arenaManager.name.equals(name)).findFirst().orElse(null);
    }
    
    public void save() {
        if (this.arenaType == null || this.loc1 == null || this.loc2 == null) {
            return;
        }
        Practice.getInstance().arenaConfig.set("arenas." + this.getName() + ".type", (Object)this.getArenaType().toString());
        Practice.getInstance().arenaConfig.set("arenas." + this.getName() + ".pos1", (Object)this.getStringLocation(this.loc1));
        Practice.getInstance().arenaConfig.set("arenas." + this.getName() + ".mid", (Object)this.getStringLocation(this.middle));
        Practice.getInstance().arenaConfig.set("arenas." + this.getName() + ".pos2", (Object)this.getStringLocation(this.loc2));
    }
    
    private String getStringLocation(final Location loc) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(loc.getWorld().getName());
        stringBuilder.append(":");
        stringBuilder.append(loc.getX());
        stringBuilder.append(":");
        stringBuilder.append(loc.getY());
        stringBuilder.append(":");
        stringBuilder.append(loc.getZ());
        stringBuilder.append(":");
        stringBuilder.append(loc.getYaw());
        stringBuilder.append(":");
        stringBuilder.append(loc.getPitch());
        return stringBuilder.toString();
    }
    
    public void load() {
        if (Practice.getInstance().arenaConfig.getString("arenas." + this.getName() + ".type") == null) {
            return;
        }
        this.arenaType = ArenaType.valueOf(Practice.getInstance().arenaConfig.getString("arenas." + this.getName() + ".type").toUpperCase());
        this.loc1 = this.getSplitLocation(1);
        final String[] split = Practice.getInstance().arenaConfig.getString("arenas." + this.getName() + ".mid").split(":");
        this.middle = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        this.loc2 = this.getSplitLocation(2);
    }
    
    private Location getSplitLocation(final int pos) {
        if (Practice.getInstance().arenaConfig.getString("arenas." + this.getName() + ".pos" + pos) == null) {
            return null;
        }
        final String[] split = Practice.getInstance().arenaConfig.getString("arenas." + this.getName() + ".pos" + pos).split(":");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
    
    public static ArenaManager getRandomArena(ArenaType arenaType)
    {
        List<ArenaManager> availableArena = all.stream().filter(arenaManager -> arenaManager.getArenaType() == arenaType).collect(Collectors.toList());
        Collections.shuffle(availableArena);
        return availableArena.get(0);
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArenaType getArenaType() {
        return this.arenaType;
    }
    
    public Location getLoc1() {
        return this.loc1;
    }
    
    public Location getMiddle() {
		return middle;
	}
    
    public void setMiddle(Location middle) {
		this.middle = middle;
	}
    
    public Location getLoc2() {
        return this.loc2;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setArenaType(final ArenaType arenaType) {
        this.arenaType = arenaType;
    }
    
    public void setLoc1(final Location loc1) {
        this.loc1 = loc1;
    }
    
    public void setLoc2(final Location loc2) {
        this.loc2 = loc2;
    }
    
    public static List<ArenaManager> getAll() {
        return ArenaManager.all;
    }
    
    static {
        ArenaManager.all = new ArrayList<ArenaManager>();
    }
}