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
import javax.sound.sampled.TargetDataLine;

public class TargetLine {
    //private Mixer mixer;
    private TargetDataLine line;

    public TargetLine(Mixer mixer, TargetDataLine line) {
        //this.mixer = mixer;
        this.line = line;
        System.out.println("TARGET " + mixer.getMixerInfo().getName() + " || " + line.getLineInfo());
    }

    public int read(byte[] bytes, int offset, int length) {
        return line.read(bytes, offset, length);
    }
}
