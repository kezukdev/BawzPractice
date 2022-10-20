package kezuk.practice.request;

import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Requesting {
	
	public Requesting(final UUID requester, final UUID requested, final String ladder) {
		Profile pmTarget = Practice.getInstance().getRegisterCollections().getProfile().get(requested);
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
        Bukkit.getPlayer(requester).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have been sent your " + (ladder == "Party" ? "party" : "duel") + " request to " + ChatColor.WHITE + Bukkit.getPlayer(requested).getName());
        pmTarget.getRequest().get(requester).applyRequestCooldown();
	}
	
	public class Request {
		private UUID requester;
		private UUID requested;
		private String ladder;
		private Long requestCooldown = 0L;
		
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
		
		public boolean haveRequestCooldownActive() {
			return this.requestCooldown > System.currentTimeMillis();
		}

		public long getRequestCooldown() {
			return Math.max(0L, this.requestCooldown - System.currentTimeMillis());
		}

		public void applyRequestCooldown() {
			this.requestCooldown = Long.valueOf(System.currentTimeMillis() + 14 * 1000);
		}

		public void removeRequestCooldown() {
			this.requestCooldown = 0L;
		}
	}

}
