package kezuk.bawz.command;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kezuk.bawz.player.PlayerManager;
import kezuk.bawz.player.Status;
import kezuk.bawz.utils.MessageSerializer;

public class BuildCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		if (!sender.isOp()) { sender.sendMessage(MessageSerializer.NO_PERMISSIONS); return false; }
		if (args.length > 0) {
			sender.sendMessage(MessageSerializer.BUILD_USAGE);
			return false;
		}
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayers().get(player.getUniqueId());
		if (pm.getPlayerStatus().equals(Status.BUILD)) {
			pm.sendToSpawn();
			player.setGameMode(GameMode.SURVIVAL);
			sender.sendMessage(MessageSerializer.BUILD_OFF);
			return false;
		}
		if (pm.getPlayerStatus() != Status.SPAWN) {
			sender.sendMessage(MessageSerializer.STATUS_NOT_ALLOWED);
			return false;
		}
		pm.setPlayerStatus(Status.BUILD);
		player.getInventory().clear();
		player.setGameMode(GameMode.CREATIVE);
		sender.sendMessage(MessageSerializer.BUILD_ON);
		return false;
	}

}
