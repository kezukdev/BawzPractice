package kezuk.practice.core.rank;

import org.bukkit.ChatColor;

public enum Rank {
	
	OWNER("Owner", ChatColor.DARK_RED, new String[] {"bawz.rank", "bukkit.command.ban.player", "verus.staff", "bawz.bypass", "bawz.arena", "bawz.manage.player", "bawz.pro", "bawz.challenger", "bawz.strong", "bawz.mod", "bawz.admin"}),
	PLATFORM_ADMIN("Platform-Admin", ChatColor.DARK_RED , new String[] {"bawz.rank", "bukkit.command.player.ban", "verus.staff", "spigot.*", "bawz.arena", "bawz.manage.player", "bawz.pro", "bawz.challenger", "bawz.strong", "bawz.mod", "bawz.admin"}),
	DEVELOPER("Developer", ChatColor.BLUE, new String[] {"bawz.rank", "bukkit.command.ban.player", "bukkit.*", "spigot.*", "bawz.arena", "bawz.manage.player", "bawz.pro", "bawz.challenger", "bawz.strong", "bawz.mod", "bawz.admin"}),
	ADMINISTRATOR("Administrator", ChatColor.RED, new String[] {"bawz.rank", "bukkit.command.ban.player", "bawz.arena", "bawz.manage.player", "bawz.pro", "bawz.challenger", "bawz.strong", "bawz.mod", "bawz.admin"}),
	SENIOR_MOD("Senior-Mod", ChatColor.DARK_AQUA, new String[] {"bukkit.command.ban.player", "bukkit.command.kick", "verus.staff.alerts", "verus.staff.logs", "verus.staff.logs.recent"}),
	MOD("Mod", ChatColor.AQUA, new String[] {"bukkit.command.ban.player", "bukkit.command.pardon.player", "bukkit.command.kick", "verus.staff.alerts", "verus.staff.logs", "verus.staff.logs.recent"}),
	BUILDER("Builder", ChatColor.DARK_GREEN, new String[] {"bukkit.command.gamemode", "bawz.build"}),
	TRIALMOD("TrialMod", ChatColor.YELLOW, new String[] {"bukkit.command.ban.player", "bukkit.command.kick", "verus.staff.alerts", "verus.staff.logs", "verus.staff.logs.recent"}),
	FAMOUS("Famous", ChatColor.DARK_PURPLE, new String[] {"bawz.host", "bawz.tournament"}),
	MEDIA("Media", ChatColor.LIGHT_PURPLE, new String[] {"bawz.host", "bawz.tournament"}),
	STRONG("Strong", ChatColor.RED, new String[] {"bawz.host", "bawz.tournament"}),
	PRO("Pro", ChatColor.GOLD, new String[] {"bawz.pro"}),
	CHALLENGER("Challenger", ChatColor.AQUA, new String[] {"bawz.challenger"}),
	PLAYER("Player", ChatColor.GRAY, new String[] {}),;
	
	private String displayName;
	private ChatColor color;
	private String[] permissions;
	
	Rank(final String displayName, final ChatColor color, final String[] permissions) {
		this.displayName = displayName;
		this.color = color;
		this.permissions = permissions;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String[] getPermissions() {
		return permissions;
	}
	
    public static Rank getRankByName(final String name) {
        for (final Rank rank : values()) {
            if (rank.getDisplayName().toLowerCase().equals(name.toLowerCase())) {
                return rank;
            }
        }
        return null;
    }
	
}