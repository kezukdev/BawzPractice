package kezuk.bawz.core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import kezuk.bawz.Practice;
import kezuk.bawz.player.PlayerManager;
import net.luckperms.api.cacheddata.CachedMetaData;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onTalking(final AsyncPlayerChatEvent event) {
		final PlayerManager pm = PlayerManager.getPlayers().get(event.getPlayer().getUniqueId());
		final CachedMetaData metaData = Practice.getInstance().getLuckPerms().getPlayerAdapter(Player.class).getMetaData(event.getPlayer());
		event.setFormat(ChatColor.translateAlternateColorCodes('&',(metaData.getPrefix() != null ? metaData.getPrefix() + " ": ChatColor.GRAY.toString()) + "%1$s" + (pm.getTag() == Tag.NORMAL ? " " :  ChatColor.GRAY + " (" + pm.getTag().getColor() + pm.getTag().getDisplay() + ChatColor.GRAY + ")")  + ChatColor.GRAY + " » " + ChatColor.WHITE + "%2$s"));
	}

}
