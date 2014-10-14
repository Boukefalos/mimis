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
    	return  getComponents(mimis.application.Application.class);
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

    protected void deactivate() throws DeactivateException {
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
        new Main().start(false);
    }
}
