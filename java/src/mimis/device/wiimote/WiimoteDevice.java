package mimis.device.wiimote;

import java.io.IOException;

import mimis.device.Device;
import mimis.device.wiimote.gesture.GestureDevice;
import mimis.device.wiimote.motion.MotionDevice;
import mimis.exception.button.UnknownButtonException;
import mimis.exception.device.DeviceNotFoundException;
import mimis.exception.worker.ActivateException;
import mimis.exception.worker.DeactivateException;
import mimis.input.Button;
import mimis.input.Feedback;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.util.ArrayCycle;
import mimis.value.Action;
import mimis.value.Signal;
import mimis.worker.Component;
import mimis.worker.Worker;

import org.wiigee.event.GestureEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.util.Log;

import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;

public class WiimoteDevice extends Component implements Device, GestureListener {
    protected static final String TITLE = "Wiimote";
    protected static final int RUMBLE = 50;
    protected static final int CONNECTED_TIMEOUT = 500;
    protected static final int LED_TIMEOUT = 1000;
    protected static final int LED_SLEEP = 50;

    protected static WiimoteService wiimoteService;
    protected WiimoteTaskMapCycle taskMapCycle;
    protected Wiimote wiimote;
    protected boolean connected;
    protected GestureDevice gestureDevice;
    protected MotionDevice motionDevice;
    protected int gestureId;
    protected LedWorker ledWorker;
    protected boolean disconnect;

    static {
        WiimoteDevice.wiimoteService = new WiimoteService();
        Log.setLevel(Log.DEBUG);
    }

    public WiimoteDevice() {
        super(TITLE);
        taskMapCycle = new WiimoteTaskMapCycle();
        gestureDevice = new GestureDevice();
        gestureDevice.add(this);
        motionDevice = new MotionDevice(this);
        gestureId = 0;
        ledWorker = new LedWorker();
    }

    /* Worker */
    protected void activate() throws ActivateException {
    	if (wiimote == null) {
            motionDevice.setRouter(router);
            motionDevice.start();
            parser(Action.ADD, taskMapCycle.player);
        }
        try {
            connect();
        } catch (DeviceNotFoundException e) {
            log.warn(e);
        }
        super.activate();
    }

    public synchronized boolean active() {
        if (wiimote != null) {
            connected = false;
            wiimote.getStatus();
            try {
                wait(CONNECTED_TIMEOUT);
            } catch (InterruptedException e) {
                log.error(e);
            }
            if (!connected) {
                try {
                    connect();
                } catch (DeviceNotFoundException e) {
                    disconnect = true;
                    stop();
                }
            }
        }
        return active;
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        ledWorker.stop();
        motionDevice.stop();
        if (disconnect && wiimote != null) {
            wiimote.disconnect();
            disconnect = false;
        }
    }

    public void exit() {
        super.exit();
        ledWorker.exit();
        if (wiimote != null) {
            wiimote.disconnect();
            wiimote = null;
        }
        wiimoteService.exit();
        motionDevice.exit();
    }

    /* Events */
    public void begin(Action action) {
        switch (action) {
            case SHIFT:
                log.debug("Shift");
                parser(Action.RESET, taskMapCycle.player);
                parser(Action.ADD, taskMapCycle.mimis);
                parser(Action.ADD, taskMapCycle.like);
                break;
            case UNSHIFT:
                log.debug("Unshift");
                parser(Action.RESET, taskMapCycle.mimis);
                parser(Action.RESET, taskMapCycle.like);
                parser(Action.ADD, taskMapCycle.player);
                break;
            case RECOGNIZE:
                log.debug("Gesture recognize press");
                gestureDevice.recognize(Signal.BEGIN);
                break;
            case TRAIN:
                log.debug("Gesture train press");
                gestureDevice.train(Signal.BEGIN);
                break;
            case CLOSE:
                log.debug("Gesture close press");
                gestureDevice.close(Signal.BEGIN);
                break;
            case SAVE:
                log.debug("Gesture save");
                gestureDevice.close(Signal.END);
                gestureDevice.saveGesture(gestureId, "tmp/gesture #" + gestureId);
                ++gestureId;
                break;
            case LOAD:
                log.debug("Gesture load");
                for (int i = 0; i < gestureId; ++i) {
                    gestureDevice.loadGesture("tmp/gesture #" + gestureId);
                }
                break;
        }
    }

    public void end(Action action) {
        switch (action) {
            case RECOGNIZE:
                log.debug("Gesture recognize release");
                gestureDevice.recognize(Signal.END);
                break;
            case TRAIN:
                log.debug("Gesture train release");
                gestureDevice.train(Signal.END);
                break;
            case CLOSE:
                log.debug("Gesture close release");
                gestureDevice.close(Signal.END);
                break;
        }
    }

    public void feedback(Feedback feedback) {
        if (wiimote != null && active()) {
            log.debug("Wiimote rumble feedback");
            wiimote.rumble(RUMBLE);
        }
    }

    /* Connectivity */
    public synchronized void connect() throws DeviceNotFoundException {
        wiimote = wiimoteService.getDevice(this);
        //wiimote.activateContinuous();
        //wiimote.activateMotionSensing();
        ledWorker.start();
    }

    /* Listeners */
    public void onButtonsEvent(WiimoteButtonsEvent event) {
    	if (!active) {
    		return;
    	}
        int pressed = event.getButtonsJustPressed() - event.getButtonsHeld();
        int released = event.getButtonsJustReleased();
        try {
            if (pressed != 0 && released == 0) {
                Button button = WiimoteButton.create(pressed);
                log.trace("Press: " + button);
                route(new Press(button));
            } else if (pressed == 0 && released != 0) {
                Button button = WiimoteButton.create(released);
                log.trace("Release: " + button);
                route(new Release(button));
            }
        } catch (UnknownButtonException e) {}
    }

    public void onMotionSensingEvent(MotionSensingEvent event) {
    	if (!active) {
    		return;
    	}
        gestureDevice.add(event.getGforce());
        motionDevice.add(event);
    }

    public void onIrEvent(IREvent event) {
        
    }

    public void gestureReceived(GestureEvent event) {
        log.debug(event.isValid());
        if (event.isValid()) {
            System.out.printf("id #%d, prob %.0f%%, valid %b\n", event.getId(), 100 * event.getProbability(), event.isValid());
        }
    }

    class LedWorker extends Worker {
        protected ArrayCycle<Integer> ledCycle;

        public LedWorker() {
            ledCycle = new ArrayCycle<Integer>();
            ledCycle.add(1);
            ledCycle.add(3);
            ledCycle.add(6);
            ledCycle.add(12);
            ledCycle.add(8);
            ledCycle.add(12);
            ledCycle.add(6);
            ledCycle.add(3);
            ledCycle.add(1);
        }

        public void activate() throws ActivateException {
            sleep(LED_TIMEOUT);
            super.activate();
        }
        
        public void deactivate() throws DeactivateException {
            super.deactivate();
            setLeds(1);
        }

        protected void work() {
            setLeds(ledCycle.next());
            sleep(LED_SLEEP);
        }

        protected void setLeds(int leds) {
            if (wiimote != null) {
                wiimote.setLeds((leds & 1) > 0, (leds & 2) > 0, (leds & 4) > 0, (leds & 8) > 0);
                sleep((leds == 8 ? 2 : 1) * LED_SLEEP);
            }
        }
    }
}
