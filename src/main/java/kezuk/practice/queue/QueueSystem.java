package kezuk.practice.queue;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import kezuk.practice.Practice;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.StartMatch;
import kezuk.practice.party.items.PartyItems;
import kezuk.practice.player.Profile;
import kezuk.practice.player.items.SpawnItems;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.queue.task.EloRange;
import kezuk.practice.utils.ItemSerializer;

public class QueueSystem {
	
	public void addPlayerToQueue(final List<UUID> uuids, final Ladders ladder, final boolean ranked, final boolean to2) {
		for (UUID uuid : uuids) {
			if(!Practice.getInstance().getRegisterCollections().getQueue().containsKey(uuid)) {
	            final Profile pm = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
	            Practice.getInstance().getRegisterCollections().getQueue().putIfAbsent(uuid, new QueueEntry(uuids, ladder, ranked, to2));
				if (pm.getGlobalState().equals(GlobalState.SPAWN)) {
		            pm.setGlobalState(GlobalState.QUEUE);	
				}
				else {
					pm.setSubState(SubState.QUEUE);
				}
	            Bukkit.getPlayer(uuid).getInventory().clear();
	            Bukkit.getPlayer(uuid).getInventory().setItem(8, ItemSerializer.serialize(new ItemStack(Material.BLAZE_POWDER), (short)0, ChatColor.GRAY + " * " + ChatColor.AQUA + "Leave Queue."));
	            Bukkit.getPlayer(uuid).updateInventory();
	            Bukkit.getPlayer(uuid).sendMessage(ChatColor.DARK_AQUA + "You have joined the queue with " + ChatColor.WHITE + ChatColor.stripColor(ladder.displayName()) + ChatColor.DARK_AQUA + " as your game mode!");
	            Practice.getInstance().getRegisterObject().getLadderInventory().refreshInventory();
	            if (ranked) {
	                new EloRange(uuid, ladder);
	            }
			}
			if (!Practice.getInstance().getRegisterCollections().getQueue().isEmpty()) {
				UUID possibleUUID;
				for (final Map.Entry<UUID, QueueEntry> map : Practice.getInstance().getRegisterCollections().getQueue().entrySet()) {
	                final UUID key = map.getKey();
	                final QueueEntry value = map.getValue();
	                if (uuid != key && ranked == value.isRanked() && to2 == value.isTo2()) {
	                    if (ladder != value.getLadder()) {
	                        continue;
	                    }
	                    possibleUUID = key;
	                    if (possibleUUID == uuid) {
	                        return;
	                    }
	                    if (value.isRanked()) {
	                    	return;
	                    }
	                    final List<UUID> firstList = Lists.newArrayList();
	                    final List<UUID> secondList = Lists.newArrayList();
                    	firstList.add(uuid);
                        secondList.add(possibleUUID);
                        new StartMatch(firstList, secondList, null, ladder, ranked, to2, false);
	                    firstList.clear();
	                    secondList.clear();
	                    Practice.getInstance().getRegisterCollections().getQueue().remove(uuid);
	                    Practice.getInstance().getRegisterCollections().getQueue().remove(possibleUUID);
	                    Practice.getInstance().getRegisterObject().getLadderInventory().refreshInventory();
	                }
				}
			}	
		}
	}
	
    public void leaveQueue(final UUID uuid) {
    	if (Practice.getInstance().getRegisterCollections().getQueue().get(uuid).isRanked()) {
    		this.getEloRange().get(uuid).setOpponentFounded(true);
    		this.getEloRange().remove(uuid);
    	}
    	Practice.getInstance().getRegisterCollections().getQueue().remove(uuid);
        Practice.getInstance().getRegisterObject().getLadderInventory().refreshInventory();
        if (Bukkit.getServer().getPlayer(uuid) != null) {
        	if (Practice.getInstance().getRegisterCollections().getProfile().get(uuid).getGlobalState().equals(GlobalState.QUEUE)) {
                new SpawnItems(uuid, false);
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have left the queue.");	
        	}
        	else {
                Practice.getInstance().getRegisterCollections().getProfile().get(uuid).setSubState(SubState.NOTHING);
                new PartyItems(Bukkit.getPlayer(uuid));
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You have left 2v2 the queue.");
        	}
        }
    }
	
	public ConcurrentMap<UUID, QueueEntry> getQueue() {
		return Practice.getInstance().getRegisterCollections().getQueue();
	}
	
	public ConcurrentMap<UUID, EloRange> getEloRange() {
		return Practice.getInstance().getRegisterCollections().getEloRange();
	}
	
	public class QueueEntry {
		
		private List<UUID> uuid;
		private Ladders ladder;
		private boolean ranked;
		private boolean to2;
		
		public QueueEntry(final List<UUID> uuid, final Ladders ladder, final boolean ranked, final boolean to2) {
			this.uuid = uuid;
			this.ladder = ladder;
			this.ranked = ranked;
			this.to2 = to2;
		}
		
		public List<UUID> getUuid() {
			return uuid;
		}
		
		public Ladders getLadder() {
			return ladder;
		}
		
		public boolean isRanked() {
			return ranked;
		}
		
		public boolean isTo2() {
			return to2;
		}
	}

}
