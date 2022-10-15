package kezuk.bawz.listener;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import kezuk.bawz.Practice;
import kezuk.bawz.match.MatchStats;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.utils.MessageSerializer;

public class EnderDelayListener implements Listener {	
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() instanceof Player) {
			final Player player = event.getPlayer();
			
			if (!event.hasItem()) {
				return;
			}
			final ItemStack item = event.getItem();
			if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && item.getType() == Material.ENDER_PEARL && player.getGameMode() != GameMode.CREATIVE) {
				PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
				if (Practice.getMatchs().get(pm.getMatchUUID()) != null && Practice.getMatchs().get(pm.getMatchUUID()).getStatus() != MatchStatus.PLAYING || Practice.getFfaMatchs().get(pm.getMatchUUID()) != null && Practice.getFfaMatchs().get(pm.getMatchUUID()).getStatus() != MatchStatus.PLAYING) {
					event.setUseItemInHand(Result.DENY);
					player.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
					player.updateInventory();
					return;
				}
				MatchStats matchStats = pm.getMatchStats();
				if (!matchStats.isEnderPearlCooldownActive()) {
					matchStats.applyEnderPearlCooldown();
					return;
				}
				event.setUseItemInHand(Result.DENY);
				final double time = matchStats.getEnderPearlCooldown() / 1000.0D;
				final DecimalFormat df = new DecimalFormat("#.#");
				player.sendMessage(MessageSerializer.PEARL_COOLDOWN + ChatColor.DARK_AQUA + df.format(time) + ChatColor.WHITE + " second" + (time > 1.0D ? "s" : ""));
				player.updateInventory();
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			final Player player = event.getPlayer();
			final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
			if (Practice.getMatchs().get(pm.getMatchUUID()) != null && Practice.getMatchs().get(pm.getMatchUUID()).getStatus() != MatchStatus.PLAYING || Practice.getFfaMatchs().get(pm.getMatchUUID()) != null && Practice.getFfaMatchs().get(pm.getMatchUUID()).getStatus() != MatchStatus.PLAYING) event.setCancelled(true);
		}
	}
}