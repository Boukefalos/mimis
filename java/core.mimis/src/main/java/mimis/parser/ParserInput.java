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
package mimis.parser;

import mimis.Component;
import mimis.input.Input;
import mimis.state.TaskMap;
import mimis.value.Action;

public class ParserInput implements Input {
    protected static final long serialVersionUID = 1L;

    protected Action action;
    protected TaskMap taskMap;
    protected Component component;
    protected boolean end;

    public ParserInput(Action action, TaskMap taskMap) {
        this.action = action;
        this.taskMap = taskMap;
    }

    public ParserInput(Action action, Component component, boolean end) {
        this.action = action;
        this.component = component;
        this.end = end;
    }

    public Action getAction() {
        return action;
    }

    public TaskMap getTaskMap() {
        return taskMap;
    }

    public Component getComponent() {
        return component;
    }

    public boolean getEnd() {
        return end;
    }
}
