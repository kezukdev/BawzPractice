package kezuk.bawz.request;

import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import kezuk.bawz.player.PlayerManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Requesting {
	
	public Requesting(final UUID requester, final UUID requested, final String ladder) {
		PlayerManager pmTarget = PlayerManager.getPlayers().get(requested);
		pmTarget.request = new WeakHashMap<>();
		pmTarget.getRequest().put(requester, new Request(requester, requested, ladder));
        final TextComponent requestMessage = new TextComponent(ChatColor.GRAY + " * " + ChatColor.WHITE + Bukkit.getPlayer(requester).getName() + ChatColor.DARK_AQUA + " have sent to you a duel request into " + ChatColor.WHITE + ChatColor.stripColor(ladder));
        final TextComponent accept = new TextComponent(ChatColor.GRAY + " (" + ChatColor.AQUA + "Accept" + ChatColor.GRAY + ")");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + Bukkit.getPlayer(requester).getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Accept the duel request!").create()));
        requestMessage.addExtra((BaseComponent)accept);
        Bukkit.getPlayer(requested).spigot().sendMessage(requestMessage);
		PlayerManager.getPlayers().get(requester).applyDuelCooldown();
	}
	
	public class Request {
		private UUID requester;
		private UUID requested;
		private String ladder;
		
		public Request(final UUID requester, final UUID requested, final String ladder) {
			this.requester = requester;
			this.requested = requested;
			this.ladder = ladder;
		}
		
		public UUID getRequester() {
			return requester;
		}
		
		public UUID getRequested() {
			return requested;
		}
		
		public String getLadder() {
			return ladder;
		}
		
		public void setLadder(String ladder) {
			this.ladder = ladder;
		}
	}

}
