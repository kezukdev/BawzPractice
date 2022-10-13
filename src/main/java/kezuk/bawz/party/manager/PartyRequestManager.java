package kezuk.bawz.party.manager;

import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.bawz.Practice;
import kezuk.bawz.party.InviteRequest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PartyRequestManager {
	
	private InviteRequest request;
	private UUID invited;
	private UUID inviter;
	
	public static WeakHashMap<UUID, PartyRequestManager> requestParty;
	
	public PartyRequestManager(final UUID invited, final UUID inviter)  {
		requestParty = new WeakHashMap<>();
		this.invited = invited;
		this.inviter = inviter;
		this.request = InviteRequest.WAITTING;
		requestParty.putIfAbsent(inviter, this);
        final TextComponent requestMessage = new TextComponent(ChatColor.GRAY + " * " + ChatColor.AQUA + Bukkit.getServer().getPlayer(inviter).getName() + ChatColor.DARK_AQUA + " have sent a party invite.");
        requestMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + Bukkit.getServer().getPlayer(inviter).getName()));
        requestMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click for join the party!").create()));
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The invite as been sended to " + ChatColor.WHITE + Bukkit.getPlayer(invited).getName());
        Bukkit.getPlayer(invited).spigot().sendMessage(requestMessage);
        new BukkitRunnable() {
            public void run() {
                if (request != InviteRequest.WAITTING) {
                    requestParty.clear();
                	this.cancel();
                	return;
                }
                requestParty.clear();
                Bukkit.getPlayer(inviter).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The party invite request have expired!");
            }
        }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 300L);
	}
	
	public void acceptInvite() {
		setRequest(InviteRequest.ACCEPT);
		PartyManager.getPartyMap().get(inviter).addToParty(invited);
	}
	
	public void denyInvite() {
		setRequest(InviteRequest.DENY);
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "The party invite reply is deny.");
	}
	
	public UUID getInvited() {
		return invited;
	}
	
	public UUID getInviter() {
		return inviter;
	}
	
	public InviteRequest getRequest() {
		return request;
	}
	
	public static WeakHashMap<UUID, PartyRequestManager> getRequestParty() {
		return requestParty;
	}
	
	public void setRequest(InviteRequest request) {
		this.request = request;
	}

}
