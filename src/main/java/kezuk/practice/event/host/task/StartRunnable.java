package kezuk.practice.event.host.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.practice.Practice;
import kezuk.practice.event.host.Event;
import kezuk.practice.event.host.sumo.SumoEvent;
import kezuk.practice.event.host.type.EventSubType;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StartRunnable {
	
	public StartRunnable(int cooldowns) {
        new BukkitRunnable() {
    		int cooldown = cooldowns;	
    		@Override
        	public void run() {
        		cooldown -= 1;
        		Event host = Practice.getInstance().getRegisterObject().getEvent();
        		if (host == null) {
        			this.cancel();
        			return;
        		}
        		if (host.getMembers().size() < 5) {
        			this.cancel();
        			return;
        		}
        		if (cooldown == 60) {
        	        final TextComponent hostMessage = new TextComponent(host.getPrefix() + ChatColor.DARK_AQUA + "The host " + ChatColor.WHITE + host.getEventType().toString() + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + Bukkit.getPlayer(host.getCreatorUUID()).getName() + ChatColor.DARK_AQUA + " starts in 1minutes" + (host.getLadder() != null ? ChatColor.DARK_AQUA + " in " + ChatColor.WHITE + ChatColor.stripColor(host.getLadder().displayName()) + ChatColor.DARK_AQUA + "." : "."));
        	        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + host.getCreatorUUID()));
        	        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + host.getEventType().toString() + " event!").create()));
        	        for (Player players : Bukkit.getOnlinePlayers()) {
        	        	players.sendMessage(" ");
        	        	players.spigot().sendMessage(hostMessage);
        	        	players.sendMessage(" ");
        	        }
        		}
        		if (cooldown == 30) {
        	        final TextComponent hostMessage = new TextComponent(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "The host " + ChatColor.WHITE + host.getEventType().toString() + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + Bukkit.getPlayer(host.getCreatorUUID()).getName() + ChatColor.DARK_AQUA + " starts in 30seconds" + (host.getLadder() != null ? ChatColor.DARK_AQUA + " in " + ChatColor.WHITE + ChatColor.stripColor(host.getLadder().displayName()) + ChatColor.DARK_AQUA + "." : "."));
        	        hostMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join " + host.getCreatorUUID()));
        	        hostMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click here for join the " + host.getEventType().toString() + " event!").create()));
        	        for (Player players : Bukkit.getOnlinePlayers()) {
        	        	players.sendMessage(" ");
        	        	players.spigot().sendMessage(hostMessage);
        	        	players.sendMessage(" ");
        	        }
        		}
        		if (cooldown == 5 || cooldown == 4 || cooldown == 3 || cooldown == 2 || cooldown == 1) {
        			if (host.getMembers().size() == 1) {
        				for (UUID uuid : host.getMembers()) {
        					final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
        					profile.setGlobalState(GlobalState.SPAWN);
                    		Bukkit.getPlayer(uuid).teleport(Practice.getInstance().getRegisterCommon().getSpawnLocation());
                    		new SpawnItems(uuid);
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.AQUA + "The host as cancelled because the host contains one player!");
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        				}
        				this.cancel();
        				return;
        			}
        			for (UUID uuid : host.getMembers()) {
        				Bukkit.getPlayer(uuid).sendMessage(" ");
        				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " [" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.AQUA + "The host started in " + ChatColor.WHITE + cooldown);
        				Bukkit.getPlayer(uuid).sendMessage(" ");
        			}
        		}
        		if (cooldown <= 0) {
        			host.getEventType().setSubType(EventSubType.STARTED);
        			if (host.getEventType().equals(EventType.FFA)) {
        				for (UUID uuid : host.getMembers()) {
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + " The host ffa as been started!");
        					Bukkit.getPlayer(uuid).sendMessage(" ");
        				}
        				new StartMatch(null, null, host.getMembers(), host.getLadder(), false, false);
        			}
        			if (host.getEventType().equals(EventType.SUMO)) {
        				host.sumoEvent = new SumoEvent(host.getMembers());
        			}
    				this.cancel();
        		}
        	}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}

}
