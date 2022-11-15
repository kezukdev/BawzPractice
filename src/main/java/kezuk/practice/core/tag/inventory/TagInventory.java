package kezuk.practice.core.tag.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kezuk.practice.core.tag.Tag;
import kezuk.practice.core.tag.TagType;
import kezuk.practice.utils.ItemSerializer;

public class TagInventory {
	
    private Inventory tagTypeInventory;
    private Inventory tagClassicInventory;
    private Inventory tagJapanInventory;
    private Inventory tagRegionInventory;
	
	public TagInventory() {
        this.tagTypeInventory = Bukkit.createInventory((InventoryHolder)null, InventoryType.HOPPER, ChatColor.DARK_GRAY + "Tag Type Selector:");
        this.tagClassicInventory = Bukkit.createInventory((InventoryHolder)null, 9, ChatColor.DARK_GRAY + "Tag Classic Selector:");
        this.tagJapanInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Tag Japan Selector:");
        this.tagRegionInventory = Bukkit.createInventory((InventoryHolder)null, 18, ChatColor.DARK_GRAY + "Tag Region Selector:");
        this.setTagTypeInventory();
        this.setTagClassicInventory();
        this.setTagJapanInventory();
        this.setTagRegionInventory();
	}
	
    private void setTagTypeInventory() {
    	this.tagTypeInventory.clear();
        final ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
		final ItemStack classic = ItemSerializer.serialize(new ItemStack(Material.NAME_TAG),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Classic Tag");
		final ItemStack japan = ItemSerializer.serialize(new ItemStack(Material.BOOK),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Japan Tag");
		final ItemStack region = ItemSerializer.serialize(new ItemStack(Material.COMPASS),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Region Tag");
		final ItemStack reset = ItemSerializer.serialize(new ItemStack(Material.BUCKET),(short) 0, ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Reset Tag");
		this.tagTypeInventory.setItem(0, classic);
		this.tagTypeInventory.setItem(1, japan);
		this.tagTypeInventory.setItem(2, region);
		this.tagTypeInventory.setItem(3, glass);
		this.tagTypeInventory.setItem(4, reset);
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
	
	public Inventory getTagClassicInventory() {
		return tagClassicInventory;
	}
	
	public Inventory getTagJapanInventory() {
		return tagJapanInventory;
	}
	
	public Inventory getTagRegionInventory() {
		return tagRegionInventory;
	}
	
	public Inventory getTagTypeInventory() {
		return tagTypeInventory;
	}

}
