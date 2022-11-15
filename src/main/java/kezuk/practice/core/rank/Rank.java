package kezuk.practice.core.rank;

import org.bukkit.ChatColor;

public enum Rank {
	
	FOUNDER("Founder", ChatColor.DARK_RED, new String[] {"bawz.rank", "bukkit.command.ban.player", "verus.staff", "bawz.bypass", "bawz.arena", "bawz.manage.player", "bawz.pro", "bawz.challenger", "bawz.strong", "bawz.moderation", "bawz.admin"}),
	STAFF("Staff", ChatColor.GOLD, new String[] {"bukkit.command.ban.player", "bukkit.command.kick", "bukkit.command.teleport", "bukkit.command.tp", "verus.staff.alerts", "verus.staff.logs", "verus.staff.logs.recent", "bawz.moderation"}),
	BUILDER("Builder", ChatColor.DARK_GREEN, new String[] {"bukkit.command.gamemode", "bawz.build", "bukkit.command.tp"}),
	TRIAL("Trial", ChatColor.YELLOW, new String[] {"bukkit.command.tp", "bukkit.command.kick", "verus.staff.alerts", "verus.staff.logs", "verus.staff.logs.recent", "bawz.moderation"}),
	MEDIA("Media", ChatColor.LIGHT_PURPLE, new String[] {"bawz.host", "bawz.tournament"}),
	AK_47("AK-47", ChatColor.DARK_AQUA, new String[] {"bawz.host", "bawz.tournament"}),
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