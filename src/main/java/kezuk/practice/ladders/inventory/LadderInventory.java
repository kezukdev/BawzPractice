package kezuk.practice.ladders.inventory;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.queue.QueueSystem.QueueEntry;
import kezuk.practice.utils.ItemSerializer;

public class LadderInventory {
	
	private Inventory queueInventory;
	private Inventory unrankedInventory;
	private Inventory rankedInventory;
	private Inventory duelInventory;
	
	private Inventory ffaInventory;
	private Inventory splitInventory;
	private Inventory unrankedPartyInventory;
	
	private Inventory editorInventory;
	
	private Inventory tournamentInventory;
	
	public LadderInventory() {
		this.queueInventory = Bukkit.createInventory(null, InventoryType.FURNACE, ChatColor.DARK_GRAY + "Queue Selection:");
		this.unrankedInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Unranked Selection:");
		this.rankedInventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_GRAY + "Ranked Selection:");
		this.duelInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Duel Gametype?");
		
		this.ffaInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "FFA Selection:");
		this.splitInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Split Selection:");
		this.unrankedPartyInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "2v2 Selection:");
		
		this.editorInventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_GRAY + "Editor Selection:");
		this.tournamentInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Tournament Gametype Selection:");
		this.setPreviewInventory();
	}

	private void setPreviewInventory() {
		this.queueInventory.clear();
		final ItemStack unranked = ItemSerializer.serialize(new ItemStack(Material.STONE_SWORD), (short) 0, ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "Casual", Arrays.asList(ChatColor.WHITE + "To practice before you show your real skills in ranked!", "", ChatColor.GRAY + " * " + ChatColor.WHITE + "Waiting: " + ChatColor.AQUA + getQueued(false), "", ChatColor.DARK_GRAY + "(Middle-Click) Currently in queue."));
		final ItemStack duos = ItemSerializer.serialize(new ItemStack(Material.GOLD_SWORD), (short) 0, ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "Duos", Arrays.asList(ChatColor.WHITE + "Play with a friend of yours or a random person in two on two!", "", ChatColor.GRAY + " * " + ChatColor.WHITE + "Waiting: " + ChatColor.AQUA + getQueued(false), "", ChatColor.DARK_GRAY + "(Middle-Click) Currently in queue."));
		final ItemStack ranked = ItemSerializer.serialize(new ItemStack(Material.DIAMOND_SWORD), (short) 0, ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "Ranked", Arrays.asList(ChatColor.WHITE + "Move up the ranking and show who has the best level!", "", ChatColor.GRAY + " * " + ChatColor.WHITE + "Waiting: " + ChatColor.AQUA + getQueued(true), "", ChatColor.DARK_GRAY + "(Middle-Click) Currently in queue."));
		this.queueInventory.setItem(0, unranked);
		this.queueInventory.setItem(1, duos);
		this.queueInventory.setItem(2, ranked);
    	this.unrankedInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (!ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName(), Arrays.asList(new String[] {ChatColor.GRAY + " * " + ChatColor.WHITE + "Waiting: " + ChatColor.DARK_AQUA + getQueuedFromLadder(ladder, false, false), "", ChatColor.DARK_GRAY + "(Shift-Click) Edit this ladder."}));
        		this.unrankedInventory.addItem(item);	
    		}
    	}
    	this.rankedInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (ladder.isRanked() && !ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName(), Arrays.asList(new String[] {ChatColor.GRAY + " * " + ChatColor.WHITE + "Waiting: " + ChatColor.DARK_AQUA + getQueuedFromLadder(ladder, true, false), "", ChatColor.DARK_GRAY + "(Shift-Click) Edit this ladder."}));
        		this.rankedInventory.addItem(item);	
    		}
    	}
    	this.duelInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (!ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
        		this.duelInventory.addItem(item);	
    		}
    	}
    	this.ffaInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (!ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
        		this.ffaInventory.addItem(item);	
    		}
    	}
    	this.splitInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (!ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
        		this.splitInventory.addItem(item);	
    		}
    	}
		this.unrankedPartyInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (!ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName(), Arrays.asList(new String(ChatColor.GRAY + " * " + ChatColor.WHITE + "Waiting: " + ChatColor.DARK_AQUA + getQueuedFromLadder(ladder, false, false))));
        		this.unrankedPartyInventory.addItem(item);	
    		}
    	}
    	this.editorInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (ladder.isAlterable() && !ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
        		this.editorInventory.addItem(item);
    		}
    	}
    	this.tournamentInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (!ladder.privateGame()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
        		this.tournamentInventory.addItem(item);
    		}
    	}
	}
	
	public int getQueuedFromLadder(Ladders ladder, boolean ranked, boolean to2) {
		int count = 0;
		for (Map.Entry<UUID, QueueEntry> map : Practice.getInstance().getRegisterCollections().getQueue().entrySet()) {
			QueueEntry value = map.getValue();
			if (value.getLadder() == ladder && value.isRanked() == ranked && value.isTo2() == to2) {
				count++;
			}
		}
		return count;
	}
	
	public int getQueued(boolean ranked) {
		int count = 0;
		for (Map.Entry<UUID, QueueEntry> map : Practice.getInstance().getRegisterCollections().getQueue().entrySet()) {
			QueueEntry value = map.getValue();
			if (value.isRanked() == ranked) {
				count++;
			}
		}
		return count;
	}
	
	public Inventory getQueueInventory() {
		return queueInventory;
	}
	
	public void refreshInventory() {
		this.setPreviewInventory();
	}
	
	public Inventory getUnrankedInventory() {
		return unrankedInventory;
	}
	
	public Inventory getRankedInventory() {
		return rankedInventory;
	}
	
	public Inventory getDuelInventory() {
		return duelInventory;
	}
	
	public Inventory getEditorInventory() {
		return editorInventory;
	}
	
	public Inventory getFfaInventory() {
		return ffaInventory;
	}
	
	public Inventory getSplitInventory() {
		return splitInventory;
	}
	
	public Inventory getUnrankedPartyInventory() {
		return unrankedPartyInventory;
	}
	
	public Inventory getTournamentInventory() {
		return tournamentInventory;
	}
}
