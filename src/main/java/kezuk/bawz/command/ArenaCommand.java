package kezuk.bawz.command;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import kezuk.bawz.arena.*;
import kezuk.bawz.utils.MessageSerializer;
import net.minecraft.util.org.apache.commons.lang3.EnumUtils;

import org.bukkit.*;

public class ArenaCommand implements CommandExecutor {
	
	String invalidArguement = ChatColor.RED + "Invalid argument!";
	
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if(!(sender instanceof Player)) return true;
        if (!sender.hasPermission("bawz.arena")) {
        	sender.sendMessage(ChatColor.AQUA + "You don't have required permissions!");
        	return true;
        }
        if(args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.AQUA + "/arena list");
            sender.sendMessage(ChatColor.AQUA + "/arena create <arenaType> <name>");
            sender.sendMessage(ChatColor.AQUA + "/arena set<1/mid/2> <name>");
        }else if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.DARK_RED + "Arena List:");
                ArenaManager.getAll().forEach(arenaManager -> sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + arenaManager.getName() + ChatColor.GRAY + " -> " + ChatColor.RED + arenaManager.getArenaType().toString()));
            }
            else{
                sender.sendMessage(invalidArguement);
            }
        }else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("set1") || args[0].equalsIgnoreCase("setmid") || args[0].equalsIgnoreCase("set2"))
            {
                if (ArenaManager.getArena(args[1]) != null) {
                    ArenaManager arenaManager = ArenaManager.getArena(args[1]);
                    Player player = (Player) sender;
                    if (args[0].equalsIgnoreCase("set1")) {
                        arenaManager.setLoc1(player.getLocation());
                        sender.sendMessage(MessageSerializer.ARENA_LOC);
                    }
                    else if (args[0].equalsIgnoreCase("setmid")) {
                        arenaManager.setMiddle(player.getLocation());
                        sender.sendMessage(MessageSerializer.ARENA_LOC);
                    }
                    else if (args[0].equalsIgnoreCase("set2")) {
                        arenaManager.setLoc2(player.getLocation());
                        sender.sendMessage(MessageSerializer.ARENA_LOC);
                    }
                }else{
                    sender.sendMessage(MessageSerializer.ARENA_NOT_EXIST);
                }
            } else{
                sender.sendMessage(invalidArguement);
            }
        }else  if(args.length == 3)
        {
            if(args[0].equalsIgnoreCase("create"))
            {
                if(EnumUtils.isValidEnum(ArenaType.class, args[1].toUpperCase()))
                {
                    if(ArenaManager.getArena(args[2]) == null)
                    {
                        new ArenaManager(args[2], ArenaType.valueOf(args[1].toUpperCase()));
                        sender.sendMessage(MessageSerializer.ARENA_CREATED);
                    }else{
                        sender.sendMessage(MessageSerializer.ARENA_EXIST);
                    }
                }else{
                    sender.sendMessage(MessageSerializer.ARENA_NOT_EXIST);
                }
            }else{
                sender.sendMessage(invalidArguement);
            }
        }
		return false;
    }
}
