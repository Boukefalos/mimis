package pm;

import pm.event.EventHandler;
import pm.selector.Activatable;

public abstract class Application extends EventHandler implements Activatable {
    protected String title;
    protected boolean active;

    public Application(String title) {
        this.title = title;
        active = false;
    }

    public String title() {
        return title;
    }

    public boolean active() {
        return active;
    }

    public void activate() {
        start();
        active = true;
    }

    public void deactivate() {
        stop();
        active = false;
    }

    public void exit() {
        deactivate();
        stop();
    }
}