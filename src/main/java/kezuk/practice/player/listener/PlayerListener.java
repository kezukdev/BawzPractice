package kezuk.practice.player.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import kezuk.practice.Practice;
import kezuk.practice.core.rank.Rank;
import kezuk.practice.core.tag.Tag;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoining(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		final Profile profile = new Profile(event.getPlayer().getUniqueId());
		final Player player = event.getPlayer();
		new SpawnItems(player.getUniqueId(), false);
		player.teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
		player.setFoodLevel(20);
		player.setHealth(event.getPlayer().getMaxHealth());
		player.setSaturation(20.0f);
		for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
			event.getPlayer().removePotionEffect(effect.getType());
		}
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.hasPermission("bawz.moderation") && (profile.getPlayerCache().getStaffCache() != null && profile.getPlayerCache().getStaffCache().isVanish())) {
				player.hidePlayer(players);
			}
		}
		event.getPlayer().setPlayerListName(profile.getRank().getColor() + player.getName());
	}
	
	@EventHandler
	public void onQuitting(final PlayerQuitEvent event) {
		GameUtils.addDisconnected(event.getPlayer().getUniqueId());
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onDamage(final EntityDamageEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		if (profile.getSubState().equals(SubState.PLAYING)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreakBlock(final BlockBreakEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getSubState().equals(SubState.BUILD)) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreakPlace(final BlockPlaceEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getSubState().equals(SubState.BUILD)) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteractWithSpawnItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (!event.hasItem()) return;
			if (profile.getSubState().equals(SubState.BUILD)) return;
			if (event.getItem().getType().equals(Material.STONE_SWORD) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedInventory());
			}
			if (event.getItem().getType().equals(Material.DIAMOND_SWORD) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getRankedInventory());
			}
			if (event.getItem().getType().equals(Material.NETHER_STAR) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getPersonnalInventory().getPersonnalInventory());
			}
			if (event.getItem().getType().equals(Material.BOOK) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getUtilsInventory().getUtilsInventory());
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDropped(final PlayerDropItemEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getSubState().equals(SubState.PLAYING) || profile.getSubState().equals(SubState.STARTING)) {
			if (event.getItemDrop().getItemStack().getType().equals(Material.DIAMOND_SWORD)) {
				event.setCancelled(true);
			}
			if (event.getItemDrop().getItemStack().getType().equals(Material.GLASS_BOTTLE)) {
				event.getItemDrop().remove();
			}
			GameUtils.addDrops(event.getItemDrop(), event.getPlayer().getUniqueId());
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onHunger(final FoodLevelChangeEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		if (profile.getSubState().equals(SubState.PLAYING)) {
			if (Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()) != null) {
				final String matchLadder = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getLadder().displayName();
				if (matchLadder.equals(ChatColor.DARK_AQUA + "Sumo") || matchLadder.equals(ChatColor.DARK_AQUA + "Boxing") || matchLadder.equals(ChatColor.DARK_AQUA + "Soup") || matchLadder.equals(ChatColor.DARK_AQUA + "Comboxing") || matchLadder.equals(ChatColor.DARK_AQUA + "Quick NoDebuff") || matchLadder.equals(ChatColor.DARK_AQUA + "Speed NoDebuff")) {
					event.setCancelled(true);
				}
				else {
					return;
				}
			}
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onConsume(final PlayerItemConsumeEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getSubState().equals(SubState.STARTING) || profile.getSubState().equals(SubState.PLAYING)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (profile.getSubState().equals(SubState.BUILD)) return;
			if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onTalking(final AsyncPlayerChatEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getPlayerCache().isMuted()) {
        	Date todayGlobal = new Date();
        	SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        	String today = s.format(todayGlobal);
			try {
				if (profile.getPlayerCache().getMuteExpiresOn() != s.parse(today)) {
					event.getPlayer().sendMessage(ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "You are currently silenced for the reason: " + ChatColor.WHITE + profile.getPlayerCache().getMuteReason());
					event.setCancelled(true);
					return;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (event.getMessage().startsWith("#") && event.getPlayer().hasPermission("bawz.moderation")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission("bawz.moderation")) {
					final String message = event.getMessage().replace("#", "");
					player.sendMessage(ChatColor.RED + " ✴ " + profile.getRank().getColor() + profile.getRank().getDisplayName() + " " + profile.getRank().getColor() + event.getPlayer().getName() + ChatColor.GRAY + " » " + ChatColor.RED + message);
				}
			}
			event.setCancelled(true);
			return;
		}
		event.setFormat((profile.getRank() == Rank.PLAYER ? ChatColor.GRAY.toString() : ChatColor.GRAY + "[" + profile.getRank().getColor() + profile.getRank().getDisplayName() + ChatColor.GRAY + "] " + profile.getRank().getColor()) + "%1$s" + (profile.getTag() == Tag.NORMAL ? ChatColor.RESET + ": ": ChatColor.GRAY + " (" + profile.getTag().getColor() + profile.getTag().getDisplay() + ChatColor.GRAY + ") " + ChatColor.RESET + ": " + (profile.getRank() == Rank.PLAYER ? ChatColor.GRAY : ChatColor.WHITE)) + "%2$s");
	}

}
