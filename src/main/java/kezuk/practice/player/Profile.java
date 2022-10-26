package kezuk.practice.player;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.core.rank.Rank;
import kezuk.practice.core.tag.Tag;
import kezuk.practice.editor.Editor;
import kezuk.practice.event.host.oitc.stats.OitcStats;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.match.stats.MatchStats;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.request.Requesting.Request;
import net.minecraft.util.com.google.common.collect.Maps;

public class Profile {
	
	public UUID uuid;
	private int[] elos;
	private GlobalState globalState;
	private UUID matchUUID;
	private Player targetDuel;
	private Rank rank;
	private Tag tag;
	private SubState subState;
	private Date banExpiresOn;
	private String banReason;
	private boolean muted;
	private Date muteExpiresOn;
	private String muteReason;
	public MatchStats matchStats;
	public OitcStats oitcStats;
	public WeakHashMap<UUID, Request> request;
	private Editor editor;
	private HashMap<UUID, PermissionAttachment> permissible;
	
	public Profile(final UUID uuid) {
		this.uuid = uuid;
		this.globalState = GlobalState.SPAWN;
		this.rank = Rank.PLAYER;
		this.tag = Tag.NORMAL;
		this.subState = SubState.NOTHING;
		this.muted = false;
		for (Ladders ladder : Practice.getInstance().getLadder()) {
			if (ladder.isRanked()) {
				this.elos = new int[ladder.id()];	
			}
		}
        for(int i = 0; i <= elos.length-1; i++) elos[i] = 1200;
        this.permissible = Maps.newHashMap();
        Practice.getInstance().getRegisterCollections().getProfile().putIfAbsent(uuid, this);
        this.update();
	}
	
	private boolean existPlayer() {
        return Practice.getInstance().getDatabaseSQL().existPlayerManager(this.uuid);
    }
    
    private void update() {
        if (!this.existPlayer()) {
        	Practice.getInstance().getDatabaseSQL().createPlayerManager(this.uuid, Bukkit.getServer().getPlayer(uuid).getName());
        }
        else {
        	Practice.getInstance().getDatabaseSQL().updatePlayerManager(Bukkit.getServer().getPlayer(uuid).getName(), this.uuid);
            this.load();
        }
    }
    
    private void load() {
        try {
        	Date todayGlobal = new Date();
        	SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        	String today = s.format(todayGlobal);
        	if (s.parse(DB.getFirstRow("SELECT banExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("banExpires")) != null) {
            	this.banExpiresOn = s.parse(DB.getFirstRow("SELECT banExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("banExpires"));
            	this.banReason = DB.getFirstRow("SELECT banReason FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("banReason");	
        	}
        	if (this.banExpiresOn != null) {
        		if (s.parse(today) == this.banExpiresOn) {
            		this.banExpiresOn = null;
            		Bukkit.getPlayer(this.uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your banning punishment has been revoked. Avoid breaking the rules in the future!");	
        		}
        		else {
        			Bukkit.getPlayer(uuid).kickPlayer(ChatColor.AQUA + "Your account is currently suspended from accessing our server for the following reason: " + ChatColor.WHITE + this.banReason + "\n" + ChatColor.DARK_AQUA + "Expires: " + ChatColor.WHITE + this.banExpiresOn + "\n\n" + ChatColor.AQUA + "https://discord.gg/bawz");
        		}
        	}
        	this.muted = Boolean.valueOf(DB.getFirstRow("SELECT muted FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muted"));
        	if (s.parse(DB.getFirstRow("SELECT muteExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muteExpires")) != null) {
            	this.muteExpiresOn = s.parse(DB.getFirstRow("SELECT muteExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muteExpires"));
            	this.muteReason = DB.getFirstRow("SELECT muteReason FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muteReason");
        	}
        	if (this.muteExpiresOn != null) {
        		if (s.parse(today) == this.muteExpiresOn) {
            		this.muted = false;
            		this.muteExpiresOn = null;
            		Bukkit.getPlayer(this.uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your punishment for writing in the chat was revoked");	
        		}
        		else {
        			Bukkit.getPlayer(this.uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You currently have a punishment preventing you from talking in the chat!");
        		}
        	}
        	this.setRank(Rank.getRankByName(DB.getFirstRow("SELECT rank FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("rank")));
            this.registerPermissions();
        	this.setTag(Tag.getTagByName(DB.getFirstRow("SELECT tag FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("tag")));
            this.elos = getSplitValue(DB.getFirstRow("SELECT elos FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("elos"), ":");
        	editor = new Editor(uuid);
            editor.load();
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "All of your data has been loaded!");
        }
        catch (SQLException | ParseException e) {
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
    
	
	public void registerPermissions() {
		final Profile profile = Practice.getInstance().getRegisterCollections().getProfile().get(uuid);
		PermissionAttachment attachment = Bukkit.getPlayer(uuid).addAttachment(Practice.getInstance());
		this.getPermissible().put(uuid, attachment);
		for (String perm : profile.getRank().getPermissions()) {
			attachment.setPermission(perm, true);	
		}
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
	
	public SubState getSubState() {
		return subState;
	}
	
	public void setSubState(SubState subState) {
		this.subState = subState;
	}
	
	public HashMap<UUID, PermissionAttachment> getPermissible() {
		return permissible;
	}
	
	public OitcStats getOitcStats() {
		return oitcStats;
	}
	
    public Rank getRank() {
		return rank;
	}
    
    public void setRank(Rank rank) {
		this.rank = rank;
	}
    
    public Tag getTag() {
		return tag;
	}
    
    public void setTag(Tag tag) {
		this.tag = tag;
	}
    
    public void setTargetDuel(Player targetDuel) {
		this.targetDuel = targetDuel;
	}
    
    public Player getTargetDuel() {
		return targetDuel;
	}
    
    public WeakHashMap<UUID, Request> getRequest() {
		return request;
	}
	
	public int[] getElos() {
		return elos;
	}
	
	public GlobalState getGlobalState() {
		return globalState;
	}
	
	public void setGlobalState(GlobalState globalState) {
		this.globalState = globalState;
	}
	
	public UUID getMatchUUID() {
		return matchUUID;
	}
	
	public void setMatchUUID(UUID matchUUID) {
		this.matchUUID = matchUUID;
	}
	
	public MatchStats getMatchStats() {
		return matchStats;
	}

}
