package kezuk.bawz.request;

import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import kezuk.bawz.Practice;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.player.PlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class RequestManager {
	
	public static WeakHashMap<UUID, Ladders> request;
	private Ladders ladder;
	
	public RequestManager(final UUID uuid, final UUID targetUUID, final String ladderName, final boolean roundable) {
		request = new WeakHashMap<>();
		final Ladders ladder = Ladders.getLadder(ladderName);
		this.ladder = ladder;
		request.put(targetUUID, ladder);
        final TextComponent requestMessage = new TextComponent(ChatColor.GRAY + " * " + ChatColor.AQUA + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.DARK_AQUA + " have sent a duel request in " + ChatColor.AQUA + ladder.displayName() + (PlayerManager.getPlayers().get(uuid).isRoundable() ? ChatColor.DARK_AQUA + " with round." : ChatColor.DARK_AQUA + "."));
        requestMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + Bukkit.getServer().getPlayer(uuid).getName()));
        requestMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click for accept the duel request.").create()));
        Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your duel request as been sent to " + ChatColor.RESET + Bukkit.getPlayer(targetUUID).getName());
        Bukkit.getServer().getPlayer(targetUUID).spigot().sendMessage(requestMessage);
        new BukkitRunnable() {
            public void run() {
                if (PlayerManager.getPlayers().get(uuid).getDuelRequest() != DuelRequestStatus.CANNOT) {
                    request.clear();
                	this.cancel();
                	return;
                }
                request.clear();
                PlayerManager.getPlayers().get(uuid).setTargetDuel(null);
                PlayerManager.getPlayers().get(uuid).setDuelRequest(DuelRequestStatus.CAN);
                Bukkit.getServer().getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Duel request has expired!");
                Bukkit.getServer().getPlayer(targetUUID).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Duel request has expired!");
            }
        }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 300L);
	}
	
	public Ladders getLadder() {
		return ladder;
	}
	
	public static WeakHashMap<UUID, Ladders> getRequest() {
		return request;
	}
}
