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

import java.util.ArrayList;
import java.util.ServiceLoader;

import javax.swing.UIManager;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import mimis.input.Task;
import mimis.manager.ButtonManager;
import mimis.manager.CurrentButtonManager;
import mimis.value.Action;

public class Main extends Mimis {
    protected CurrentButtonManager applicationManager;
    protected ButtonManager deviceManager;
    protected Gui gui;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
    }

    public static Component[] getApplications() {
        return getComponents(mimis.application.Application.class);
    }

    public static Component[] getDevices() {
        return getComponents(mimis.device.Device.class);
    }

    public static Component[] getComponents(Class<?> clazz) {
        ArrayList<Component> componentList = new ArrayList<Component>();
        for (Object object : ServiceLoader.load(clazz)) {
            if (object instanceof Component) {
                componentList.add((Component) object);
            }
        }
        return componentList.toArray(new Component[]{});
    }

    public Main() {
        super(getApplications());

        /* Create gui from application and device managers */
        applicationManager = new CurrentButtonManager(router, componentCycle, "Applications", currentArray);
        deviceManager = new ButtonManager("Devices", initialize(false, getDevices()));
        gui = new Gui(this, applicationManager, deviceManager);
        manager.add(initialize(false, gui));
    }

    public void activate() throws ActivateException {
        super.activate();
        listen(Task.class);

        /* Start managers */
        applicationManager.start();
        deviceManager.start();

        /* Force display of current component when gui started */
        gui.start();
        while (!gui.active());
        end(Action.CURRENT);
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();

        logger.debug("Stop managers");
        applicationManager.stop();
        deviceManager.stop();
    }

    public void exit() {
        super.exit();

        logger.debug("Exit managers");
        applicationManager.exit();
        deviceManager.exit();
        gui.exit();
        router.exit();
        parser.exit();
    }

    public void end(Action action) {
        super.end(action);
        switch (action) {
            case CURRENT:
            case NEXT:
            case PREVIOUS:
                applicationManager.currentChanged();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
