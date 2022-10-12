package kezuk.bawz.manager;

import java.util.Arrays;

import org.bukkit.*;
import org.bukkit.inventory.*;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.core.Tag;
import kezuk.bawz.core.TagType;
import kezuk.bawz.ladders.*;
import kezuk.bawz.utils.*;

public class InventoryManager {
	
    private Inventory personnalInventory;
    private Inventory utilsInventory;
    private Inventory unrankedInventory;
    private Inventory rankedInventory;
    private Inventory duelInventory;
    private Inventory tagTypeInventory;
    private Inventory tagClassicInventory;
    private Inventory tagJapanInventory;
    private Inventory tagRegionInventory;
    private Inventory editableInventory;
	private Inventory editorInventory;
	private Inventory ladderInventory;
    private Inventory startHostInventory;
    private MultipageSerializer spectateInventory;
    
    public InventoryManager() {
        this.personnalInventory = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.DARK_GRAY + "Personnal Management:");
        this.utilsInventory = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.DARK_GRAY + "Utils:");
        this.unrankedInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Unranked:");
        this.rankedInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Ranked:");
    	this.duelInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Duel:");
        this.tagTypeInventory = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.DARK_GRAY + "Tag Type Selector:");
        this.tagClassicInventory = Bukkit.createInventory((InventoryHolder)null, 9, ChatColor.DARK_GRAY + "Tag Classic Selector:");
        this.tagJapanInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Tag Japan Selector:");
        this.tagRegionInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Tag Region Selector:");
        this.editableInventory = Bukkit.createInventory((InventoryHolder)null, 9, ChatColor.DARK_GRAY + "Choose ladder to edit:");
		this.editorInventory = Bukkit.createInventory(null, 45, ChatColor.DARK_GRAY + "Editor:");
		this.ladderInventory = Bukkit.createInventory(null, 36, ChatColor.DARK_GRAY + "Ladder:");
        this.spectateInventory = new MultipageSerializer(Lists.newArrayList(), ChatColor.DARK_GRAY + "Spectate", ItemSerializer.serialize(new ItemStack(Material.COMPASS), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Current Fight" + ChatColor.GRAY + " * "));
        this.startHostInventory = Bukkit.createInventory((InventoryHolder)null, 9, ChatColor.DARK_GRAY + "Host:");
        this.setPersonnalInventory();
        this.setUtilsInventory();
        this.setUnrankedInventory();
        this.setDuelInventory();
        this.setRankedInventory();
        this.setTagTypeInventory();
        this.setTagClassicInventory();
        this.setTagJapanInventory();
        this.setTagRegionInventory();
        this.setEditorInventory();
        this.setStartHostInventory();
    }

	public void setDuelInventory() {
    	this.duelInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName());
    		this.duelInventory.addItem(item);
    	}
	}

	private void setTagClassicInventory() {
		this.tagClassicInventory.clear();
		for (Tag tag : Tag.values()) {
			if (tag.getTagType().equals(TagType.CLASSIC)) {
	    		final ItemStack item = ItemSerializer.serialize(new ItemStack(tag.getIcon()), (short)0, tag.getColor() + tag.getDisplay());
        		this.tagClassicInventory.addItem(item);
			}
		}
	}

	private void setTagJapanInventory() {
		this.tagJapanInventory.clear();
		for (Tag tag : Tag.values()) {
			if (tag.getTagType().equals(TagType.JAPAN)) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(tag.getIcon()), (short)0, tag.getColor() + tag.getDisplay());
        		this.tagJapanInventory.addItem(item);
			}
		}
	}

	private void setTagRegionInventory() {
		this.tagRegionInventory.clear();
		for (Tag tag : Tag.values()) {
			if (tag.getTagType().equals(TagType.REGION)) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(tag.getIcon()), (short)0, tag.getColor() + tag.getDisplay());
        		this.tagRegionInventory.addItem(item);
			}
		}
	}

	private void setStartHostInventory() {
    	this.startHostInventory.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 9; ++i) {
            this.startHostInventory.setItem(i, glass);
        }
        final ItemStack size25 = ItemSerializer.serialize(new ItemStack(Material.ANVIL), (short)0, ChatColor.AQUA + "25 Players Size");
        final ItemStack size50 = ItemSerializer.serialize(new ItemStack(Material.NETHER_STAR), (short)0, ChatColor.DARK_AQUA + "50 Players Size");
        final ItemStack size75 = ItemSerializer.serialize(new ItemStack(Material.BEACON), (short)0, ChatColor.AQUA + "75 Players Size");
        this.startHostInventory.setItem(2, size25);
        this.startHostInventory.setItem(4, size50);
        this.startHostInventory.setItem(6, size75);
    }
    
    private void setEditorInventory() {
    	this.editableInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		if (ladder.isAlterable()) {
        		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName(), null);
        		this.editableInventory.addItem(item);
    		}
    	}
    }
    
    private void setTagTypeInventory() {
    	this.tagTypeInventory.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 27; ++i) {
            this.tagTypeInventory.setItem(i, glass);
        }
		final ItemStack classic = ItemSerializer.serialize(new ItemStack(Material.NAME_TAG),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Classic Tag");
		final ItemStack japan = ItemSerializer.serialize(new ItemStack(Material.BOOK),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Japan Tag");
		final ItemStack region = ItemSerializer.serialize(new ItemStack(Material.COMPASS),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Region Tag");
		final ItemStack reset = ItemSerializer.serialize(new ItemStack(Material.BUCKET),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Reset Tag");
		this.tagTypeInventory.setItem(11, classic);
		this.tagTypeInventory.setItem(13, japan);
		this.tagTypeInventory.setItem(15, region);
		this.tagTypeInventory.setItem(26, reset);
	}

	private void setPersonnalInventory() {
		this.personnalInventory.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 27; ++i) {
            this.personnalInventory.setItem(i, glass);
        }
        final ItemStack stats = ItemSerializer.serialize(new ItemStack(Material.CAULDRON_ITEM),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Statistics", Arrays.asList(new String(" " + MessageSerializer.LORE_STATS)));
        final ItemStack tag = ItemSerializer.serialize(new ItemStack(Material.NAME_TAG),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Tag", Arrays.asList(new String(" " + MessageSerializer.LORE_TAGS)));
        final ItemStack settings = ItemSerializer.serialize(new ItemStack(Material.DAYLIGHT_DETECTOR),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Settings", Arrays.asList(new String(" " + MessageSerializer.LORE_SETTINGS)));
        this.personnalInventory.setItem(12, stats);
        this.personnalInventory.setItem(13, tag);
        this.personnalInventory.setItem(14, settings);
    }
    
    private void setUtilsInventory() {
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        for (int i = 0; i < 27; ++i) {
            this.utilsInventory.setItem(i, glass);
        }
        final ItemStack party = ItemSerializer.serialize(new ItemStack(Material.CAKE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Party", Arrays.asList(new String(" " + MessageSerializer.LORE_PARTY)));
        final ItemStack tourney = ItemSerializer.serialize(new ItemStack(Material.FERMENTED_SPIDER_EYE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Tournaments (BETA)", Arrays.asList(new String(" " + MessageSerializer.LORE_TOURNEY)));
        final ItemStack host = ItemSerializer.serialize(new ItemStack(Material.PAPER),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Host (BETA)", Arrays.asList(new String(" " + MessageSerializer.LORE_HOST)));
        final ItemStack leaderboard = ItemSerializer.serialize(new ItemStack(Material.DIAMOND),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Leaderboard", Arrays.asList(new String(" " + MessageSerializer.LORE_LEADERBOARD)));
        final ItemStack spectate = ItemSerializer.serialize(new ItemStack(Material.ENDER_PORTAL),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Spectate", Arrays.asList(new String(" " + MessageSerializer.LORE_SPECTATE)));
        final ItemStack editor = ItemSerializer.serialize(new ItemStack(Material.ENCHANTMENT_TABLE),(short) 0, ChatColor.GRAY + "» " + ChatColor.DARK_AQUA + "Editor", Arrays.asList(new String(" " + MessageSerializer.LORE_EDITOR)));
        this.utilsInventory.setItem(4, party);
        this.utilsInventory.setItem(10, tourney);
        this.utilsInventory.setItem(12, host);
        this.utilsInventory.setItem(22, leaderboard);
        this.utilsInventory.setItem(14, spectate);
        this.utilsInventory.setItem(16, editor);
    }
    
    private void setUnrankedInventory() {
    	this.unrankedInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName(), Arrays.asList(new String(ChatColor.GRAY + " * " + ChatColor.AQUA + "Waiting" + ChatColor.RESET + ": " + ChatColor.DARK_AQUA + Practice.getInstance().getQueueManager().getQueuedFromLadder(ladder, false))));
    		this.unrankedInventory.addItem(item);
    	}
    }
    
    private void setRankedInventory() {
    	this.rankedInventory.clear();
    	for (Ladders ladder : Practice.getInstance().getLadder()) {
    		final ItemStack item = ItemSerializer.serialize(new ItemStack(ladder.material()), ladder.data(), ladder.displayName(), Arrays.asList(new String(ChatColor.GRAY + " * " + ChatColor.AQUA + "Waiting" + ChatColor.RESET + ": " + ChatColor.DARK_AQUA + Practice.getInstance().getQueueManager().getQueuedFromLadder(ladder, true))));
    		this.rankedInventory.addItem(item);
    	}
    }
    
    public void updateQueueInventory(final boolean ranked) {
        if (!ranked) {
        	this.unrankedInventory.clear();
        	this.setUnrankedInventory();
        	return;
        }
        this.rankedInventory.clear();
        this.setRankedInventory();
    }
    
    public Inventory getStartHostInventory() {
		return startHostInventory;
	}
    
    public MultipageSerializer getSpectateInventory() {
		return spectateInventory;
	}
    
    public Inventory getEditableInventory() {
		return editableInventory;
	}
    
    public Inventory getTagTypeInventory() {
		return tagTypeInventory;
	}
    
    public Inventory getTagClassicInventory() {
		return tagClassicInventory;
	}
    
    public Inventory getTagJapanInventory() {
		return tagJapanInventory;
	}
    
    public Inventory getTagRegionInventory() {
		return tagRegionInventory;
	}
    
    public Inventory getUtilsInventory() {
		return utilsInventory;
	}
    
    public Inventory getPersonnalInventory() {
		return personnalInventory;
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
    
    public Inventory getLadderInventory() {
		return ladderInventory;
	}
}
