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
package mimis.state;

import java.util.HashMap;

import mimis.input.Button;
import mimis.input.Input;
import mimis.input.Task;
import mimis.input.state.Press;
import mimis.input.state.State;
import mimis.input.state.sequence.Macro;
import mimis.input.state.sequence.Sequence;

public class TaskMap extends HashMap<Sequence, Task> implements Input {
    protected static final long serialVersionUID = 1L;

    public void add(Button button, Task task) {
        add(new Press(button), task);
    }

    public void add(Press press, Task task) {
        add(press, task, true);
    }

    protected void add(Press press, Task task, boolean macro) {
        if (macro) {
            add(new Macro(press), task);
        } else {
            add((State) press, task);
        }
    }

    public void add(State state, Task task) {
        add(new Sequence(state), task);
    }

    public void add(Sequence sequence, Task task) {
        put(sequence, task);
    }
}
