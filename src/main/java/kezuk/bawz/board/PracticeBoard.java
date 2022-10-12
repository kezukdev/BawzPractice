package kezuk.bawz.board;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;

import kezuk.bawz.Practice;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.MatchStatus;
import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;

public class PracticeBoard implements BoardAdapter
{
    private final Practice plugin;
    private String spacer = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "»--*------*--«";
    private String ip = ChatColor.ITALIC.toString() + ChatColor.AQUA + "bawz.eu";
    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT);
    
    public PracticeBoard() {
        this.plugin = Practice.getInstance();
    }
    
    @Override
    public String getTitle(final Player player) {
        return ChatColor.GRAY.toString() + shortDateFormat.format(new Date());
    }
    
    @Override
    public List<String> getScoreboard(final Player player, final Board board, final Set<BoardCooldown> cooldowns) {
        final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
        if (pm == null) {
            this.plugin.getLogger().warning(String.valueOf(player.getName()) + "'s player data is null");
            return null;
        }
        if (pm.getPlayerStatus().equals(Status.SPAWN) || pm.getPlayerStatus().equals(Status.QUEUE)) {
        	return this.getSpawnBoard(player);
        }
        if (pm.getPlayerStatus().equals(Status.FIGHT)) {
            if (Practice.getMatchs().get(pm.getMatchUUID()) != null && Practice.getMatchs().get(pm.getMatchUUID()).getStatus().equals(MatchStatus.PLAYING)) {
            	return this.getGameBoard(player);
            }
            if (Practice.getMatchs().get(pm.getMatchUUID()) != null && Practice.getMatchs().get(pm.getMatchUUID()).getStatus().equals(MatchStatus.STARTING)) {
            	return this.getStartBoard(player);
            }
            return null;
        }
		return null;
    }
    
	private List<String> getSpawnBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        board.add(spacer);
        for (Ladders ladder : Practice.getInstance().getLadder()) {
        	board.add(ChatColor.WHITE + ChatColor.stripColor(ladder.displayName()) + ChatColor.GRAY + ": " + ChatColor.DARK_AQUA + PlayerManager.getPlayers().get(player.getUniqueId()).getElos()[ladder.id()]);
        }
        board.add(spacer);
        board.add("     " + ip);
        return board;
    }
    
	private List<String> getGameBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
        board.add(spacer);
    	board.add(ChatColor.DARK_AQUA + Practice.getMatchs().get(pm.getMatchUUID()).getLadder().displayName() + ChatColor.GRAY + " [" + ChatColor.AQUA.toString() + (Practice.getMatchs().get(pm.getMatchUUID()).isRanked() ? "R" : "U") + ChatColor.GRAY + "]");
    	board.add(ChatColor.DARK_AQUA + "Ping" + ChatColor.RESET + ": " + ChatColor.AQUA + player.getPing());
        if (pm.getMatchStats().getEnderPearlCooldown() != 0L) {
    		final double time = pm.getMatchStats().getEnderPearlCooldown() / 1000.0D;
    		final DecimalFormat df = new DecimalFormat("#.#");
    		board.add(ChatColor.DARK_AQUA + "Enderpearl" + ChatColor.RESET + ": " + ChatColor.AQUA + df.format(time));	
        }
        board.add(spacer);
        board.add("      " + ip.toString());
        return board;
    }

	private List<String> getStartBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
    	board.add(spacer);
    	board.add(ChatColor.DARK_AQUA + Practice.getMatchs().get(pm.getMatchUUID()).getLadder().displayName() + ChatColor.GRAY + " [" + (Practice.getMatchs().get(pm.getMatchUUID()).isRanked() ? ChatColor.AQUA + "R" : ChatColor.AQUA + "U") + ChatColor.GRAY + "]");
    	board.add(ChatColor.DARK_AQUA + "Opponent" + ChatColor.RESET + ": " + ChatColor.AQUA + Bukkit.getServer().getPlayer(Practice.getMatchs().get(pm.getMatchUUID()).getOpponent(player.getUniqueId())).getName());
    	board.add(ChatColor.DARK_AQUA + "Their Ping" + ChatColor.RESET + ": " + ChatColor.AQUA + Bukkit.getServer().getPlayer(Practice.getMatchs().get(pm.getMatchUUID()).getOpponent(player.getUniqueId())).getPing());
    	if (Practice.getMatchs().get(pm.getMatchUUID()).isRanked()) {
    		board.add(ChatColor.DARK_AQUA + "Elo" + ChatColor.RESET + ": " + ChatColor.AQUA + PlayerManager.getPlayers().get(Bukkit.getPlayer(Practice.getMatchs().get(pm.getMatchUUID()).getOpponent(player.getUniqueId())).getUniqueId()).getElos()[Practice.getMatchs().get(pm.getMatchUUID()).getLadder().id()]);
    	}
    	board.add(spacer);
    	board.add("      " + ip.toString().toString());
        return board;
    }
}