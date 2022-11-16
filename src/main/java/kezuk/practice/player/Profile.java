package kezuk.practice.player;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import co.aikar.idb.DB;
import kezuk.practice.Practice;
import kezuk.practice.core.rank.Rank;
import kezuk.practice.core.tag.Tag;
import kezuk.practice.editor.Editor;
import kezuk.practice.ladders.Ladders;
import kezuk.practice.player.cache.PlayerCache;
import kezuk.practice.player.personnal.PersonnalInventory;
import kezuk.practice.player.state.GlobalState;
import kezuk.practice.player.state.SubState;
import kezuk.practice.request.Requesting.Request;
import kezuk.practice.utils.NPCUtils;
import net.minecraft.util.com.google.common.collect.Maps;

public class Profile {
	
	public UUID uuid;
	private int[] elos;
	private GlobalState globalState;
	private UUID matchUUID;
	private Player targetDuel;
	private Rank rank;
	private Tag tag;
	private PlayerCache playerCache;
	private SubState subState;
	private boolean existData;
	public WeakHashMap<UUID, Request> request;
	private Editor editor;
	private HashMap<UUID, PermissionAttachment> permissible;
	private PersonnalInventory personnalInventory;
	
	public Profile(final UUID uuid) {
		this.uuid = uuid;
		this.globalState = GlobalState.SPAWN;
		this.rank = Rank.PLAYER;
		this.tag = Tag.NORMAL;
		this.subState = SubState.NOTHING;
		for (Ladders ladder : Practice.getInstance().getLadder()) {
			if (ladder.isRanked()) {
				this.elos = new int[ladder.id()];	
			}
		}
        for(int i = 0; i <= elos.length-1; i++) elos[i] = 1200;
        this.permissible = Maps.newHashMap(); 
        Practice.getInstance().getRegisterCollections().getProfile().putIfAbsent(uuid, this);
        this.update();
        this.personnalInventory = new PersonnalInventory(Bukkit.getPlayer(uuid));
        NPCUtils.showNPCtoPlayer(uuid);
	}
    
    private void update() {
        if (!Practice.getInstance().getDatabaseSQL().existPlayerManager(this.uuid)) {
        	Practice.getInstance().getDatabaseSQL().createPlayerManager(this.uuid, Bukkit.getServer().getPlayer(uuid).getName());
    		this.load();
        }
        else {
        	Practice.getInstance().getDatabaseSQL().updatePlayerManager(Bukkit.getServer().getPlayer(uuid).getName(), this.uuid);
    		this.load();
        }
    }
    
    public PersonnalInventory getPersonnalInventory() {
		return personnalInventory;
	}
    
    private void load() {
    	try {
        	Calendar calendar = Calendar.getInstance();
        	SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        	this.setRank(Rank.getRankByName(DB.getFirstRow("SELECT rank FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("rank")));
            this.registerPermissions();
    		this.playerCache = new PlayerCache(uuid);
    		this.playerCache.setScoreboard(Boolean.valueOf(DB.getFirstRow("SELECT scoreboard FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("scoreboard")));
    		this.playerCache.setPm(Boolean.valueOf(DB.getFirstRow("SELECT pm FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("pm")));
    		this.playerCache.setDuel(Boolean.valueOf(DB.getFirstRow("SELECT duel FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("duel")));
    		this.playerCache.getSettings().setPreviewSettings(uuid);
        	this.playerCache.banned = Boolean.valueOf(DB.getFirstRow("SELECT banned FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("banned"));
        	if (this.playerCache.banned) {
        		this.playerCache.banExpiresOn = s.parse(DB.getFirstRow("SELECT banExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("banExpires"));
            	this.playerCache.banReason = DB.getFirstRow("SELECT banReason FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("banReason");	
        	}
        	if (this.playerCache.banned) {
        		if (this.playerCache.banExpiresOn.after(calendar.getTime())) {
        			new BukkitRunnable() {
    					
    					@Override
    					public void run() {
    	        			Bukkit.getPlayer(uuid).kickPlayer(ChatColor.AQUA + "This user is currently banned from the network\n\n" + ChatColor.DARK_AQUA + "Reason: " + ChatColor.WHITE + playerCache.banReason + "\n" + ChatColor.DARK_AQUA + "Expires: " + ChatColor.WHITE + s.format(playerCache.banExpiresOn) + "\n\n" + ChatColor.AQUA + "https://discord.gg/bawz");
    					}
    				}.runTaskLater(Practice.getInstance(), 2L);
    				return;
        		}
        		else {
        			this.playerCache.banExpiresOn = null;
            		Bukkit.getPlayer(this.uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your banning punishment has been revoked. Avoid breaking the rules in the future!");	
        		}
        	}
        	this.playerCache.muted = Boolean.valueOf(DB.getFirstRow("SELECT muted FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muted"));
        	if (this.playerCache.muted) {
        		this.playerCache.muteExpiresOn = s.parse(DB.getFirstRow("SELECT muteExpires FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muteExpires"));
        		this.playerCache.muteReason = DB.getFirstRow("SELECT muteReason FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("muteReason");
        	}
        	if (this.playerCache.muteExpiresOn != null) {
        		if (this.playerCache.muteExpiresOn.before(calendar.getTime())) {
        			this.playerCache.muted = false;
        			this.playerCache.muteExpiresOn = null;
            		Bukkit.getPlayer(this.uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "Your punishment for writing in the chat was revoked");	
        		}
        		else {
        			Bukkit.getPlayer(this.uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "You currently have a punishment preventing you from talking in the chat!");
        		}
        	}
        	this.setTag(Tag.getTagByName(DB.getFirstRow("SELECT tag FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("tag")));
            this.elos = getSplitValue(DB.getFirstRow("SELECT elos FROM playersdata WHERE name=?", Bukkit.getServer().getPlayer(uuid).getName()).getString("elos"), ":");
        	editor = new Editor(uuid);
            editor.load();
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + " * " + ChatColor.AQUA + "All of your data has been loaded!");
		} catch (ParseException | SQLException e) {
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
	
	public void destroyProfile() {
		
	}
	
	public boolean isExistData() {
		return existData;
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
	
	public PlayerCache getPlayerCache() {
		return playerCache;
	}
}
