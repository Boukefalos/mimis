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
package mimis.device.jintellitype;

import mimis.input.Task;
import mimis.state.TaskMap;
import mimis.state.TaskMapCycle;
import mimis.value.Action;
import mimis.value.Key;
import mimis.value.Target;

public class JIntellitypeTaskMapCycle extends TaskMapCycle {
    protected static final long serialVersionUID = 1L;
    
    public TaskMap mimis, player;
    
    public JIntellitypeTaskMapCycle() {
        /* Mimis */
        mimis = new TaskMap();
        mimis.add(
            new Hotkey(Key.PRIOR),
            new Task(Action.PREVIOUS, Target.MAIN));
        mimis.add(
            new Hotkey(Key.NEXT),
            new Task(Action.NEXT, Target.MAIN));
        add(mimis);
        
        /* Player */
        player = new TaskMap();
        player.add(
            CommandButton.VOLUME_DOWN,
            new Task(Action.VOLUME_DOWN, Target.APPLICATIONS));
        player.add(
            CommandButton.VOLUME_UP,
            new Task(Action.VOLUME_UP, Target.APPLICATIONS));
        player.add(
            new Hotkey(Modifier.CTRL | Modifier.WIN, 'x'),
            new Task(Action.EXIT, Target.MAIN));
        player.add(
            new Hotkey(Modifier.CTRL | Modifier.SHIFT | Modifier.WIN, 'n'),
            new Task(Action.NEXT, Target.CURRENT));
        player.add(
            new Hotkey(Modifier.CTRL | Modifier.SHIFT | Modifier.WIN, 'p'),
            new Task(Action.PREVIOUS, Target.CURRENT));
        add(player);
    }
}
