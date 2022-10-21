package kezuk.practice.match.inventory;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.utils.ItemSerializer;
import kezuk.practice.utils.GameUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class MatchSeeInventory
{
    private ItemStack[] content;
    private ItemStack[] armor;
    private String life;
    private double food;
    private int hits;
    private int longestCombo;
    private int missedPots;
    private String ownerName;
    private UUID opponent;
    private String opponentName;
    private int amountPot;
    private int amountSoup;
    private int thrownPotions;
    private Ladders ladder;
    
    public MatchSeeInventory(final UUID uuid) {
    	final Player player = Bukkit.getPlayer(uuid);
        this.content = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.hits = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getMatchStats().getHits();
        this.longestCombo = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getMatchStats().getLongestCombo();
        this.missedPots = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getMatchStats().getMissedPotions();
        this.thrownPotions = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getMatchStats().getThrownPotions();
        double remainLife = player.getHealth() / 2;
        NumberFormat nf = new DecimalFormat("0.#");
        this.life = nf.format(remainLife);
        this.food = (float) player.getFoodLevel();
        this.ownerName = player.getName();
        this.opponentName = Bukkit.getServer().getPlayer(GameUtils.getOpponent(player.getUniqueId())).getName();
        this.opponent = GameUtils.getOpponent(player.getUniqueId());
        this.amountPot = (player.getInventory().contains(new ItemStack(Material.POTION, 1, (short)16421)) ? player.getInventory().all(new ItemStack(Material.POTION, 1, (short)16421)).size() : 0);
        this.amountSoup = (player.getInventory().contains(new ItemStack(Material.MUSHROOM_SOUP)) ? player.getInventory().all(new ItemStack(Material.MUSHROOM_SOUP)).size() : 0);
        this.ladder = Practice.getInstance().getRegisterCollections().getMatchs().get(Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getMatchUUID()).getLadder();
        Practice.getInstance().getRegisterCollections().getOfflineInventories().put(player.getUniqueId(), this);
    }
    
    public Inventory getPreviewInventory() {
        final Inventory preview = Bukkit.createInventory((InventoryHolder)null, 54, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Preview of " + ChatColor.AQUA + this.ownerName);
        preview.setContents(this.content);
        preview.setItem(36, this.armor[3]);
        preview.setItem(37, this.armor[2]);
        preview.setItem(38, this.armor[1]);
        preview.setItem(39, this.armor[0]);
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 45; i < 54; ++i) {
            preview.setItem(i, glass);
        }
        preview.setItem(45, ItemSerializer.serialize(new ItemStack(Material.PISTON_STICKY_BASE), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Go to preview of " + ChatColor.AQUA + opponentName + ChatColor.GRAY + " * ", Arrays.asList(new String[] {" ",ChatColor.GRAY.toString() + opponent})));
        preview.setItem(48, ItemSerializer.serialize(new ItemStack(Material.MELON), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Player Statistics" + ChatColor.RESET + ": ", Arrays.asList(new String[] {ChatColor.DARK_AQUA  + "Food Level" + ChatColor.RESET + ": " + this.food, ChatColor.DARK_AQUA + "Life Level" + ChatColor.RESET + ": " + this.life + ChatColor.DARK_RED + "\u2665"})));
        if (this.ladder.displayName().equals(ChatColor.DARK_AQUA + "NoDebuff")|| this.ladder.displayName().equals(ChatColor.DARK_AQUA + "Debuff") || this.ladder.displayName().equals(ChatColor.DARK_AQUA + "NoEnchant")) {
            preview.setItem(49, ItemSerializer.serialize(new ItemStack(Material.ANVIL), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Match Statistics" + ChatColor.RESET + ": ", Arrays.asList(new String[]{ChatColor.DARK_AQUA.toString() + "Hits" + ChatColor.RESET + ": " + this.hits, ChatColor.DARK_AQUA + "Longest Combo" + ChatColor.RESET + ": " + this.longestCombo})));
            preview.setItem(50, ItemSerializer.serialize(new ItemStack(Material.POTION), (short)16421, ChatColor.GRAY + " * " + ChatColor.AQUA + "Potions" + ChatColor.RESET + ": ", Arrays.asList(new String[] {ChatColor.DARK_AQUA.toString() + "Remaining" + ChatColor.RESET + ": " + this.amountPot, ChatColor.DARK_AQUA + "Missed Potions" + ChatColor.RESET + ": " + this.missedPots, ChatColor.DARK_AQUA + "Accuracy" + ChatColor.RESET + ": " + getAccuracy() + "%"})));
        }
        else if (this.ladder.displayName().equals(ChatColor.DARK_AQUA + "Soup") || this.ladder.displayName().equals(ChatColor.DARK_AQUA + "MCSG")) {
            preview.setItem(49, ItemSerializer.serialize(new ItemStack(Material.ANVIL), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Match Statistics" + ChatColor.RESET + ": ", Arrays.asList(new String[]{ChatColor.DARK_AQUA.toString() + "Hits" + ChatColor.RESET + ": " + this.hits, ChatColor.DARK_AQUA + "Longest Combo" + ChatColor.RESET + ": " + this.longestCombo})));
            preview.setItem(50, ItemSerializer.serialize(new ItemStack(Material.BROWN_MUSHROOM), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Soups" + ChatColor.RESET + ": ", Arrays.asList(new String[] {ChatColor.DARK_AQUA.toString() + "Remaining" + ChatColor.RESET + ": " + this.amountSoup})));
        }
        else {
            preview.setItem(50, ItemSerializer.serialize(new ItemStack(Material.ANVIL), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Match Statistics" + ChatColor.RESET + ": ", Arrays.asList(new String[]{ChatColor.DARK_AQUA.toString() + "Hits" + ChatColor.RESET + ": " + this.hits, ChatColor.DARK_AQUA + "Longest Combo" + ChatColor.RESET + ": " + this.longestCombo})));
        }
        preview.setItem(53, ItemSerializer.serialize(new ItemStack(Material.PISTON_STICKY_BASE), (short)0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Go to preview of " + ChatColor.AQUA + opponentName + ChatColor.GRAY + " * ", Arrays.asList(new String[] {" " , ChatColor.GRAY.toString() + opponent})));
        return preview;
    }
    
    public double getAccuracy() {
		if (this.missedPots == 0) {
			return 100.0;
		} else if (this.thrownPotions == this.missedPots) {
			return 50.0;
		}

		return Math.round(100.0D - (((double) this.missedPots / (double) this.thrownPotions) * 100.0D));
    }
}
