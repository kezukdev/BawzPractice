package kezuk.practice.event.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.practice.Practice;
import kezuk.practice.event.host.type.EventSubType;
import kezuk.practice.player.state.GlobalState;
import net.md_5.bungee.api.ChatColor;

public class JoinCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (Practice.getInstance().getRegisterCollections().getProfile().get(player.getUniqueId()).getGlobalState() != GlobalState.SPAWN) {
			sender.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "You can't do this right now.");
			return false;
		}
		if (args.length == 0) {
			player.openInventory(Practice.getInstance().getRegisterObject().getJoinInventory().getJoinInventory());
			return false;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("host")) {
				if (Practice.getInstance().getRegisterObject().getEvent().isLaunched() && Practice.getInstance().getRegisterObject().getEvent().getEventType().getSubType().equals(EventSubType.WAITTING)) {
					Practice.getInstance().getRegisterObject().getEvent().addMemberToEvent(player.getUniqueId());
				}
				else {
					player.sendMessage(ChatColor.AQUA + "Sorry but no events are currently available! Either it is already in progress or nobody has created one!");
				}
				return false;
			}
		}
		else {
			player.sendMessage(ChatColor.AQUA + "/join <host/tournaments>");
		}
		return false;
	}
}
