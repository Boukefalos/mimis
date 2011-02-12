package pm.macro;

import java.util.ArrayList;
import java.util.HashMap;

import pm.Macro;
import pm.Task;
import pm.exception.MacroException;
import pm.task.TaskProvider;

public class MacroListener {
    public ArrayList<Macro> macroList;
    public HashMap<Macro, Task> taskMap;
    public ArrayList<Active> activeList;

    public MacroListener() {
        macroList = new ArrayList<Macro>();
        taskMap = new HashMap<Macro, Task>();
        activeList = new ArrayList<Active>();
    }

    public void add(Macro macro, Task task) {
        macroList.add(macro);
        taskMap.put(macro, task);
    }

    public void add(Event event, Task task) throws MacroException {
        add(new Macro(event), task);
    }

    public void add(Event event) {
        for (Macro macro : macroList) {
            activeList.add(new Active(macro));
        }
        ArrayList<Active> removeList = new ArrayList<Active>();
        for (Active active : activeList) {
            if (active.next(event)) {
                if (active.last()) {
                    TaskProvider.add(taskMap.get(active.getMacro()));
                    removeList.add(active);
                }
            } else {
                removeList.add(active);
            }
        }
        for (Active active : removeList) {
            activeList.remove(active);
        }
    }
}
