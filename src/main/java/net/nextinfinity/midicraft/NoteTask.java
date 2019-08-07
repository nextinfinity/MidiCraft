package net.nextinfinity.midicraft;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * The NoteTask class exists to play the loaded MIDI notes in the proper timings, and is meant to be run as a repeating task by the Bukkit scheduler.
 */
class NoteTask implements Runnable {

	private int tick = 0;
	private final MusicPlayer player;

	NoteTask(MusicPlayer player) {
		this.player = player;
	}

	@Override
	public void run() {
		if (player.getNotes().containsKey(tick)) {
			for (Float note : player.getNotes().get(tick)) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (note > 2F) {
						note -= 2F;
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, note);
					}
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0F, note);
				}
			}
		}
		tick++;
	}

}
