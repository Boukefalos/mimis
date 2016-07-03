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
        logger.debug(component + " is listening to " + clazz);
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

    public void input(Input input) {
        for (Entry<Component, ArrayList<Class<? extends Input>>> entry : listenMap.entrySet()) {
            Component component = entry.getKey();

            if (input instanceof Task) {
                System.err.println(component + " " + target((Task) input, component));
                
                Task x = (Task) input;
                System.err.println(x.getTarget());
                
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
