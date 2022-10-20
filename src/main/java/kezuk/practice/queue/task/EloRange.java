package kezuk.practice.queue.task;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;

public class EloRange {
	
	private int minimumRange;
	private int maximumRange;
	private int updateTime;
	private boolean opponentFounded;
	private UUID uuid;
	private Ladders ladder;
	
	public EloRange(final UUID uuid, Ladders ladder) {
		final int elo = Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getElos()[ladder.id()];
		this.range(elo-60, elo+60);
		if (Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().size() >= 1) {
			this.searchOpponent(uuid);
		}
		this.uuid = uuid;
		this.ladder = ladder;
		this.updateTime = 0;
		this.opponentFounded = false;
		Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "Your elo range is " + ChatColor.WHITE + minimumRange + ChatColor.DARK_AQUA + " to " + ChatColor.WHITE + maximumRange);
		Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().put(uuid, this);
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
        for (final Map.Entry<UUID, EloRange> map : Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().entrySet()) {
        	final UUID key = map.getKey();
        	final EloRange eloRange = map.getValue();
        	if (uuid != key && ladder == eloRange.getLadder()) {
        		possibleUUID = key;
    			if (Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().get(possibleUUID).getMaximumRange() >= getMinimumRange() || Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().get(possibleUUID).getMaximumRange() <= getMaximumRange()) {
    				opponentFounded = true;
    				Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().get(possibleUUID).setOpponentFounded(true);
    				List<UUID> firstPlayer = Lists.newArrayList(uuid);
    				List<UUID> secondPlayer = Lists.newArrayList(possibleUUID);
    				new StartMatch(firstPlayer, secondPlayer,null, ladder, true, false);
    				Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().remove(uuid);
    				Practice.getInstance().getRegisterObject().getQueueSystem().getEloRange().remove(possibleUUID);
    				Practice.getInstance().getRegisterObject().getQueueSystem().getQueue().remove(uuid);
    				Practice.getInstance().getRegisterObject().getQueueSystem().getQueue().remove(possibleUUID);
    				Practice.getInstance().getRegisterObject().getLadderInventory().refreshInventory();
    				return;
    			}
        	}
			continue;
        }
		this.range(minimumRange-60, maximumRange+60);
		Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "!" + ChatColor.GRAY + "] " + ChatColor.DARK_AQUA + "Your elo range has updated " + ChatColor.WHITE + minimumRange + ChatColor.DARK_AQUA + " to " + ChatColor.WHITE + maximumRange);
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
