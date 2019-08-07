package net.nextinfinity.midicraft;

import org.bukkit.Bukkit;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MusicPlayer {

	private final Map<Integer, List<Float>> notes = new HashMap<>();

	private final MidiCraft plugin;
	private Integer task;

	MusicPlayer(String fileName, MidiCraft plugin) throws InvalidMidiDataException, IOException {
		this.plugin = plugin;
		load(fileName);
	}

	/**
	 * Load the MIDI data from a midi file. Some tricky conversions are made here to to Minecraft's limitations of note possibitilies,
	 * as well as the limitations of 20-tick in music (not many common BPMs are easy to fit in this time.
	 *
	 * @param fileName The name of the file, including its extension, to load MIDI data from.
	 *                 The file must exist and contain valid MIDI data.
	 */
	private void load(String fileName) throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(new File(plugin.getDataFolder(), fileName));
		int tpb = sequence.getResolution();
		int ticks = tpb / 4;
		int trackNumber = 0;
		for (Track track : sequence.getTracks()) {
			trackNumber++;
			System.out.println("Track " + trackNumber + ": size = " + track.size());
			System.out.println();
			for (int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);
				Integer tick = Math.toIntExact(event.getTick());
				if (tick % ticks == 0) {
					MidiMessage message = event.getMessage();
					if (message instanceof ShortMessage) {
						ShortMessage sm = (ShortMessage) message;
						if (sm.getCommand() == 0x90) {
							int key = sm.getData1() - 30;
							if (key <= 48) {
								List<Float> list = notes.containsKey(tick) ? notes.get(tick) : new ArrayList<>();
								list.add(NoteUtil.getPitch(key));
								notes.put(tick / ticks, list);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Plays the loaded MIDI data from start.
	 */
	void play() {
		stop();
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new NoteTask(this), 0, 2);
	}

	/**
	 * Stops the MIDI playback.
	 */
	void stop() {
		if (task != null) {
			Bukkit.getScheduler().cancelTask(task);
			task = null;
		}
	}

	Map<Integer, List<Float>> getNotes() {
		return notes;
	}

}
