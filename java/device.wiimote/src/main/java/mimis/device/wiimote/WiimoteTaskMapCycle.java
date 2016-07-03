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
package mimis.device.wiimote;

import mimis.input.Task;
import mimis.input.state.Release;
import mimis.state.TaskMap;
import mimis.state.TaskMapCycle;
import mimis.value.Action;
import mimis.value.Target;

public class WiimoteTaskMapCycle extends TaskMapCycle {
    protected static final long serialVersionUID = 1L;

    public TaskMap mimis, player, gesture, like;

    public WiimoteTaskMapCycle() {
        /* Mimis */
        mimis = new TaskMap();
        mimis.add(WiimoteButton.HOME, new Task(Action.NEXT, Target.MAIN));
        mimis.add(new Release(WiimoteButton.B), new Task(Action.UNSHIFT, Target.SELF));

        /* Gesture */
        gesture = new TaskMap();
        gesture.add(WiimoteButton.A, new Task(Action.TRAIN, Target.SELF));
        gesture.add(WiimoteButton.B, new Task(Action.SAVE, Target.SELF));
        gesture.add(WiimoteButton.DOWN, new Task(Action.LOAD, Target.SELF));
        gesture.add(WiimoteButton.HOME, new Task(Action.RECOGNIZE, Target.SELF));
        add(gesture);

        /* Player */
        player = new TaskMap();
        player.add(WiimoteButton.A, new Task(Action.PLAY, Target.CURRENT));
        player.add(WiimoteButton.B, new Task(Action.SHIFT, Target.SELF));
        player.add(WiimoteButton.HOME, new Task(Action.MUTE, Target.CURRENT));
        player.add(WiimoteButton.ONE, new Task(Action.SHUFFLE, Target.CURRENT));
        player.add(WiimoteButton.TWO, new Task(Action.REPEAT, Target.CURRENT));
        player.add(WiimoteButton.UP, new Task(Action.NEXT, Target.CURRENT));
        player.add(WiimoteButton.DOWN, new Task(Action.PREVIOUS, Target.CURRENT));    
        player.add(WiimoteButton.RIGHT, new Task(Action.FORWARD, Target.CURRENT));
        player.add(WiimoteButton.LEFT, new Task(Action.REWIND, Target.CURRENT));
        player.add(WiimoteButton.MINUS, new Task(Action.VOLUME_DOWN, Target.CURRENT));
        player.add(WiimoteButton.PLUS, new Task(Action.VOLUME_UP, Target.CURRENT));
        add(player);

        /* Like */
        like = new TaskMap();
        like.add(WiimoteButton.PLUS, new Task(Action.LIKE, Target.CURRENT));
        like.add(WiimoteButton.MINUS, new Task(Action.DISLIKE, Target.CURRENT));
        add(like);
    }
}
