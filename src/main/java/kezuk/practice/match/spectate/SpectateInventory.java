package kezuk.practice.match.spectate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.match.StartMatch;
import kezuk.practice.utils.ItemSerializer;
import kezuk.practice.utils.MatchUtils;
import kezuk.practice.utils.MultipageSerializer;

public class SpectateInventory {
	
    private MultipageSerializer spectateInventory;
    private List<ItemStack> matchs = Lists.newArrayList();
    
    public SpectateInventory() {
        this.spectateInventory = new MultipageSerializer(Lists.newArrayList(), ChatColor.DARK_GRAY + "Spectate", ItemSerializer.serialize(new ItemStack(Material.COMPASS), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Current Fight" + ChatColor.GRAY + " * "));
        this.setInventory();
    }

	private void setInventory() {
        for (StartMatch matchManager : Practice.getInstance().getRegisterCollections().getMatchs().values()) {
        	for (UUID uuid : matchManager.getFirstList()) {
        		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getLadder().displayName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Type" + ChatColor.RESET + ": " + ChatColor.AQUA + (matchManager.isRanked() ? "Ranked" : "Unranked"), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Arena" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getArena().getName(), " "};
                ItemStack item = ItemSerializer.serialize(new ItemStack(matchManager.getLadder().material()), matchManager.getLadder().data(), ChatColor.GREEN + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.AQUA + " vs " + ChatColor.RED + Bukkit.getServer().getPlayer(MatchUtils.getOpponent(uuid)).getName(), Arrays.asList(lore));
                matchs.add(item);
        	}
        }
        this.spectateInventory.refresh(matchs);
	}
	
	public MultipageSerializer getSpectateInventory() {
        this.spectateInventory.refresh(matchs);
		return spectateInventory;
	}
}
