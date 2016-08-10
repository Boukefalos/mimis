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
package mimis.device.javainput.rumblepad;

import mimis.device.javainput.DirectionButton;
import mimis.input.Task;
import mimis.input.state.Press;
import mimis.state.TaskMap;
import mimis.state.TaskMapCycle;
import mimis.value.Action;
import mimis.value.Target;

public class RumblepadTaskMapCycle extends TaskMapCycle {
    protected static final long serialVersionUID = 1L;

    public TaskMap mimis, player, like;

    public RumblepadTaskMapCycle() {
        /* Mimis */
        mimis = new TaskMap();
        mimis.add(
            new Press(RumblepadButton.ONE),
            new Task(Action.PREVIOUS, Target.MAIN));
        mimis.add(
            new Press(RumblepadButton.THREE),
            new Task(Action.NEXT, Target.MAIN));
        add(mimis);
        
        /* Player */
        player = new TaskMap();
        player.add(
            new Press(DirectionButton.WEST),
            new Task(Action.PLAY, Target.CURRENT));
        player.add(
            new Press(DirectionButton.EAST),
            new Task(Action.MUTE, Target.CURRENT));
        player.add(
            new Press(RumblepadButton.NINE),
            new Task(Action.SHUFFLE, Target.CURRENT));
        player.add(
            new Press(RumblepadButton.TEN),
            new Task(Action.REPEAT, Target.CURRENT));
        player.add(
            new Press(RumblepadButton.EIGHT),
            new Task(Action.NEXT, Target.CURRENT));
        player.add(
            new Press(RumblepadButton.SIX),
            new Task(Action.PREVIOUS, Target.CURRENT));    
        player.add(
            new Press(RumblepadButton.SEVEN),
            new Task(Action.FORWARD, Target.CURRENT));
        player.add(
            new Press(RumblepadButton.FIVE),
            new Task(Action.REWIND, Target.CURRENT));
        player.add(
            new Press(DirectionButton.SOUTH),
            new Task(Action.VOLUME_DOWN, Target.CURRENT));
        player.add(
            new Press(DirectionButton.NORTH),
            new Task(Action.VOLUME_UP, Target.CURRENT));
        add(player);

        like = new TaskMap();
        like.add(
            new Press(RumblepadButton.FOUR),
            new Task(Action.LIKE, Target.CURRENT));
        like.add(
            new Press(RumblepadButton.TWO),
            new Task(Action.DISLIKE, Target.CURRENT));
        add(like);
    }
}
