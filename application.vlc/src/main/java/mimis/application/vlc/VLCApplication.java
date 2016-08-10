/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
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
package mimis.application.vlc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;
import mimis.application.cmd.CMDApplication;
import mimis.value.Action;
import mimis.value.Amount;
import util.Native;
import value.Registry;

public class VLCApplication extends CMDApplication {
    protected final static Registry REGISTRY = Registry.CLASSES_ROOT;
    protected final static String KEY = "Applications\\vlc.exe\\shell\\Open\\command";
    protected final static String PROGRAM = "vlc.exe";
    protected final static String TITLE = "VLC media player";

    protected static final int POSTION_CHANGE_RATE = 1;
    protected static final int VOLUME_CHANGE_RATE = 20;

    protected static final String HOST = "localhost";
    protected static final int PORT = 8080;

    protected static final int VOLUME_SLEEP = 100;
    protected static final int SEEK_SLEEP = 100;
    
    protected VolumeWork volumeWorker;
    protected SeekWork seekWorker;
    
    protected int volume = 255;
    protected boolean muted = false;

    public VLCApplication() {
        super(PROGRAM, TITLE);
        volumeWorker = new VolumeWork();
        seekWorker = new SeekWork();
    }

    public String getPath() {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(Native.getValue(REGISTRY, KEY));
        return matcher.find() ? matcher.group(1) : null;
    }

    public void command(String command) {
        String request = String.format("http://%s:%d/requests/status.xml?command=%s", HOST, PORT, command);
        try {
            URL url = new URL(request);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            int response = httpUrlConnection.getResponseCode();
            logger.trace("Response: " + response);
        } catch (MalformedURLException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        volumeWorker.stop();
        seekWorker.stop();
        Native.terminate(program);
    }

    public void exit() {
        super.exit();
        volumeWorker.exit();
        seekWorker.exit();
    }

    public void begin(Action action) {
        logger.trace("VLCApplication begin: " + action);
        try {
            switch (action) {
                case VOLUME_UP:                    
                    volumeWorker.activate("+");    
                    break;
                case VOLUME_DOWN:
                    volumeWorker.activate("-");
                    break;
                case FORWARD:
                    seekWorker.start(Amount.SMALL, "+");
                    break;
                case REWIND:
                    seekWorker.start(Amount.SMALL, "-");
                    break;
                default:
                    break;
            }
        } catch (ActivateException e) {
            logger.error("", e);
        }
    }
    
    public void end(Action action) {
        logger.trace("VLCApplication end: " + action);
        switch (action) {
            case PLAY:
                command("pl_pause");
                break;
            case PAUSE:
                command("pl_pause");
                break;
            case NEXT:
                command("pl_next");
                break;
            case PREVIOUS:
                command("pl_previous");
                break;
            case FORWARD:
            case REWIND:
                seekWorker.stop();
                break;
            case MUTE:
                command("volume&val=" + toggleMute());
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWorker.stop();
                break;
            case SHUFFLE:
                command("command=pl_random");
                break;
            case REPEAT:
                command("command=pl_repeat");
                break;
            default:
                break;
        }
    }
    
    protected void volumeUp() {
        if (!muted) {
            volume += VOLUME_CHANGE_RATE;
            command("volume&val=+" + VOLUME_CHANGE_RATE);
        }
    }
    
    protected void volumeDown() {
        if (!muted) {
            volume -= VOLUME_CHANGE_RATE;
            command("volume&val=-" + VOLUME_CHANGE_RATE);
        }
    }

    protected int toggleMute() {
        return (muted = !muted) ? 0 : volume;
    }

    public String getTitle() {
        return TITLE;
    }
    
    protected class VolumeWork extends Work {
        protected String volumeChangeSign;

        public void activate(String volumeChangeSign) throws ActivateException {
            super.activate();
            this.volumeChangeSign = volumeChangeSign;
        }

        public void work() {
            volume += VOLUME_CHANGE_RATE;
            command("volume&val=" + volumeChangeSign + VOLUME_CHANGE_RATE);
            sleep(VOLUME_SLEEP);
        }
    };

    protected class SeekWork extends Work {
        protected Amount amount;
        protected String seekDirection;

        public void start(Amount amount, String seekDirection) {
            super.start();
            this.amount = amount;
            this.seekDirection = seekDirection;
        }

        public void work() {
            switch (amount) {
                case SMALL:
                    command("command=seek&val=" + seekDirection + POSTION_CHANGE_RATE);
                    break;
                case MEDIUM:
                    command("command=seek&val=" + seekDirection + POSTION_CHANGE_RATE * 2);
                    break;
                case LARGE:
                    command("command=seek&val=" + seekDirection + POSTION_CHANGE_RATE * 3);
                    break;
            }
            sleep(SEEK_SLEEP);
        }
    };
}
