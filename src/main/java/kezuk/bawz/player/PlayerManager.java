package kezuk.bawz.player;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;
import co.aikar.idb.DB;
import kezuk.bawz.*;
import kezuk.bawz.core.Tag;
import kezuk.bawz.host.HostStats;
import kezuk.bawz.host.PlayerHostStatus;
import kezuk.bawz.ladders.Ladders;
import kezuk.bawz.match.*;
import kezuk.bawz.request.DuelRequestStatus;
import kezuk.bawz.utils.*;

import java.sql.SQLException;
import java.util.*;
import net.minecraft.util.com.google.common.collect.*;

public class PlayerManager {
    private UUID uuid;
    private Status playerStatus;
    private UUID matchUUID;
    private UUID hostUUID;
    private Long nextHitTick;
    private DuelRequestStatus duelRequest;
    private PlayerHostStatus hostStatus;
    private HostStats hostStats;
    private MatchStats matchStats;
    private Player targetDuel;
    private Tag tag;
    private int[] elos;
    public static HashMap<UUID, PlayerManager> players;
    
    public PlayerManager(final UUID uuid) {
        this.uuid = uuid;
		for (Ladders ladder : Practice.getInstance().getLadder()) this.elos = new int[ladder.id()];
        for(int i = 0; i <= elos.length-1; i++) elos[i] = 1200;
        this.nextHitTick = 0L;
        this.matchStats = new MatchStats();
        this.hostStats = new HostStats();
        this.tag = Tag.NORMAL;
        this.duelRequest = DuelRequestStatus.CAN;
        PlayerManager.players.putIfAbsent(uuid, this);
		this.update();
        Bukkit.getPlayer(uuid).sendMessage(MessageSerializer.DATA_LOADED);
    }
    
    public void sendToSpawn() {
        Practice.getInstance().getItemsManager().giveSpawnItems(Bukkit.getServer().getPlayer(this.uuid));
        Bukkit.getServer().getPlayer(this.uuid).teleport(new Location(Bukkit.getWorld("world"), 135.556D, 126.50000D, 6.540D, 45.3f, -0.8f));
        Bukkit.getServer().getPlayer(this.uuid).setHealth(Bukkit.getServer().getPlayer(this.uuid).getMaxHealth());
        Bukkit.getServer().getPlayer(this.uuid).setFoodLevel(20);
        Bukkit.getServer().getPlayer(this.uuid).setSaturation(20);
        Bukkit.getServer().getPlayer(this.uuid).extinguish();
        Bukkit.getServer().getPlayer(this.uuid).setArrowsStuck(0);
        Bukkit.getServer().getPlayer(this.uuid).setMaximumNoDamageTicks(10);
        Bukkit.getServer().getPlayer(this.uuid).setLevel(0);
        Bukkit.getServer().getPlayer(this.uuid).setExp(0.0f);
        Bukkit.getServer().getPlayer(this.uuid).setAllowFlight(false);
        Bukkit.getServer().getPlayer(this.uuid).setFlying(false);
        players.get(uuid).setPlayerStatus(Status.SPAWN);
        for (final PotionEffect effect : Bukkit.getServer().getPlayer(this.uuid).getActivePotionEffects()) {
            Bukkit.getServer().getPlayer(this.uuid).removePotionEffect(effect.getType());
        }
    }
    
    public HostStats getHostStats() {
		return hostStats;
	}
    
    public void setHostStats(HostStats hostStats) {
		this.hostStats = hostStats;
	}
    
    public void setHostStatus(PlayerHostStatus hostStatus) {
		this.hostStatus = hostStatus;
	}
    
    public PlayerHostStatus getHostStatus() {
		return hostStatus;
	}
    
    public void setTargetDuel(Player targetDuel) {
		this.targetDuel = targetDuel;
	}
    
    public Player getTargetDuel() {
		return targetDuel;
	}
    
    public void setDuelRequest(DuelRequestStatus duelRequest) {
		this.duelRequest = duelRequest;
	}
    
    public DuelRequestStatus getDuelRequest() {
		return duelRequest;
	}
    
    public UUID getHostUUID() {
		return hostUUID;
	}
    
    public void setHostUUID(UUID hostUUID) {
		this.hostUUID = hostUUID;
	}
    
    public void setTag(Tag tag) {
		this.tag = tag;
	}
    
    public Tag getTag() {
		return tag;
	}
    
    public MatchStats getMatchStats() {
        return this.matchStats;
    }
    
    public void setMatchStats(final MatchStats matchStats) {
        this.matchStats = matchStats;
    }
    
    public void sendToQueue() {
        Practice.getInstance().getItemsManager().giveLeaveItems(Bukkit.getServer().getPlayer(this.uuid), "Queue");
        Bukkit.getServer().getPlayer(this.uuid).sendMessage(MessageSerializer.ADDED_TO_QUEUE);
    }
    
    public Long getNextHitTick() {
        return this.nextHitTick;
    }
    
    public void updateNextHitTick() {
        this.nextHitTick = System.currentTimeMillis() + 500L;
    }
    
    public UUID getMatchUUID() {
        return this.matchUUID;
    }
    
    public void setMatchUUID(final UUID matchUUID) {
        this.matchUUID = matchUUID;
    }
    
    public Status getPlayerStatus() {
        return this.playerStatus;
    }
    
    public void setPlayerStatus(final Status playerStatus) {
        this.playerStatus = playerStatus;
    }
    
    public static HashMap<UUID, PlayerManager> getPlayers() {
        return PlayerManager.players;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
	private boolean existPlayer() {
        return Practice.getInstance().databaseSQL.existPlayerManager(this.uuid);
    }
    
    private void update() {
        if (!this.existPlayer()) {
            Practice.getInstance().databaseSQL.createPlayerManager(this.uuid, Bukkit.getServer().getPlayer(uuid).getName());
        }
        else {
            Practice.getInstance().databaseSQL.updatePlayerManager(Bukkit.getServer().getPlayer(uuid).getName(), this.uuid);
            this.load();
        }
    }
    
    private void load() {
        try {
        	this.tag = Tag.getTagByName(DB.getFirstRow("SELECT tag FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("tag"));
            this.elos = getSplitValue(DB.getFirstRow("SELECT elos FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("elos"), ":");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int[] getSplitValue(final String string, final String spliter) {
        final String[] split = string.split(spliter);
        final int[] board = new int[split.length];
        for (int i = 0; i <= split.length - 1; ++i) {
            board[i] = Integer.parseInt(split[i]);
        }
        return board;
    }
    
    public static String getStringValue(final int[] board, final String spliter) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= board.length - 1; ++i) {
            stringBuilder.append(board[i]);
            if (i != board.length - 1) {
                stringBuilder.append(spliter);
            }
        }
        return stringBuilder.toString();
    }
    
    public int[] getElos() {
		return elos;
	}
    
    static {
        PlayerManager.players = Maps.newHashMap();
    }
}
