package mimis.input.state.sequence;

import java.util.ArrayList;

import mimis.exception.macro.StateOrderException;
import mimis.input.Button;
import mimis.input.state.Hold;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.input.state.State;


public class Macro extends Sequence {
    public Macro(Press press) {
        Button button = press.getButton();
        this.eventArray = new State[] {press, new Release(button)};
    }

    public Macro(State... eventArray) throws StateOrderException {
        ArrayList<Button> holdList = new ArrayList<Button>();
        ArrayList<State> eventList = new ArrayList<State>();
        for (State state : eventArray) {
            Button button = state.getButton();
            if (state instanceof Press) {
                if (holdList.contains(button)) {
                    throw new StateOrderException("Press events cannot follow hold events for the same button.");
                }
                eventList.add(state);
                eventList.add(new Release(button));
            } else if (state instanceof Release) {
                if (!holdList.contains(button)) {
                    throw new StateOrderException("Cannot release a button that is not held.");
                }
                holdList.remove(button);
                eventList.add(state);
            } else if (state instanceof Hold) {
                if (holdList.contains(button)) {
                    throw new StateOrderException("Cannot hold a button more than once.");
                }
                holdList.add(button);
                eventList.add(new Press(button));
            }
        }
        if (!holdList.isEmpty()) {
            throw new StateOrderException("One or more buttons are not released.");
        }        
        this.eventArray = (State[]) eventList.toArray(new State[0]);
    }
}