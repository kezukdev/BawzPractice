package kezuk.practice.core.tip;

import org.bukkit.Bukkit;
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
					Bukkit.broadcastMessage("[TIP FIRST]");
				}
				if (cooldown == 2) {
					Bukkit.broadcastMessage("[TIP SECOND]");
				}
				if (cooldown == 3) {
					Bukkit.broadcastMessage("[TIP THIRD]");
					cooldown = 0;
				}
			}
		}.runTaskTimerAsynchronously(Practice.getInstance(), 1200L*5L, 1200L*5L);
	}

}
