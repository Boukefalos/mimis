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
package mimis.device.panel;

import mimis.input.Task;
import mimis.input.state.Press;
import mimis.state.TaskMap;
import mimis.state.TaskMapCycle;
import mimis.value.Action;
import mimis.value.Target;

public class PanelTaskMapCycle extends TaskMapCycle {
    protected static final long serialVersionUID = 1L;

    public TaskMap player;

    public PanelTaskMapCycle() {
        /* Player */
        player = new TaskMap();
        player.add(new Press(PanelButton.UP), new Task(Action.PREVIOUS, Target.MAIN));
        player.add(new Press(PanelButton.PREVIOUS), new Task(Action.PREVIOUS, Target.CURRENT));
        player.add(new Press(PanelButton.REWIND), new Task(Action.REWIND, Target.CURRENT));
        player.add(new Press(PanelButton.STOP), new Task(Action.STOP, Target.CURRENT));
        player.add(new Press(PanelButton.PAUSE), new Task(Action.PAUSE, Target.CURRENT));
        player.add(new Press(PanelButton.PLAY), new Task(Action.PLAY, Target.CURRENT));
        player.add(new Press(PanelButton.FORWARD), new Task(Action.FORWARD, Target.CURRENT));
        player.add(new Press(PanelButton.DOWN), new Task(Action.NEXT, Target.MAIN));
        player.add(new Press(PanelButton.NEXT), new Task(Action.NEXT, Target.CURRENT));        player.add(new Press(PanelButton.VOLUME_DOWN), new Task(Action.VOLUME_DOWN, Target.CURRENT));
        player.add(new Press(PanelButton.MUTE), new Task(Action.MUTE, Target.CURRENT));
        player.add(new Press(PanelButton.VOLUME_UP), new Task(Action.VOLUME_UP, Target.CURRENT));
        player.add(new Press(PanelButton.REPEAT), new Task(Action.REPEAT, Target.CURRENT));
        player.add(new Press(PanelButton.SHUFFLE), new Task(Action.SHUFFLE, Target.CURRENT));
        add(player);
    }
}
