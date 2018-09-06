package net.nextinfinity.midicraft;

/**
 * This simple class serves to convert read MIDI data into a range better used within Minecraft.
 */
class NoteUtil {

	public static float getPitch(int note) {
		if(note > 23){
			float unalteredPitch = (float)Math.pow(2.0, ((double)note - 36.0) / 12.0);
			return unalteredPitch+2F;
		}
		return (float)Math.pow(2.0, ((double)note - 12.0) / 12.0);
	}

}