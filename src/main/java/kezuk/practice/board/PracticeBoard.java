package kezuk.practice.board;

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

import kezuk.practice.Practice;
import kezuk.practice.player.Profile;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.utils.GameUtils;

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
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        if (pm == null) {
            this.plugin.getLogger().warning(String.valueOf(player.getName()) + "'s player data is null");
            return null;
        }
        if (pm.getGlobalState().equals(GlobalState.FIGHT)) {
            if (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()) != null && pm.getSubState().equals(SubState.PLAYING)) {
            	return this.getGameBoard(player);
            }
            if (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()) != null && pm.getSubState().equals(SubState.STARTING)) {
            	return this.getStartBoard(player);
            }
            return null;
        }
		return null;
    }
    
	private List<String> getGameBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
        board.add(spacer);
    	board.add(ChatColor.DARK_AQUA + Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).getLadder().displayName() + " " + ChatColor.AQUA + (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).isRanked() ? "R" : "U"));
    	board.add(ChatColor.DARK_AQUA + "Ping" + ChatColor.RESET + ": " + ChatColor.AQUA + player.getPing());
        if (pm.getPlayerCache().getMatchStats().getEnderPearlCooldown() != 0L) {
    		final double time = pm.getPlayerCache().getMatchStats().getEnderPearlCooldown() / 1000.0D;
    		final DecimalFormat df = new DecimalFormat("#.#");
    		board.add(ChatColor.DARK_AQUA + "Enderpearl" + ChatColor.RESET + ": " + ChatColor.AQUA + df.format(time));	
        }
        board.add(spacer);
        board.add("      " + ip.toString());
        return board;
    }

	private List<String> getStartBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId());
    	board.add(spacer);
    	board.add(ChatColor.DARK_AQUA + Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).getLadder().displayName() + ChatColor.AQUA + " " + (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).isRanked() ? "R" : "U"));
    	board.add(ChatColor.DARK_AQUA + "Opponent" + ChatColor.RESET + ": " + Bukkit.getPlayer(GameUtils.getOpponent(player.getUniqueId())).getName());
    	board.add(ChatColor.DARK_AQUA + "Their Ping" + ChatColor.RESET + ": " + ChatColor.AQUA + Bukkit.getServer().getPlayer(GameUtils.getOpponent(player.getUniqueId())).getPing());
    	if (Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).isRanked()) {
    		board.add(ChatColor.DARK_AQUA + "Elo" + ChatColor.RESET + ": " + ChatColor.AQUA + Practice.getInstance().getRegisterCollections().getProfile().get(Bukkit.getPlayer(GameUtils.getOpponent(player.getUniqueId())).getUniqueId()).getElos()[Practice.getInstance().getRegisterCollections().getMatchs().get(pm.getMatchUUID()).getLadder().id()]);
    	}
    	board.add(spacer);
    	board.add("      " + ip.toString().toString());
        return board;
    }
}