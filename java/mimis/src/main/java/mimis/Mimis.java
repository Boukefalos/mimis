package mimis;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Component;
import mimis.input.Feedback;
import mimis.input.Task;
import mimis.manager.Manager;
import mimis.parser.Parser;
import mimis.router.Router;
import mimis.util.ArrayCycle;
import mimis.value.Action;
import mimis.value.Target;

public abstract class Mimis extends Component {
    protected Component[] currentArray;
    protected Manager manager;

    protected ArrayCycle<Component> componentCycle;

    public Mimis(Component... currentArray) {
        this.currentArray = initialize(false, currentArray);        
        componentCycle = new ArrayCycle<Component>(currentArray);        
        router = new Router();
        manager = new Manager(initialize(true, router, new Parser()));
    }

    public void activate() throws ActivateException {
        manager.start();
        super.activate();
    }

    protected void deactivate() throws DeactivateException {
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