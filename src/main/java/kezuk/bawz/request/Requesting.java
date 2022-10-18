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
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, (ladder == "Party" ? "/party accept " : "/accept ") + Bukkit.getPlayer(requester).getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Accept the " + (ladder == "Party" ? "Party" : "Duel") + " request!").create()));
        requestMessage.addExtra((BaseComponent)accept);
        final TextComponent deny = new TextComponent(ChatColor.GRAY + " (" + ChatColor.RED + "Deny" + ChatColor.GRAY + ")");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, (ladder == "Party" ? "/party deny " : "/deny ")  + Bukkit.getPlayer(requester).getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Deny the duel request!").create()));
        requestMessage.addExtra((BaseComponent)deny);
        Bukkit.getPlayer(requested).spigot().sendMessage(requestMessage);
        Bukkit.getPlayer(requester).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been sent your duel request to " + ChatColor.WHITE + Bukkit.getPlayer(requested).getName());
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
