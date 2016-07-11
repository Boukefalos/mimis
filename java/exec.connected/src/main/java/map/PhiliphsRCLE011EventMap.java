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
package map;

import com.github.boukefalos.lirc.button.remote.PhiliphsRCLE011Button;

import mimis.input.Task;
import mimis.state.TaskMap;
import mimis.value.Action;
import mimis.value.Target;

public class PhiliphsRCLE011EventMap extends TaskMap {
    protected static final long serialVersionUID = 1L;
    
    public PhiliphsRCLE011EventMap() {
        /* Mimis */
        add(PhiliphsRCLE011Button.UP, new Task(Action.NEXT, Target.MAIN));
        add(PhiliphsRCLE011Button.DOWN, new Task(Action.PREVIOUS, Target.MAIN));

        /* Application */
        add(PhiliphsRCLE011Button.POWER, new Task(Action.START, Target.CURRENT));
        add(PhiliphsRCLE011Button.PROGRAM_UP, new Task(Action.NEXT, Target.CURRENT));
        add(PhiliphsRCLE011Button.PROGRAM_DOWN, new Task(Action.PREVIOUS, Target.CURRENT));
        add(PhiliphsRCLE011Button.LEFT, new Task(Action.REWIND, Target.CURRENT));
        add(PhiliphsRCLE011Button.TUNE, new Task(Action.PLAY, Target.CURRENT));
        add(PhiliphsRCLE011Button.RIGHT, new Task(Action.FORWARD, Target.CURRENT));
        add(PhiliphsRCLE011Button.VOLUME_DOWN, new Task(Action.VOLUME_DOWN, Target.CURRENT));
        add(PhiliphsRCLE011Button.MUTE, new Task(Action.MUTE, Target.CURRENT));
        add(PhiliphsRCLE011Button.VOLUME_UP, new Task(Action.VOLUME_UP, Target.CURRENT));
        add(PhiliphsRCLE011Button.CLOCK, new Task(Action.REPEAT, Target.CURRENT));
        add(PhiliphsRCLE011Button.OUT, new Task(Action.SHUFFLE, Target.CURRENT));
        add(PhiliphsRCLE011Button.SQUARE, new Task(Action.FULLSCREEN, Target.CURRENT));
        add(PhiliphsRCLE011Button.RED, new Task(Action.DISLIKE, Target.CURRENT));
        add(PhiliphsRCLE011Button.GREEN, new Task(Action.LIKE, Target.CURRENT));
    }
}
