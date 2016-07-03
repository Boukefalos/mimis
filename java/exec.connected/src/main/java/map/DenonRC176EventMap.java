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

import com.github.boukefalos.lirc.button.remote.DenonRC176Button;
import com.github.boukefalos.lirc.state.TaskMap;
import com.github.boukefalos.lirc.value.Action;
import com.github.boukefalos.lirc.value.Target;

import extra.Task;

public class DenonRC176EventMap extends TaskMap {
    protected static final long serialVersionUID = 1L;
    
    public DenonRC176EventMap() {
        /* Mimis */
        receive(DenonRC176Button.TUNER_UP, new Task(Action.NEXT, Target.MAIN));
        receive(DenonRC176Button.TUNER_DOWN, new Task(Action.PREVIOUS, Target.MAIN));

        /* Application */
        receive(DenonRC176Button.AMP_POWER, new Task(Action.START, Target.CURRENT));
        receive(DenonRC176Button.CD_NEXT, new Task(Action.NEXT, Target.CURRENT));
        receive(DenonRC176Button.CD_PREVIOUS, new Task(Action.PREVIOUS, Target.CURRENT));
        receive(DenonRC176Button.TAPE_REWIND, new Task(Action.REWIND, Target.CURRENT));
        receive(DenonRC176Button.CD_PLAY, new Task(Action.PLAY, Target.CURRENT));
        receive(DenonRC176Button.CD_PAUSE, new Task(Action.PLAY, Target.CURRENT));
        receive(DenonRC176Button.TAPE_FORWARD, new Task(Action.FORWARD, Target.CURRENT));
        receive(DenonRC176Button.AMP_MUTE, new Task(Action.MUTE, Target.CURRENT));
        receive(DenonRC176Button.AMP_VOLUME_UP, new Task(Action.VOLUME_UP, Target.CURRENT));
        receive(DenonRC176Button.AMP_VOLUME_DOWN, new Task(Action.VOLUME_DOWN, Target.CURRENT));
        receive(DenonRC176Button.CD_REPEAT, new Task(Action.REPEAT, Target.CURRENT));
        receive(DenonRC176Button.CD_SHUFFLE, new Task(Action.SHUFFLE, Target.CURRENT));
        receive(DenonRC176Button.TAPE_AB, new Task(Action.LIKE, Target.CURRENT));
        receive(DenonRC176Button.TAPE_REC, new Task(Action.DISLIKE, Target.CURRENT));
    }
}
