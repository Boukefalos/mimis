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
