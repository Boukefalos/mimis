/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
