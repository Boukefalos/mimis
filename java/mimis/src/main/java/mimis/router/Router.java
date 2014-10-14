package mimis.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import mimis.Component;
import mimis.Main;
import mimis.application.Application;
import mimis.device.Device;
import mimis.input.Input;
import mimis.input.Task;

public class Router extends Component {
    protected HashMap<Component, ArrayList<Class<? extends Input>>> listenMap;

    public Router() {
        listenMap = new HashMap<Component, ArrayList<Class<? extends Input>>>();
    }

    public synchronized void listen(Component component, Class<? extends Input> clazz) {
        if (!listenMap.containsKey(component)) {
            listenMap.put(component, new ArrayList<Class<? extends Input>>());
        }
        ArrayList<Class<? extends Input>> listenList = listenMap.get(component);
        if (!listenList.contains(clazz)) {
            listenList.add(clazz);
        }
    }

    public synchronized void ignore(Component component, Class<?> clazz) {
        if (listenMap.containsKey(component)) {
            ArrayList<Class<? extends Input>> listenList = listenMap.get(component);
            listenList.remove(clazz);
            if (listenList.isEmpty()) {
                listenMap.remove(listenList);
            }
        }        
    }

    public synchronized void input(Input input) {
        for (Entry<Component, ArrayList<Class<? extends Input>>> entry : listenMap.entrySet()) {
            Component component = entry.getKey();

            if (input instanceof Task) {
                if (!target((Task) input, component)) {
                    continue;
                }
            }

            ArrayList<Class<? extends Input>> listenList = entry.getValue();
            for (Class<?> clazz : listenList) {
                if (clazz.isInstance(input)) {
                    component.add(input);
                }
            }
        }
    }

    protected boolean target(Task task, Component component) {
        switch (task.getTarget()) {
            case ALL:
                return true;
            case MAIN:
            case CURRENT:
                return component instanceof Main;
            case DEVICES:
                return component instanceof Device;
            case APPLICATIONS:
                return component instanceof Application;
            default:
                return false;
        }
    }
}
