package test.lines;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Main {
	public static void main(String[] args) {
		System.out.println(System.getProperty("javax.sound.sampled.SourceDataLine"));

		new AudioFormat(44100, 16, 2, true, false);

		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			for (Line.Info lineInfo : mixer.getTargetLineInfo()) {
				try {
					Line line = mixer.getLine(lineInfo);
					if (mixer.isLineSupported(lineInfo)) {
						if (line instanceof TargetDataLine) {
							new TargetLine(mixer, (TargetDataLine) line);
						}/* else if (line instanceof SourceDataLine) {
							new SourceLine(mixer, (SourceDataLine) line);							
						}*/
					}
				} catch (LineUnavailableException e) {}
			}

		}
	}
}
