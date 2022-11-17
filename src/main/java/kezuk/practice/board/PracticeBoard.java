package kezuk.practice.board;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.queue.QueueSystem.QueueEntry;
import kezuk.practice.utils.GameUtils;

public class PracticeBoard implements BoardAdapter
{
    private final Practice plugin;
    private String spacer = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "»--*---------*--«";
    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT);
    
    public PracticeBoard() {
        this.plugin = Practice.getInstance();
    }
    
    @Override
    public String getTitle(final Player player) {
        return "§3§opractice";
    }
    
    @Override
    public List<String> getScoreboard(final Player player, final Board board, final Set<BoardCooldown> cooldowns) {
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        if (pm == null) {
            this.plugin.getLogger().warning(String.valueOf(player.getName()) + "'s player data is null");
            return null;
        }
        if (pm.getPlayerCache().isScoreboard()) {
            if (pm.getGlobalState().equals(GlobalState.SPAWN) || pm.getGlobalState().equals(GlobalState.QUEUE) || pm.getGlobalState().equals(GlobalState.PARTY)) {
            	return this.getLobbyBoard(player);
            }
            if (pm.getGlobalState().equals(GlobalState.SPECTATE)) {
            	return this.getSpectateBoard(player);
            }
            if (pm.getGlobalState().equals(GlobalState.FIGHT)) {
                if (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()) != null && pm.getSubState().equals(SubState.PLAYING)) {
                	return this.getGameBoard(player);
                }
                if (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()) != null && pm.getSubState().equals(SubState.STARTING)) {
                	return this.getStartBoard(player);
                }
            }	
        }
		return null;
    }
    
	private List<String> getSpectateBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final StartMatch match = GameUtils.getMatchManagerBySpectator(player.getUniqueId());
        board.add(spacer);
        board.add(ChatColor.WHITE + ChatColor.stripColor(match.getLadder().displayName()) + ChatColor.GRAY + " [" + (match.isRanked() ? "§5R" : "§eC") + ChatColor.GRAY + "]");
        for (UUID uuid : match.getFirstList()) {
            board.add(ChatColor.GREEN + Bukkit.getPlayer(uuid).getName() + ChatColor.WHITE + " elo: " + ChatColor.DARK_AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getElos()[match.getLadder().id()]);	
        }
        for (UUID uuid : match.getSecondList()) {
            board.add(ChatColor.GREEN + Bukkit.getPlayer(uuid).getName() + ChatColor.WHITE + " elo: " + ChatColor.DARK_AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getElos()[match.getLadder().id()]);	
        }
        board.add(spacer);
        board.add(ChatColor.GRAY.toString() + "  " + shortDateFormat.format(new Date()));
        return board;
    }
    
	private List<String> getLobbyBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        board.add(spacer);
        for (Ladders ladder : Practice.getInstance().getLadder()) {
        	if (ladder.isRanked()) {
            	board.add(ChatColor.WHITE + ChatColor.stripColor(ladder.displayName()) + ChatColor.GRAY + ": " + ChatColor.DARK_AQUA + pm.getElos()[ladder.id()]);	
        	}
        }
        if (pm.getGlobalState().equals(GlobalState.QUEUE)) {
        	board.add(spacer);
        	board.add(ChatColor.GRAY + " * " + ChatColor.WHITE + "Currently in queue");
        	final QueueEntry queue = Practice.getInstance().getRegisterObject().getQueueSystem().getQueue().get(player.getUniqueId());
        	board.add(ChatColor.WHITE + " Ladder: " + queue.getLadder().displayName());
        	board.add(ChatColor.WHITE + " Type: " + (queue.isRanked() ? "§5Ranked" : "§eCasual"));
        	if (queue.isRanked()) {
            	board.add(ChatColor.WHITE + " Range: " + ChatColor.AQUA + queue.getEloRange().getMinimumRange() + " §7» §b" + queue.getEloRange().getMaximumRange());	
        	}
        	board.add(ChatColor.WHITE + " Ping: " + ChatColor.DARK_AQUA + player.getPing());
        }
        board.add(spacer);
        board.add(ChatColor.GRAY.toString() + "  " + shortDateFormat.format(new Date()));
        return board;
    }
    
	private List<String> getGameBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        board.add(spacer);
    	board.add(ChatColor.WHITE + ChatColor.stripColor(Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).getLadder().displayName()).toString() + ChatColor.GRAY + " [" + (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).isRanked() ? "§5R" : "§eC") + ChatColor.GRAY + "]");
    	board.add(ChatColor.DARK_AQUA + "Their Ping" + ChatColor.WHITE + ": " + Bukkit.getPlayer(GameUtils.getOpponent(player.getUniqueId())).getPing());
        if (pm.getPlayerCache().getMatchStats().getEnderPearlCooldown() != 0L) {
    		final double time = pm.getPlayerCache().getMatchStats().getEnderPearlCooldown() / 1000.0D;
    		final DecimalFormat df = new DecimalFormat("#.#");
    		board.add(ChatColor.DARK_AQUA + "Enderpearl" + ChatColor.WHITE + ": " + df.format(time) + "s");	
    		player.setLevel((int)time);
    		final float timeInf = pm.getPlayerCache().getMatchStats().getEnderPearlCooldown() / 16000.0f;
    		player.setExp(timeInf);
        }
        board.add(spacer);
        board.add(ChatColor.GRAY.toString() + "  " + shortDateFormat.format(new Date()));
        return board;
    }

	private List<String> getStartBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
    	board.add(spacer);
    	board.add(ChatColor.WHITE + ChatColor.stripColor(Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).getLadder().displayName()).toString() + ChatColor.GRAY + " [" + (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).isRanked() ? "§5R" : "§eC") + ChatColor.GRAY + "]");
    	board.add(ChatColor.DARK_AQUA + "Opponent" + ChatColor.RESET + ": " + Bukkit.getPlayer(GameUtils.getOpponent(player.getUniqueId())).getName());
    	board.add(ChatColor.DARK_AQUA + "Their Ping" + ChatColor.RESET + ": " + ChatColor.AQUA + Bukkit.getServer().getPlayer(GameUtils.getOpponent(player.getUniqueId())).getPing());
    	if (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).isRanked()) {
    		board.add(ChatColor.DARK_AQUA + "Elo" + ChatColor.RESET + ": " + ChatColor.AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(GameUtils.getOpponent(player.getUniqueId())).getUniqueId()).getElos()[Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).getLadder().id()]);
    	}
    	board.add(spacer);
        board.add(ChatColor.GRAY.toString() + "  " + shortDateFormat.format(new Date()));
        return board;
    }
}