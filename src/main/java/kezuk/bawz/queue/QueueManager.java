package kezuk.bawz.queue;

import org.bukkit.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import kezuk.bawz.*;
import kezuk.bawz.ladders.*;
import kezuk.bawz.match.manager.MatchManager;
import kezuk.bawz.player.*;
import kezuk.bawz.queue.task.EloRange;
import kezuk.bawz.utils.*;

import java.util.*;

public class QueueManager
{
    public Map<UUID, Queue> queue;
    
    public QueueManager() {
        this.queue = Maps.newConcurrentMap();
    }
    
    public void addPlayerToQueue(final UUID uuid, final boolean ranked, final String ladderName, final boolean partyTo2) {
    	final Ladders ladder = Ladders.getLadder(ladderName);
        if (!this.queue.containsKey(uuid)) {
            final PlayerManager pm = PlayerManager.getPlayers().get(uuid);
            this.queue.put(uuid, new Queue(ranked, ladder, partyTo2));
            pm.setPlayerStatus(Status.QUEUE);
            pm.sendToQueue();
            Practice.getInstance().getInventoryManager().updateQueueInventory(ranked);
            if (ranked) {
                new EloRange(uuid, ladder);	
            }
        }
        if(!this.queue.isEmpty()) {
            UUID secondUUID = uuid;
            for (final Map.Entry<UUID, Queue> map : this.queue.entrySet()) {
                final UUID key = map.getKey();
                final Queue value = map.getValue();
                if (uuid != key && ranked == value.isRanked()) {
                    if (ladder != value.getLadder()) {
                        continue;
                    }
                    secondUUID = key;
                    if (secondUUID == uuid) {
                        return;
                    }
                    if (value.isRanked()) {
                    	return;
                    }
                    final List<UUID> firstList = Lists.newArrayList();
                    firstList.add(uuid);
                    final List<UUID> secondList = Lists.newArrayList();
                    secondList.add(secondUUID);
                    MatchManager match = new MatchManager();
                    match.startMath(firstList, secondList, ladder, ranked);
                    firstList.clear();
                    secondList.clear();
                    this.queue.remove(uuid);
                    this.queue.remove(secondUUID);
                    Practice.getInstance().getInventoryManager().updateQueueInventory(ranked);
                }
            }
        }
    }
    
    public void leaveQueue(final UUID uuid) {
    	final boolean ranked = this.queue.get(uuid).ranked;
    	if (ranked) {
    		Practice.getEloRangeMap().get(uuid).setOpponentFounded(true);
    		Practice.getEloRangeMap().remove(uuid);
    	}
        this.queue.remove(uuid);
        Practice.getInstance().getInventoryManager().updateQueueInventory(ranked);
        if (Bukkit.getServer().getPlayer(uuid) != null) {
            PlayerManager.getPlayers().get(uuid).sendToSpawn();
            Bukkit.getServer().getPlayer(uuid).sendMessage(MessageSerializer.REMOVE_QUEUE);
        }
    }
    
	public int getQueuedFromLadder(Ladders ladder, boolean ranked, boolean partyTo2) {
		int count = 0;
		for (Map.Entry<UUID, Queue> map : this.queue.entrySet()) {
			Queue value = map.getValue();
			if (value.getLadder() == ladder && value.isRanked() == ranked) {
				count++;
			}
		}
		return count;
	}
    
    public class Queue {
    	
        private boolean ranked;
        private Ladders ladder;
        private boolean partyTo2;
        
        public Queue(final boolean ranked, final Ladders ladder, final boolean partyTo2) {
            this.ranked = ranked;
            this.ladder = ladder;
            this.partyTo2 = partyTo2;
        }
        
        public boolean isRanked() {
            return this.ranked;
        }
        
        public Ladders getLadder() {
			return ladder;
		}
        
        public boolean isPartyTo2() {
			return partyTo2;
		}
    }
}
