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

import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class SourceLine {
    //private Mixer mixer;
    private SourceDataLine line;

    public SourceLine(Mixer mixer, SourceDataLine line) {
        //this.mixer = mixer;
        this.line = line;
        System.out.println("SOURCE " + mixer.getMixerInfo().getName() + " || " + line.getLineInfo());
    }

    public int write(byte[] bytes, int offset, int length) {
        return line.write(bytes, offset, length);
    }
}
