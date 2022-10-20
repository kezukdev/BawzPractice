package kezuk.practice.player.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import kezuk.practice.Practice;
import kezuk.practice.core.rank.Rank;
import kezuk.practice.core.tag.Tag;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.MatchUtils;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoining(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		new Profile(event.getPlayer().getUniqueId());
		new SpawnItems(event.getPlayer().getUniqueId());
		event.getPlayer().teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
		event.getPlayer().setSaturation(20.0f);
		for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
			event.getPlayer().removePotionEffect(effect.getType());
		}
		event.getPlayer().setPlayerListName(Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId()).getRank().getColor() + event.getPlayer().getName());
	}
	
	@EventHandler
	public void onQuitting(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onGotKicked(final PlayerKickEvent event) {
		onQuitting(new PlayerQuitEvent(event.getPlayer(), null));
	}
	
	@EventHandler
	public void onDamage(final EntityDamageEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			final StartMatch match = Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID());
			if (match != null && match.getLadder().name().equalsIgnoreCase("sumo") ^ match.getLadder().name().equalsIgnoreCase("boxing") ^ match.getLadder().name().equalsIgnoreCase("soup")){
				event.setCancelled(true);
				return;
			}
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteractWithSpawnItems(final PlayerInteractEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (!event.hasItem()) return;
			if (event.getItem().getType().equals(Material.STONE_SWORD) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getUnrankedInventory());
			}
			if (event.getItem().getType().equals(Material.DIAMOND_SWORD) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getRankedInventory());
			}
			if (event.getItem().getType().equals(Material.NETHER_STAR) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getUtilsInventory().getUtilsInventory());
			}
			if (event.getItem().getType().equals(Material.BOOK) && event.getAction().equals(Action.RIGHT_CLICK_AIR) ^ event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.getPlayer().openInventory(Practice.getInstance().getRegisterObject().getUtilsInventory().getUtilsInventory());
			}
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDropped(final PlayerDropItemEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.PLAYING) || profile.getGlobalState().getSubState().equals(SubState.STARTING)) {
			if (event.getItemDrop().getItemStack().getType().equals(Material.GLASS_BOTTLE)) {
				event.getItemDrop().remove();
			}
			MatchUtils.addDrops(event.getItemDrop(), event.getPlayer().getUniqueId());
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onHunger(final FoodLevelChangeEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getEntity().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			if (Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()) != null) {
				if (Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getLadder().name() != "sumo" || Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getLadder().name() != "boxing" || Practice.getInstance().getRegisterCollections().getMatchs().get(profile.getMatchUUID()).getLadder().name() != "soup") {
					return;
				}
			}
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onConsume(final PlayerItemConsumeEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		if (profile.getGlobalState().getSubState().equals(SubState.STARTING) || profile.getGlobalState().getSubState().equals(SubState.PLAYING)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onTalking(final AsyncPlayerChatEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getPlayer().getUniqueId());
		event.setFormat((profile.getRank() == Rank.PLAYER ? ChatColor.GRAY.toString() : ChatColor.GRAY + "[" + profile.getRank().getColor() + profile.getRank().getDisplayName() + ChatColor.GRAY + "] " + profile.getRank().getColor()) + "%1$s" + (profile.getTag() == Tag.NORMAL ? ChatColor.RESET + ": ": ChatColor.GRAY + "(" + profile.getTag().getColor() + profile.getTag() + ChatColor.GRAY + ") " + ChatColor.RESET + ": " + (profile.getRank() == Rank.PLAYER ? ChatColor.GRAY : ChatColor.WHITE)) + "%2$s");
	}

}