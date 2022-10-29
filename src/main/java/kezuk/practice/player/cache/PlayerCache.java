package kezuk.practice.player.cache;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;

import co.aikar.idb.DB;
import kezuk.practice.core.staff.cache.StaffCache;
import kezuk.practice.event.host.oitc.stats.OitcStats;
import kezuk.practice.match.stats.MatchStats;

public class PlayerCache {
	
	private UUID uuid;
	public boolean banned;
	public Date banExpiresOn;
	public String banReason;
	public boolean muted;
	public Date muteExpiresOn;
	public String muteReason;
	public MatchStats matchStats;
	public OitcStats oitcStats;
	public boolean frozen;
	private StaffCache staffCache;
	
	public PlayerCache(final UUID uuid) {
		this.uuid = uuid;
		this.muted = false;
		this.banned = false;
		this.frozen = false;
		if (Bukkit.getPlayer(uuid).hasPermission("bawz.moderation")) {
			this.staffCache = new StaffCache(uuid);
		}
	}
	
	public Date getMuteExpiresOn() {
    	SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	try {
			this.muteExpiresOn = s.parse(DB.getFirstRow("SELECT muteExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muteExpires"));
		} catch (ParseException | SQLException e) {
			e.printStackTrace();
		}
		return muteExpiresOn;
	}
	
	public void setMuteReason(String muteReason) {
		this.muteReason = muteReason;
	}
	
	public void setMuteExpiresOn(Date muteExpiresOn) {
		this.muteExpiresOn = muteExpiresOn;
	}
	
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	public boolean isMuted() {
		return muted;
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public String getBanReason() {
		return banReason;
	}
	
	public String getMuteReason() {
		return muteReason;
	}
	
	public Date getBanExpiresOn() {
		return banExpiresOn;
	}

	public OitcStats getOitcStats() {
		return oitcStats;
	}
	
	public MatchStats getMatchStats() {
		return matchStats;
	}
	
	public StaffCache getStaffCache() {
		return staffCache;
	}
}
