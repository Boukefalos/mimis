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
package mimis.device.wiimote.motion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import mimis.Component;
import mimis.device.lirc.LircButton;
import mimis.device.lirc.remote.PhiliphsRCLE011Button;
import mimis.device.wiimote.WiimoteDevice;
import mimis.input.Button;
import mimis.input.button.ColorButton;
import mimis.input.button.NumberButton;
import mimis.input.state.State;
import mimis.value.Action;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public class MotionDevice extends Component {
    protected WiimoteDevice wiimoteDevice;
    protected int id;
    protected long start;
    protected boolean replay;
    protected Action action;

    public ReplayWork replayWork;
    public ArrayList<MotionData> motionList;

    public MotionDevice(WiimoteDevice wiimoteDevice) {
        this.wiimoteDevice = wiimoteDevice;
        id = 0;
        start = -1;
        replay = false;
        action = Action.TRAIN;
        replayWork = new ReplayWork();
        motionList = new ArrayList<MotionData>();
    }

    public void activate() throws ActivateException {
        super.activate();
        listen(State.class);
    }

    public void deactivate() throws DeactivateException {
        replayWork.stop();
        super.deactivate();
    }

    public void exit() {
        super.exit();
        replayWork.exit();
    }

    public void release(Button button) {
        if (button instanceof LircButton) {
            PhiliphsRCLE011Button lircButton = (PhiliphsRCLE011Button) button;
            logger.debug(lircButton.getName());
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
                default:
                    break;
            }
        } else if (button instanceof NumberButton) {
            NumberButton numberButton = (NumberButton) button;
            id = numberButton.ordinal();
            if (replay == false) {
                release(ColorButton.YELLOW);
            } else {
                logger.debug("Set file to #" + id);
            }
        } else if (button instanceof ColorButton) {
            ColorButton colorButton = (ColorButton) button;
            logger.debug(colorButton.name());
            synchronized (motionList) {
                switch (colorButton) {
                    case GREEN:
                        logger.debug("Start capturing motion");
                        motionList.clear();
                        start = System.currentTimeMillis();
                        break;
                    case RED:
                        if (replayWork.active()) {
                            logger.debug("Stop replaying motion");
                            replayWork.stop();
                        } else {
                            logger.debug("Writing motion to file #" + id);
                            try {                            
                                FileOutputStream fileOutputStream = new FileOutputStream(String.format("tmp/motion #%d.bin", id));
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                objectOutputStream.writeObject(motionList.size());
                                for (MotionData motionData : motionList) {
                                    objectOutputStream.writeObject(motionData);
                                }
                                objectOutputStream.close();
                            } catch (IOException e) {
                                logger.error("", e);
                            }
                            motionList.clear();
                            start = -1;
                        }
                        break;
                    case YELLOW:
                        logger.debug("Replaying motion from file #" + id);
                        replay = true;
                        replayWork.start();
                        break;
                    default:
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

    class ReplayWork extends Work {
        protected ObjectInputStream objectInputStream;
        protected int count, i, time;

        public void activate() throws ActivateException {
            try {
                FileInputStream fileInputStream = new FileInputStream(String.format("tmp/motion #%d.bin", id));
                objectInputStream = new ObjectInputStream(fileInputStream);
                count = (Integer) objectInputStream.readObject();
                i = time = 0;
                wiimoteDevice.begin(action);
                super.activate();
                return;
            } catch (FileNotFoundException e) {
                logger.error("", e);
            } catch (IOException e) {
                logger.error("", e);
            } catch (ClassNotFoundException e) {
                logger.error("", e);
            }
        }

        public void deactivate() throws DeactivateException {
            logger.debug(String.format("Replay stopped (%d ms)", time));
            wiimoteDevice.end(action);
            replay = false;
            try {
                objectInputStream.close();
            } catch (IOException e) {
                logger.debug("", e);
            }
            super.deactivate();
        }

        public void work() {
            if (i++ < count) {                
                try {
                    Object object = objectInputStream.readObject();
                    MotionData motionData = (MotionData) object;
                    wiimoteDevice.onMotionSensingEvent(motionData.getEvent());
                    sleep(motionData.getTime() - time);
                    time = motionData.getTime();                    
                    return;
                } catch (IOException e) {
                    logger.error("", e);
                } catch (ClassNotFoundException e) {
                    logger.error("", e);
                }
            }
            stop();
        }        
    }
}