package kezuk.bawz.core;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Tag {
	
	QUATRE_CENT_VINGT("420", ChatColor.GREEN, new ItemStack(Material.NAME_TAG), TagType.CLASSIC),
	VAPE("Vape", ChatColor.DARK_AQUA, new ItemStack(Material.STRING), TagType.CLASSIC),
	BESTWW("BestWW", ChatColor.AQUA, new ItemStack(Material.BEACON), TagType.CLASSIC),
	CPS("CPS", ChatColor.LIGHT_PURPLE, new ItemStack(Material.IRON_SWORD), TagType.CLASSIC),
	BAWZ("Bawz", ChatColor.RED, new ItemStack(Material.CAKE), TagType.CLASSIC),
	BERRY("Berry", ChatColor.GOLD, new ItemStack(Material.PAPER), TagType.JAPAN),
	CHOPPER("Chopper", ChatColor.YELLOW, new ItemStack(Material.MONSTER_EGG), TagType.JAPAN),
	ZORO("Zoro", ChatColor.DARK_GREEN, new ItemStack(Material.DIAMOND_SWORD), TagType.JAPAN),
	LUFFY("Luffy", ChatColor.GOLD, new ItemStack(Material.GOLD_HELMET), TagType.JAPAN),
	KORO("Koro", ChatColor.YELLOW, new ItemStack(Material.FEATHER), TagType.JAPAN),
	SENSEI("Sensei", ChatColor.LIGHT_PURPLE, new ItemStack(Material.BOOK_AND_QUILL), TagType.JAPAN),
	SAMA("Sama", ChatColor.RED, new ItemStack(Material.FERMENTED_SPIDER_EYE), TagType.JAPAN),
	KUN("Kun", ChatColor.AQUA, new ItemStack(Material.EGG), TagType.JAPAN),
	JOYBOY("JoyBoy", ChatColor.YELLOW, new ItemStack(Material.SKULL), TagType.JAPAN),
	FR("French", ChatColor.DARK_AQUA, new ItemStack(Material.BREAD), TagType.REGION),
	UK("United-Kingdom", ChatColor.BLUE, new ItemStack(Material.APPLE), TagType.REGION),
	US("United-States", ChatColor.WHITE, new ItemStack(Material.BLAZE_ROD), TagType.REGION),
	QC("Quebec", ChatColor.DARK_BLUE, new ItemStack(Material.BOAT), TagType.REGION),
	CAD("Canada", ChatColor.RED, new ItemStack(Material.ANVIL), TagType.REGION),
	CH("Swiss", ChatColor.DARK_RED, new ItemStack(Material.COCOA), TagType.REGION),
	AS("Asia", ChatColor.DARK_PURPLE, new ItemStack(Material.REDSTONE), TagType.REGION),
	RU("Russian", ChatColor.BLUE, new ItemStack(Material.POTION), TagType.REGION),
	SA("South-America", ChatColor.YELLOW, new ItemStack(Material.BEDROCK), TagType.REGION),
	MA("Maroc", ChatColor.DARK_RED, new ItemStack(Material.DIAMOND), TagType.REGION),
	AL("Algeria", ChatColor.DARK_GREEN, new ItemStack(Material.EMERALD), TagType.REGION),
	NORMAL("Normal", ChatColor.GRAY, new ItemStack(Material.BUCKET), TagType.MAIN);
	
	private String display;
	private ChatColor color;
	private ItemStack icon;
	private TagType tagType;
	
	Tag(final String display, final ChatColor color, final ItemStack icon, final TagType tagType) {
		this.display = display;
		this.color = color;
		this.icon = icon;
		this.tagType = tagType;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public void setColor(ChatColor color) {
		this.color = color;
	}
	
	public TagType getTagType() {
		return tagType;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
    public static Tag getTagByName(final String name) {
        for (final Tag tag : values()) {
            if (tag.getDisplay().toLowerCase().equals(name.toLowerCase())) {
                return tag;
            }
        }
        return null;
    }

}
