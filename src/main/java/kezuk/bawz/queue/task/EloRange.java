package kezuk.bawz.queue.task;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.bawz.Practice;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.player.PlayerManager;

public class EloRange {
	
	private int minimumRange;
	private int maximumRange;
	private int updateTime;
	private boolean opponentFounded;
	private UUID uuid;
	private Ladders ladder;
	
	public EloRange(final UUID uuid, Ladders ladder) {
		final int elo = PlayerManager.getPlayers().get(uuid).getElos()[ladder.id()];
		this.range(elo-60, elo+60);
		if (Practice.getEloRangeMap().size() >= 1) {
			this.searchOpponent(uuid);
		}
		this.uuid = uuid;
		this.ladder = ladder;
		this.updateTime = 0;
		this.opponentFounded = false;
		Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "Your elo range is " + ChatColor.WHITE + minimumRange + ChatColor.DARK_AQUA + " to " + ChatColor.WHITE + maximumRange);
		Practice.getEloRangeMap().put(uuid, this);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				updateTime++;
				if (opponentFounded) {
					this.cancel();
					return;
				}
				if (updateTime == 15) {
					updateTime = 0;
					searchOpponent(uuid);
				}
			}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}
	
	public void searchOpponent(UUID uuid) {
		UUID possibleUUID;
        for (final Map.Entry<UUID, EloRange> map : Practice.getEloRangeMap().entrySet()) {
        	final UUID key = map.getKey();
        	final EloRange eloRange = map.getValue();
        	if (uuid != key && ladder == eloRange.getLadder()) {
        		possibleUUID = key;
    			if (Practice.getEloRangeMap().get(possibleUUID).getMaximumRange() >= getMinimumRange() || Practice.getEloRangeMap().get(possibleUUID).getMaximumRange() <= getMaximumRange()) {
    				opponentFounded = true;
    				Practice.getEloRangeMap().get(possibleUUID).setOpponentFounded(true);
    				MatchManager match = new MatchManager();
    				List<UUID> firstPlayer = Lists.newArrayList(uuid);
    				List<UUID> secondPlayer = Lists.newArrayList(possibleUUID);
    				match.startMath(firstPlayer, secondPlayer, ladder, true);
    				Practice.getEloRangeMap().remove(uuid);
    				Practice.getEloRangeMap().remove(possibleUUID);
    				Practice.getInstance().getQueueManager().queue.remove(uuid);
    				Practice.getInstance().getQueueManager().queue.remove(possibleUUID);
                    Practice.getInstance().getInventoryManager().updateQueueInventory(true);
    				return;
    			}
        	}
			this.range(minimumRange-60, maximumRange+60);
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "Your elo range has updated " + ChatColor.WHITE + minimumRange + ChatColor.DARK_AQUA + " to " + ChatColor.WHITE + maximumRange);
			continue;
        }
	}
	
	public void range(int minRange, int maxRange) {
		this.minimumRange = minRange;
		this.maximumRange = maxRange;
	}
	
	public void setOpponentFounded(boolean opponentFounded) {
		this.opponentFounded = opponentFounded;
	}
	
	public Ladders getLadder() {
		return ladder;
	}
	
	public int getMinimumRange() {
		return minimumRange;
	}
	
	public int getMaximumRange() {
		return maximumRange;
	}
	
	public UUID getUuid() {
		return uuid;
	}

}
