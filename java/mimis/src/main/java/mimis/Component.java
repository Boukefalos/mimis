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
package mimis;

import mimis.input.Button;
import mimis.input.Feedback;
import mimis.input.Input;
import mimis.input.Task;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.input.state.State;
import mimis.parser.ParserInput;
import mimis.router.Router;
import mimis.state.TaskMap;
import mimis.value.Action;
import base.work.Listen;
import base.worker.Worker.Type;

public abstract class Component extends Listen<Input> {
    protected static final String TITLE = "Component";

    protected String title;
    protected Router router;

    public Component() {
        this(TITLE);
    }

    public Component(Type type) {
		super(type);
	}

    public Component(String title) {
        this.title = title;
    }

	public void setRouter(Router router) {
        this.router = router;        
    }

    public String getTitle() {
        return title;
    }

    public void listen(Class<? extends Input> clazz) {
        if (router == null) {
            logger.error("Router not set");
        } else {
            router.listen(this, clazz);
        }
    }

    public void ignore(Class<? extends Input> clazz) {
        if (router == null) {
            logger.error("Router not set");
        } else {
            router.ignore(this, clazz);
        }
    }

    public void route(Input input) {
        if (router == null) {
            logger.error("Router not set");
        } else {
            if (input instanceof State) {
                State state = (State) input;
                if (state.getComponent() == null) {
                    state.setComponent(this);
                }
            }
            router.add(input);
        }
    }

    public void input(Input input) {
        if (input instanceof State) {
            state((State) input);
        } else if (input instanceof Task) {
            task((Task) input);
        } else if (input instanceof Feedback) {
            feedback((Feedback) input);
        }
    }

    protected void state(State state) {
        Button button = state.getButton();
        if (state instanceof Press) {
            press(button);
        } else if (state instanceof Release) {
            release(button);
        }
    }

    protected void task(Task task) {
        Action action = task.getAction();
        switch (task.getSignal()) {
            case BEGIN:
                switch (action) {
                    case START:
                        start();
                        break;
                    case STOP:
                        stop();
                        break;
                    case EXIT:
                        exit();
                        break;
                    default:
                        begin(action);
                        break;
                }
                break;
            case END:
                end(action);
                break;
            default:
                action(action);
                break;
        }
    }

    protected void press(Button button) {}
    protected void release(Button button) {}
    protected void feedback(Feedback feedback) {}
    protected void action(Action action) {}
    protected void begin(Action action) {}
    protected void end(Action action) {}

    protected void parser(Action action, TaskMap taskMap) {
        route(new ParserInput(action, taskMap));
    }
}
