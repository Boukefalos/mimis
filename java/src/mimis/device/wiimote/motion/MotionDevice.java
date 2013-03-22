package mimis.device.wiimote.motion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import mimis.device.lirc.LircButton;
import mimis.device.lirc.remote.PhiliphsRCLE011Button;
import mimis.device.wiimote.WiimoteDevice;
import mimis.exception.worker.ActivateException;
import mimis.exception.worker.DeactivateException;
import mimis.input.Button;
import mimis.input.button.ColorButton;
import mimis.input.button.NumberButton;
import mimis.input.state.State;
import mimis.value.Action;
import mimis.worker.Component;
import mimis.worker.Worker;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class MotionDevice extends Component {
    protected WiimoteDevice wiimoteDevice;
    protected int id;
    protected long start;
    protected boolean replay;
    protected Action action;

    public ReplayWorker replayWorker;
    public ArrayList<MotionData> motionList;

    public MotionDevice(WiimoteDevice wiimoteDevice) {
        this.wiimoteDevice = wiimoteDevice;
        id = 0;
        start = -1;
        replay = false;
        action = Action.TRAIN;
        replayWorker = new ReplayWorker();
        motionList = new ArrayList<MotionData>();
    }

    public void activate() throws ActivateException {
        super.activate();
        listen(State.class);
    }

    public void deactivate() throws DeactivateException {
        replayWorker.stop();
        super.deactivate();
    }

    public void exit() {
        super.exit();
        replayWorker.exit();
    }

    public void release(Button button) {
        if (button instanceof LircButton) {
            PhiliphsRCLE011Button lircButton = (PhiliphsRCLE011Button) button;
            log.debug(lircButton);
            switch (lircButton) {
                case CLOCK:
                    action = Action.TRAIN;
                    break;
                case OUT:
                    action = Action.RECOGNIZE;
                    break;
                case MUTE:
                    wiimoteDevice.begin(Action.CLOSE);
                    break;
                case SCREEN_UP:
                    wiimoteDevice.begin(Action.SAVE);
                    break;
                case SCREEN_DOWN:
                    wiimoteDevice.begin(Action.LOAD);
                    break;
            }
        } else if (button instanceof NumberButton) {
            NumberButton numberButton = (NumberButton) button;
            id = numberButton.ordinal();
            if (replay == false) {
                release(ColorButton.YELLOW);
            } else {
                log.debug("Set file to #" + id);
            }
        } else if (button instanceof ColorButton) {
            ColorButton colorButton = (ColorButton) button;
            log.debug(colorButton.name());
            synchronized (motionList) {
                switch (colorButton) {
                    case GREEN:
                        log.debug("Start capturing motion");
                        motionList.clear();
                        start = System.currentTimeMillis();
                        break;
                    case RED:
                        if (replayWorker.active()) {
                            log.debug("Stop replaying motion");
                            replayWorker.stop();
                        } else {
                            log.debug("Writing motion to file #" + id);
                            try {                            
                                FileOutputStream fileOutputStream = new FileOutputStream(String.format("tmp/motion #%d.bin", id));
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                objectOutputStream.writeObject(motionList.size());
                                for (MotionData motionData : motionList) {
                                    objectOutputStream.writeObject(motionData);
                                }
                                objectOutputStream.close();
                            } catch (IOException e) {
                                log.error(e);
                            }
                            motionList.clear();
                            start = -1;
                        }
                        break;
                    case YELLOW:
                        log.debug("Replaying motion from file #" + id);
                        replay = true;
                        replayWorker.start();
                        break;
                }
            }
        }
    }

    public void add(MotionSensingEvent event) {
        if (start > 0) {
            synchronized (motionList) {
                motionList.add(new MotionData(System.currentTimeMillis() - start, event));
            }
        }
    }

    class ReplayWorker extends Worker {
        protected ObjectInputStream objectInputStream;
        protected int count, i, time;

        protected void activate() throws ActivateException {
            try {
                FileInputStream fileInputStream = new FileInputStream(String.format("tmp/motion #%d.bin", id));
                objectInputStream = new ObjectInputStream(fileInputStream);
                count = (Integer) objectInputStream.readObject();
                i = time = 0;
                wiimoteDevice.begin(action);
                super.activate();
                return;
            } catch (FileNotFoundException e) {
                log.error(e);
            } catch (IOException e) {
                log.error(e);
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }

        protected void deactivate() throws DeactivateException {
            log.debug(String.format("Replay stopped (%d ms)", time));
            wiimoteDevice.end(action);
            replay = false;
            try {
                objectInputStream.close();
            } catch (IOException e) {
                log.debug(e);
            }
            super.deactivate();
        }
        protected void work() {
            if (i++ < count) {                
                try {
                    Object object = objectInputStream.readObject();
                    MotionData motionData = (MotionData) object;
                    wiimoteDevice.onMotionSensingEvent(motionData.getEvent());
                    sleep(motionData.getTime() - time);
                    time = motionData.getTime();                    
                    return;
                } catch (IOException e) {
                    log.error(e);
                } catch (ClassNotFoundException e) {
                    log.error(e);
                }
            }
            stop();
        }        
    }

}