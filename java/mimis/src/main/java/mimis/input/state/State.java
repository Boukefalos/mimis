package mimis.input.state;

import mimis.Component;
import mimis.input.Button;
import mimis.input.Input;
import mimis.input.state.State;

public abstract class State implements Input {
    protected static final long serialVersionUID = 1L;

    protected Button button;
    protected Component component;

    public Button getButton() {
        return button;
    }

    public State(Button button) {
        this.button = button;
    }

    public void setComponent(Component component) {
        this.component = component;        
    }

    public Component getComponent() {
        return component;
    }

    public boolean equals(State state, boolean type) {
        return (type || state.getClass().equals(getClass())) && state.getButton().equals(button);
    }
}
