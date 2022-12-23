package kezuk.practice.player.personnal.subinventory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.Profile;
import kezuk.practice.player.cache.StatisticsCache;
import kezuk.practice.utils.ItemSerializer;

public class StatisticsInventory {

	private Inventory stats;
	private UUID uuid;
	private String[] lore = new String[99];
	
	public StatisticsInventory(final UUID uuid) {
		this.uuid = uuid;
		this.stats = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_GRAY + Bukkit.getOfflinePlayer(uuid).getName() + "'s statistics: ");
		this.setStatsInventory();
	}
	
	public void setStatsInventory() {
		final StatisticsCache h = Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getHistoric();
		final String ranked = ChatColor.AQUA + "Win " + ChatColor.GRAY + "(" + ChatColor.WHITE + h.getRankedWin() + ChatColor.GRAY + ") │ " + ChatColor.AQUA + "Played " +ChatColor.GRAY + "(" + ChatColor.WHITE + h.getRankedPlayed() + ChatColor.GRAY + ")";
		final String unranked = ChatColor.AQUA + "Win " + ChatColor.GRAY + "(" + ChatColor.WHITE + h.getUnrankedWin() + ChatColor.GRAY + ") │ " + ChatColor.AQUA + "Played " +ChatColor.GRAY + "(" + ChatColor.WHITE + h.getUnrankedPlayed() + ChatColor.GRAY + ")";
		final ItemStack matchPlayed = ItemSerializer.serialize(new ItemStack(Material.EXP_BOTTLE), (short)0, ChatColor.DARK_GRAY + " » " + ChatColor.DARK_AQUA + "Match Statistics", Arrays.asList("", ChatColor.GRAY + " * " + ChatColor.LIGHT_PURPLE + "Ranked" + ChatColor.GRAY + " * ", ranked, "", ChatColor.GRAY + " * " + ChatColor.YELLOW + "Casual" + ChatColor.GRAY + " * ", unranked));
		final ItemStack eloStatistics = ItemSerializer.serialize(new ItemStack(Material.FISHING_ROD), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Elo Statistics", null);
		if (h.getUnrankedPlayed() + h.getRankedPlayed() != 0) {
			int[] eloRival = new int[Practice.getInstance().getLadder().size()];
			try {
				eloRival = Profile.getSplitValue(DB.getFirstRow("SELECT elos FROM playersdata WHERE name=?", h.getAgainst()).getString("elos"), ":");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			final ItemStack lastMatch = ItemSerializer.serialize(new ItemStack(Material.BOOK), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Last Match:", Arrays.asList("", ChatColor.GRAY + " * " + ChatColor.WHITE + " Opponents: " + ChatColor.AQUA + h.getAgainst() + (h.getRanked() ? ChatColor.GRAY + " (" + ChatColor.DARK_AQUA + eloRival[Ladders.getLadder(h.getLastPlayedLadder()).id()] + ChatColor.GRAY + ") ": ""), "    " + this.getLastMatchStats(h.getAgainst(), h.getRanked()), "", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.GRAY + ": " + ChatColor.WHITE + ChatColor.stripColor(h.getLastPlayedLadder())));
			this.stats.setItem(4, lastMatch);
		}
		this.stats.setItem(0, matchPlayed);
		this.stats.setItem(1, eloStatistics);
		//final ItemMeta item =this.stats.getItem(1).getItemMeta();
		//for (Ladders ladder : Practice.getInstance().getLadder()) {
			//if (ladder.isRanked()) {
				//lore[ladder.id()] = ladder.displayName() + ChatColor.WHITE + ": " + Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getElos()[ladder.id()];	
			//}
		//}
		//item.setLore(lore);//
		//this.stats.getItem(1).setItemMeta(item);//
	}
	
	public Inventory getStats() {
		return stats;
	}
	
	private String getLastMatchStats(final String name, final Boolean ranked) {
		String displayer = null;
		try {
			if (ranked) {
				int rankedPlayed = DB.getFirstRow("SELECT rankedPlayed FROM playersdata WHERE name=?", name).getInt("rankedPlayed");	
				int rankedWin = DB.getFirstRow("SELECT rankedWin FROM playersdata WHERE name=?", name).getInt("rankedWin");
				int rankedLose = rankedPlayed - rankedWin;
				displayer = ChatColor.LIGHT_PURPLE + "Ranked" + ChatColor.GRAY + ": " + ChatColor.GREEN + rankedWin + ChatColor.GRAY + "/" + ChatColor.RED + rankedLose;
				return displayer;
			}
			int unrankedPlayed = DB.getFirstRow("SELECT unrankedPlayed FROM playersdata WHERE name=?", name).getInt("unrankedPlayed");	
			int unrankedWin = DB.getFirstRow("SELECT unrankedWin FROM playersdata WHERE name=?", name).getInt("unrankedWin");
			int unrankedLose = unrankedPlayed - unrankedWin;
			displayer = ChatColor.YELLOW + "Casual" + ChatColor.GRAY + ": " + ChatColor.GREEN + unrankedWin + ChatColor.GRAY + "/" + ChatColor.RED + unrankedLose;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return displayer;
	}
	
}
