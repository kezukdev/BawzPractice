package kezuk.practice.player.utils.listener;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.bspigot.utils.DiscordWebhook;
import kezuk.bspigot.utils.DiscordWebhook.EmbedObject;
import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventType;
import kezuk.practice.event.tournament.Tournament;
import kezuk.practice.event.tournament.TournamentTeam;
import kezuk.practice.event.tournament.items.TournamentItems;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;
import kezuk.practice.party.Party;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.utils.GameUtils;
import kezuk.practice.utils.ItemSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class UtilsListener implements Listener {
	
	@EventHandler
	public void onInventoryClickOfUtils(final InventoryClickEvent event) {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(event.getWhoClicked().getUniqueId());
		if (profile.getGlobalState().equals(GlobalState.SPAWN)) {
			if (event.getCurrentItem() == null && event.getCurrentItem().getType().equals(Material.AIR)) return;
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getUtilsInventory().getUtilsInventory().getName())) {
				if (event.getCurrentItem().getType().equals(Material.DIAMOND)) {
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getUtilsInventory().getLeaderboardInventory());
				}
				if (event.getCurrentItem().getType().equals(Material.CAKE)) {
					event.getWhoClicked().closeInventory();
					new Party(event.getWhoClicked().getUniqueId());
				}
				if (event.getCurrentItem().getType().equals(Material.ENDER_PORTAL)) {
					event.getWhoClicked().closeInventory();
					List<ItemStack> matchs = Lists.newArrayList();
			        for (StartMatch matchManager : Practice.getInstance().getRegisterCollections().getMatchs().values()) {
			        	for (UUID uuid : matchManager.getFirstList()) {
			        		final String[] lore = new String[] {" ", ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Ladder" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getLadder().displayName(), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Type" + ChatColor.RESET + ": " + ChatColor.AQUA + (matchManager.isRanked() ? "Ranked" : "Unranked"), ChatColor.GRAY + " * " + ChatColor.DARK_AQUA + "Arena" + ChatColor.RESET + ": " + ChatColor.AQUA + matchManager.getArena().getName(), " "};
			                ItemStack item = ItemSerializer.serialize(new ItemStack(matchManager.getLadder().material()), matchManager.getLadder().data(), ChatColor.GREEN + Bukkit.getServer().getPlayer(uuid).getName() + ChatColor.AQUA + " vs " + ChatColor.RED + Bukkit.getServer().getPlayer(GameUtils.getOpponent(uuid)).getName(), Arrays.asList(lore));
			                matchs.add(item);
			        	}
			        }
			        Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().refresh(matchs);
					Practice.getInstance().getRegisterObject().getSpectateInventory().getSpectateInventory().open((Player) event.getWhoClicked(), 1);
				}
				if (event.getCurrentItem().getType().equals(Material.PAPER)) {
					event.getWhoClicked().closeInventory();
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getHostInventory().getHost());
				}
				if (event.getCurrentItem().getType().equals(Material.FERMENTED_SPIDER_EYE)) {
					event.getWhoClicked().closeInventory();
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getTournamentInventory());
				}
				event.setCancelled(true);
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getHostInventory().getHost().getName())) {
				if (event.getCurrentItem().getType().equals(Material.FLINT)) {
					event.getWhoClicked().closeInventory();
					event.getWhoClicked().openInventory(Practice.getInstance().getRegisterObject().getLadderInventory().getFfaInventory());
				}
				if (event.getCurrentItem().getType().equals(Material.ANVIL)) {
					event.getWhoClicked().closeInventory();
					Practice.getInstance().getRegisterObject().getEvent().startHost(event.getWhoClicked().getUniqueId(), EventType.SUMO, null);
				}
				if (event.getCurrentItem().getType().equals(Material.GOLD_SWORD)) {
					event.getWhoClicked().closeInventory();
					Practice.getInstance().getRegisterObject().getEvent().startHost(event.getWhoClicked().getUniqueId(), EventType.OITC, null);
				}
				event.setCancelled(true);
			}
			if (event.getClickedInventory().getName().equalsIgnoreCase(Practice.getInstance().getRegisterObject().getLadderInventory().getTournamentInventory().getName())) {
				if (Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()) == null) return;
				event.getWhoClicked().closeInventory();
				if (Practice.getInstance().getRegisterCollections().getRunningTournaments().size() != 0) {
					final Player player = (Player) event.getWhoClicked();
					player.sendMessage(ChatColor.AQUA + "You can't start a tournament because a tournament is already started!");
					return;
				}
				final Tournament tournament = new Tournament(1, Ladders.getLadder(event.getCurrentItem().getItemMeta().getDisplayName()), true);
				Practice.getInstance().getRegisterCollections().getRunningTournaments().add(tournament);
				profile.setGlobalState(GlobalState.TOURNAMENT);
				new TournamentItems(event.getWhoClicked().getUniqueId());
		        final TextComponent startMessage = new TextComponent(ChatColor.GRAY.toString() + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "]" + ChatColor.WHITE + " " + Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).getName() + ChatColor.DARK_AQUA + " to launch a tournament in " + ChatColor.WHITE + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
		        final TextComponent join = new TextComponent(ChatColor.GRAY + " (" + ChatColor.AQUA + "Join" + ChatColor.GRAY + ")");
		        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join tournaments"));
		        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Join the tournament in " + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())).create()));
		        startMessage.addExtra((BaseComponent)join);
		        for(Player players : Bukkit.getOnlinePlayers()) {
		        	players.spigot().sendMessage(startMessage);
		        }
                final TournamentTeam tournamentTeam = new TournamentTeam();
                final Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
                tournamentTeam.setPlayers(Collections.singletonList(player.getUniqueId()));
                tournament.getTeams().add(tournamentTeam);
                tournament.sendMessage(ChatColor.AQUA + player.getName() + " has joined the tournament. (" + tournament.getTotalPlayersInTournament() + "/" + tournament.getPlayersLimit() + ")");
        		try {
        			DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1031183031022657557/iUL3mbkNXzzEV0bdOPpclu-u6wL4YtxoTvZl5gxNw8RK90hG5EbZUCqAbRWse2MnVIHo");
        			EmbedObject embed = new EmbedObject();
        			embed.setTitle("   🪸  TOURNAMENT 1v1 🪸  ");
        			embed.addField("Author: ", Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).getName(), false);
        			embed.addField("Ladder: ", ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()), false);
        			embed.addField("IP: ", "bawz.eu", false);
        			embed.setFooter("This is an event that requires 4 players in it to run!", null);
        			embed.setColor(Color.CYAN);
        			webhook.addEmbed(embed);
        			webhook.execute();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
			}
		}
	}
}
