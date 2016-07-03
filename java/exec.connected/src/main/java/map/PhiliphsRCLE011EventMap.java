/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
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
import com.github.boukefalos.lirc.state.TaskMap;
import com.github.boukefalos.lirc.value.Action;
import com.github.boukefalos.lirc.value.Target;

import extra.Task;

public class PhiliphsRCLE011EventMap extends TaskMap {
    protected static final long serialVersionUID = 1L;
    
    public PhiliphsRCLE011EventMap() {
        /* Mimis */
        receive(PhiliphsRCLE011Button.UP, new Task(Action.NEXT, Target.MAIN));
        receive(PhiliphsRCLE011Button.DOWN, new Task(Action.PREVIOUS, Target.MAIN));

        /* Application */
        receive(PhiliphsRCLE011Button.POWER, new Task(Action.START, Target.CURRENT));
        receive(PhiliphsRCLE011Button.PROGRAM_UP, new Task(Action.NEXT, Target.CURRENT));
        receive(PhiliphsRCLE011Button.PROGRAM_DOWN, new Task(Action.PREVIOUS, Target.CURRENT));
        receive(PhiliphsRCLE011Button.LEFT, new Task(Action.REWIND, Target.CURRENT));
        receive(PhiliphsRCLE011Button.TUNE, new Task(Action.PLAY, Target.CURRENT));
        receive(PhiliphsRCLE011Button.RIGHT, new Task(Action.FORWARD, Target.CURRENT));
        receive(PhiliphsRCLE011Button.VOLUME_DOWN, new Task(Action.VOLUME_DOWN, Target.CURRENT));
        receive(PhiliphsRCLE011Button.MUTE, new Task(Action.MUTE, Target.CURRENT));
        receive(PhiliphsRCLE011Button.VOLUME_UP, new Task(Action.VOLUME_UP, Target.CURRENT));
        receive(PhiliphsRCLE011Button.CLOCK, new Task(Action.REPEAT, Target.CURRENT));
        receive(PhiliphsRCLE011Button.OUT, new Task(Action.SHUFFLE, Target.CURRENT));
        receive(PhiliphsRCLE011Button.SQUARE, new Task(Action.FULLSCREEN, Target.CURRENT));
        receive(PhiliphsRCLE011Button.RED, new Task(Action.DISLIKE, Target.CURRENT));
        receive(PhiliphsRCLE011Button.GREEN, new Task(Action.LIKE, Target.CURRENT));
    }
}
