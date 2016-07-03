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

import mimis.input.Feedback;
import mimis.input.Task;
import mimis.manager.Manager;
import mimis.parser.Parser;
import mimis.router.Router;
import mimis.value.Action;
import mimis.value.Target;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.util.ArrayCycle;
import base.worker.Worker;

public abstract class Mimis extends Component {
    protected Component[] currentArray;
    protected Manager manager;
    protected Parser parser;

    protected ArrayCycle<Component> componentCycle;

    public Mimis(Component... currentArray) {
        super(Worker.Type.FOREGROUND);
        this.currentArray = initialize(false, currentArray);        
        componentCycle = new ArrayCycle<Component>(currentArray);        
        router = new Router();
        parser = new Parser();
        manager = new Manager(initialize(true, router, parser));
    }

    public void activate() throws ActivateException {
        manager.start();
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        manager.stop();
    }

    public void exit() {
        super.exit();
        manager.exit();
    }

    public Component[] initialize(boolean start, Component... componentArray) {
        for (Component component : componentArray) {
            component.setRouter(router);
            if (start) {
                component.start();
            }
        }
        return componentArray;
    }

    public void task(Task task) {
        if (task.getTarget().equals(Target.CURRENT)) {
            componentCycle.current().add(task);
        } else {
            super.task(task);
        }
    }

    public void end(Action action) {
        switch (action) {
            case CURRENT:
                route(new Feedback("Current component: " + componentCycle.current().getTitle()));
                break;
            case NEXT:
                logger.debug("Next component");
                route(new Feedback("Next component: " + componentCycle.next().getTitle()));
                break;
            case PREVIOUS:
                logger.debug("Previous component");
                route(new Feedback("Previous component: " + componentCycle.previous().getTitle()));
                break;
            case EXIT:
                exit();
                break;
            default:
                break;
        }
    }
}