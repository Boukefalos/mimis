package mimis.event;

import mimis.Event;
import mimis.value.Action;

public abstract class EventHandler extends EventListener {
    protected static EventRouter eventRouter;

    public static void initialise(EventRouter eventRouter) {
        EventHandler.eventRouter = eventRouter;
    }

    public void event(Event event) {
        if (event instanceof Feedback) {
            feedback((Feedback) event);
        } else if (event instanceof Task) {
            task((Task) event);
        }
    }

    protected void feedback(Feedback feedback) {}

    protected void task(Task task) {
        action(task.getAction());
    }

    protected void action(Action action) {}
}