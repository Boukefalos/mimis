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
package mimis.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import base.exception.worker.ActivateException;
import mimis.Component;
import mimis.input.Input;
import mimis.input.Task;
import mimis.input.state.State;
import mimis.input.state.sequence.Sequence;
import mimis.state.Active;
import mimis.state.TaskMap;
import mimis.value.Action;
import mimis.value.Signal;
import mimis.value.Target;

public class Parser extends Component {
    protected TaskMap taskMap;
    protected ArrayList<Sequence> sequenceList;
    protected HashMap<Component, ArrayList<Active>> activeMap;

    public Parser() {
        taskMap = new TaskMap();
        sequenceList = new ArrayList<Sequence>();
        activeMap = new HashMap<Component, ArrayList<Active>>();
    }

    public void activate() throws ActivateException {
        listen(ParserInput.class);
        listen(State.class);
        super.activate();
    }

    public void input(Input input) {
        if (input instanceof ParserInput) {
            input((ParserInput) input);            
        } else if (input instanceof State) {
            input((State) input);            
        }       
    }

    public void input(ParserInput parserInput) {
        logger.trace("input(ParserInput)");
        Action action = parserInput.getAction();
        try {
            switch (action) {
                case ADD:
                    add(parserInput.getTaskMap());
                    return;
                case REMOVE:
                    remove(parserInput.getTaskMap());
                    return;
                case RESET:
                    reset(parserInput.getComponent(), parserInput.getEnd());
                    return;
            }
        } catch (NullPointerException e) {
            logger.error("Illegal use of ParserInput");
        }
    }

    public void input(State state) {
        //log.trace("input(State)");
        Component component = state.getComponent();
        if (!activeMap.containsKey(component)) {
            activeMap.put(component, new ArrayList<Active>());
        }
        ArrayList<Active> activeList = activeMap.get(component);
        for (Sequence sequence : sequenceList) {
            activeList.add(new Active(sequence));
        }
        ArrayList<Active> removeList = new ArrayList<Active>();
        for (Active active : activeList) {
            if (active.next(state)) {
                Task task = taskMap.get(active.getSequence());
                if (active.first()) {
                    route(component, (Task) task, Signal.BEGIN);
                }
                if (active.last()) {
                    route(component, (Task) task, Signal.END);
                    removeList.add(active);
                }
            } else {
                removeList.add(active);
            }
        }
        for (Active active : removeList) {
            activeList.remove(active);
        }
        activeMap.put(component, activeList);
        if (activeList.isEmpty()) {
            activeMap.remove(activeList);
        }
    }

    protected void add(TaskMap taskMap) {
        logger.trace("add(TaskMap)");
        this.taskMap.putAll(taskMap);
        sequenceList.addAll(taskMap.keySet());
    }

    protected void remove(TaskMap taskMap) {
        for (Entry<Sequence, Task> eventEntry : taskMap.entrySet()) {
            Sequence sequence = eventEntry.getKey();
            this.taskMap.remove(sequence);
            sequenceList.remove(sequence);
            for (Entry<Component, ArrayList<Active>> activeEntry : activeMap.entrySet()) {
                ArrayList<Active> activeList = activeEntry.getValue();
                activeList.remove(sequence);
            }
        }
    }

    protected void reset(Component component, boolean end) {
        ArrayList<Active> activeList = activeMap.get(component);
        if (end) {
            for (Active active : activeList) {
                Task task = taskMap.get(active.getSequence());
                route(component, task, Signal.END);
            }
        }
        activeList.clear();
    }

    protected void route(Component component, Task task, Signal signal) {
        task = task.setSignal(signal);
        if (task.getTarget().equals(Target.SELF)) {
            component.add(task);
        } else {
            route(task);
        }        
    }
}
