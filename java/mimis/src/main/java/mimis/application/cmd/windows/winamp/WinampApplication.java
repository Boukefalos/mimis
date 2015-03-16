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
package mimis.application.cmd.windows.winamp;

import base.exception.worker.DeactivateException;
import base.worker.Worker;
import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;
import mimis.value.Command;

public class WinampApplication extends WindowsApplication {
    protected final static String PROGRAM = "winamp.exe";
    protected final static String TITLE = "Winamp";
    protected final static String WINDOW = "Winamp v1.x";

    protected final static int STATUS_PLAYING = 1;
    protected final static int STATUS_PAUSED = 3;
    protected final static int STATUS_STOPPED = 0;

    protected final static int IPC_ISPLAYING = 104;
    protected final static int IPC_GETOUTPUTTIME = 105;
    protected final static int IPC_SETVOLUME = 122;

    protected final static int WINAMP_FILE_QUIT     = 40001;
    protected final static int WINAMP_FILE_REPEAT   = 40022;
    protected final static int WINAMP_FILE_SHUFFLE  = 40023;
    protected final static int WINAMP_BUTTON1       = 40044;
    protected final static int WINAMP_BUTTON2       = 40045;
    protected final static int WINAMP_BUTTON3       = 40046;
    protected final static int WINAMP_BUTTON5       = 40048;   
    protected final static int WINAMP_VOLUMEUP      = 40058;
    protected final static int WINAMP_VOLUMEDOWN    = 40059;
    protected final static int WINAMP_FFWD5S        = 40060;
    protected final static int WINAMP_REW5S         = 40061;
    protected final static int WINAMP_BUTTON4_SHIFT = 40147;
    protected final static int WINAMP_VISPLUGIN     = 40192;

    protected static final int VOLUME_SLEEP = 50;
    protected static final int SEEK_SLEEP = 100;

    protected VolumeWorker volumeWorker;
    protected SeekWorker seekWorker;
    protected double volume;
    protected boolean muted;

    public WinampApplication() {
        super(PROGRAM, TITLE, WINDOW);
        volume = getVolume();
        muted = volume == 0;
        volumeWorker = new VolumeWorker();
        seekWorker = new SeekWorker();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        volumeWorker.stop();
        seekWorker.stop();
    }

    public void exit() {
        super.exit();
        volumeWorker.exit();
        seekWorker.exit();
    }

    public void begin(Action action) {
        logger.trace("WinampApplication begin: " + action);
        switch (action) {
            case VOLUME_UP:
                volumeWorker.start(1);
                break;
            case VOLUME_DOWN:
                volumeWorker.start(-1);
                break;
            case FORWARD:
                seekWorker.start(1);
                break;
            case REWIND:
                seekWorker.start(-1);
                break;
        }
    }

    public void end(Action action) {
        logger.trace("WinampApplication end: " + action);
        switch (action) {
            case PLAY:
                logger.debug("play");
                switch (user(0, IPC_ISPLAYING)) {
                    case STATUS_STOPPED:
                        command(WINAMP_BUTTON2);
                        break;
                    default:
                        command(WINAMP_BUTTON3);
                        break;
                }                
                break;
            case NEXT:
                command(WINAMP_BUTTON5);
                break;
            case PREVIOUS:
                command(WINAMP_BUTTON1);
                break;
            case FORWARD:
            case REWIND:
                seekWorker.stop();
                break;
            case MUTE:
                if (muted) {
                    setVolume(volume);
                } else {
                    volume = getVolume();
                    setVolume(0);
                }
                muted = !muted;
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWorker.stop();
                break;
            case SHUFFLE:
                command(WINAMP_FILE_SHUFFLE);
                break;
            case REPEAT:
                command(WINAMP_FILE_REPEAT);
                break;
            case FADEOUT:
                command(WINAMP_BUTTON4_SHIFT);
                break;
            case QUIT:
                command(WINAMP_FILE_QUIT);
                break;
            case VISUALISER:
                system(Command.System.MAXIMIZE);
                command(WINAMP_VISPLUGIN);
                break;
        }
    }

    public double getVolume() {
        return user(-666, IPC_SETVOLUME) / 255f;
    }

    public void setVolume(double volume) {
        user((int) Math.ceil(volume * 255), IPC_SETVOLUME);
    }

    public int getDuration() {
        return user(1, IPC_GETOUTPUTTIME);
    }

    public int getElapsed() {
        return user(0, IPC_GETOUTPUTTIME) / 1000;
    }

    protected class VolumeWorker extends Worker {
        protected int volumeChangeSign;

        public void start(int volumeChangeSign) {
            super.start();
            this.volumeChangeSign = volumeChangeSign;
        }

        public void work() {
            command(volumeChangeSign > 0 ? WINAMP_VOLUMEUP : WINAMP_VOLUMEDOWN);
            sleep(VOLUME_SLEEP);
        }
    };

    protected class SeekWorker extends Worker {
        protected int seekDirection;

        public void start(int seekDirection) {
            super.start();
            this.seekDirection = seekDirection;
        }

        public void work() {
            command(seekDirection > 0 ? WINAMP_FFWD5S : WINAMP_REW5S);
            sleep(SEEK_SLEEP);
        }
    };
}
