package kezuk.practice.core.tip;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;

public class TipSchedule {
	
	private int cooldown;
	
	public TipSchedule() {
		cooldown = 0;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				cooldown++;
				if (cooldown == 1) {
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "Be aware that all methods used to make you better at the game are strictly forbidden and punished " + ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + "Lag" + ChatColor.GRAY + "," + ChatColor.WHITE + " Cheat" + ChatColor.GRAY + "," + ChatColor.WHITE + " Autoclick..." + ChatColor.DARK_GRAY +")");
				}
				if (cooldown == 2) {
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "Don't hesitate to join our discord to follow all our instances!" + ChatColor.WHITE + " discord.gg/bawz");
				}
				if (cooldown == 3) {
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "Prioritize the ranked to show who will be the best by climbing the top rankings!");
					cooldown = 0;
				}
			}
		}.runTaskTimerAsynchronously(Practice.getInstance(), 1200L*5L, 1200L*5L);
	}

}
