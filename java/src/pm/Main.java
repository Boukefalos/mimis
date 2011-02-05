package pm;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import pm.action.Action;
import pm.application.Application;
import pm.application.voorbeeld.VoorbeeldApplication;
import pm.device.Device;
import pm.device.example.ExampleDevice;
import pm.device.rumblepad.RumblepadDevice;
import pm.exception.ActionException;
import pm.exception.ActionNotImplementedException;
import pm.exception.EventException;

public class Main extends Target {
    protected static final int SLEEP = 100;

    ArrayList<Application> applicationList;
    ArrayList<Device> deviceList;
    Queue<Action> actionQueue;

    boolean run;
    Application currentApplication;
    
    public Main() {
        applicationList = new ArrayList<Application>();
        //applicationList.iterator();
        deviceList = new ArrayList<Device>();
        actionQueue = new ConcurrentLinkedQueue<Action>();
        //JavaInputService.initialize();
        Device.initialise(actionQueue);
    }

    public void addApplication(Application application) {
        applicationList.add(application);
    }

    public boolean removeApplication(Application application) {
        return applicationList.remove(application);
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }

    public boolean removeDevie(Device device) {
        return deviceList.remove(device);
    }

    public void start() throws Exception {
        //addDevice(new ExampleDevice());
        addDevice(new RumblepadDevice());
   
        Application application = new VoorbeeldApplication();
        addApplication(application);
        currentApplication = application;

        for (Device device : deviceList) {
            device.start();
        }
        
        run();
    }

    public void run() throws ActionException, EventException {
        run = true;
        while (run) {
            //System.out.println("Print!");
            if (actionQueue.isEmpty()) {
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {}
            } else {
                Action action = actionQueue.poll();
                Target target;
                switch (action.getTarget()) {
                    case MAIN:
                        target = this;
                        invoke(action);
                        break;
                    case APPLICATION:
                        target = currentApplication;
                        break;
                    default:
                        throw new EventException("Unknown event type");
                }
                try {
                    target.invoke(action);
                } catch (ActionNotImplementedException e) {
                    // Todo: log.write(...)
                }
            }
        }
    }

    public void exit() {
        run = false;
        for (Device device : deviceList) {
            device.exit();
        }
        System.out.println("Als ie nu niet uit gaat, dan hebben we een verstekeling! Dat is vervelend ende naar!");
    }

    public static void main(String[] args) {
        try {
            new Main().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}