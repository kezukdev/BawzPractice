package kezuk.practice.arena.command;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import kezuk.practice.Practice;
import kezuk.practice.arena.Arena;
import kezuk.practice.arena.type.ArenaType;
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
                sender.sendMessage(ChatColor.DARK_AQUA + "Arena List:");
                Practice.getInstance().getRegisterCollections().getArena().forEach(arenaManager -> sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.AQUA + arenaManager.getName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + arenaManager.getArenaType().toString()));
            }
            else{
                sender.sendMessage(invalidArguement);
            }
        }else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("set1") || args[0].equalsIgnoreCase("setmid") || args[0].equalsIgnoreCase("set2"))
            {
                if (Arena.getArena(args[1]) != null) {
                    Arena arenaManager = Arena.getArena(args[1]);
                    Player player = (Player) sender;
                    if (args[0].equalsIgnoreCase("set1")) {
                        arenaManager.setLoc1(player.getLocation());
                        sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "You have been set the " + ChatColor.WHITE + "first" + ChatColor.DARK_AQUA + " location!");
                    }
                    else if (args[0].equalsIgnoreCase("setmid")) {
                        arenaManager.setMiddle(player.getLocation());
                        sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "You have been set the " + ChatColor.WHITE + "middle" + ChatColor.DARK_AQUA + " location!");
                    }
                    else if (args[0].equalsIgnoreCase("set2")) {
                        arenaManager.setLoc2(player.getLocation());
                        sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "You have been set the " + ChatColor.WHITE + "second" + ChatColor.DARK_AQUA + " location!");
                    }
                }else{
                    sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "This arena does not exist!");
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
                    if(Arena.getArena(args[2]) == null)
                    {
                        new Arena(args[2], ArenaType.valueOf(args[1].toUpperCase()));
                        sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "Arena" + ChatColor.RESET + ": " + args[2] + ChatColor.DARK_AQUA + " as been created into " + ChatColor.WHITE + args[1] + ChatColor.DARK_AQUA + " type!");
                    }else{
                    	sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "This arena already exist!");
                    }
                }else{
                	sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_AQUA + "This arena does not exist!");
                }
            }else{
                sender.sendMessage(invalidArguement);
            }
        }
		return false;
    }
}
