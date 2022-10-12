package kezuk.bawz.command;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import kezuk.bawz.arena.*;
import net.minecraft.util.org.apache.commons.lang3.EnumUtils;

import org.bukkit.*;

public class ArenaCommand implements CommandExecutor {
	
	String invalidArguement = ChatColor.RED + "Invalid argument!";
	
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if(!(sender instanceof Player)) return true;
        if (!sender.hasPermission("practice.command.arena")) {
        	sender.sendMessage(ChatColor.RED + "You don't have required permissions!");
        	return true;
        }
        if(args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.DARK_RED + "Correct Usage: /arena");
            sender.sendMessage(ChatColor.RED + "    list");
            sender.sendMessage(ChatColor.RED + "    create <arenaType> <name>");
            sender.sendMessage(ChatColor.RED + "    setspawn<1/2> <name>");
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
            if(args[0].equalsIgnoreCase("setspawn1") || args[0].equalsIgnoreCase("setspawn2"))
            {
                if (ArenaManager.getArena(args[1]) != null) {
                    ArenaManager arenaManager = ArenaManager.getArena(args[1]);
                    Player player = (Player) sender;
                    if (args[0].equalsIgnoreCase("setspawn1")) {
                        arenaManager.setLoc1(player.getLocation());
                        sender.sendMessage(ChatColor.GREEN + "You have set the spawn 1 of arena " + ChatColor.LIGHT_PURPLE + arenaManager.getName());
                    } else if (args[0].equalsIgnoreCase("setspawn2")) {
                        arenaManager.setLoc2(player.getLocation());
                        sender.sendMessage(ChatColor.GREEN + "You have set the spawn 2 of arena " + ChatColor.LIGHT_PURPLE + arenaManager.getName());
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "This arena does'nt exist!");
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
                        sender.sendMessage(ChatColor.GREEN + "You have create the "+args[1]+" arena " + ChatColor.LIGHT_PURPLE + args[2]);
                    }else{
                        sender.sendMessage(ChatColor.RED + "This arena does'nt exist!");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "This type doesn't exist!");
                }
            }else{
                sender.sendMessage(invalidArguement);
            }
        }
		return false;
    }
}
