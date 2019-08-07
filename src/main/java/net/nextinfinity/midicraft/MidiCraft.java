package net.nextinfinity.midicraft;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

/**
 * The base JavaPlugin class of the MidiCraft plugin.
 */
public class MidiCraft extends JavaPlugin {

	private final HashMap<Player, MusicPlayer> players = new HashMap<>();

	private final static String PREFIX = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "MidiCraft" + ChatColor.DARK_PURPLE + "] ";

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("midi")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can use MidiCraft!");
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 1) {
				player.sendMessage(PREFIX + "MidiCraft by NextInfinity");
				player.sendMessage(PREFIX + "/midi load [file] - Create a new music player with the specified file");
				return true;
			}

			MusicPlayer mp = players.get(player);
			switch (args[0].toLowerCase()) {
				case ("load"):
					String fileName = args[1];
					player.sendMessage(PREFIX + "Attempting to load " + fileName + "...");
					if (new File(this.getDataFolder(), fileName).exists()) {
						try {
							players.put(player, new MusicPlayer(fileName, this));
						} catch (Exception e) {
							player.sendMessage(PREFIX + "Error loading " + fileName + "! See console for log.");
							e.printStackTrace();
						}
					} else {
						player.sendMessage(PREFIX + ChatColor.RED + "That file doesn't exist!");
					}
					break;
				case ("play"):
					if (mp != null) {
						mp.play();
					} else {
						player.sendMessage(PREFIX + "You don't currently have a music player!");
					}
					break;
				case ("stop"):
					if (mp != null) {
						mp.stop();
					} else {
						player.sendMessage(PREFIX + "You don't currently have a music player!");
					}
					break;
				case ("clear"):
					if (mp != null) {
						mp.stop();
						players.remove(player);
					} else {
						player.sendMessage(PREFIX + "You don't currently have a music player!");
					}
					break;
			}
		}
		return true;
	}

}
