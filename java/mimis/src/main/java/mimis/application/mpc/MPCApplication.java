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
package mimis.application.mpc;

import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;
import base.work.Work;

public class MPCApplication extends WindowsApplication {
    protected final static String PROGRAM = "mpc-hc.exe";
    protected final static String TITLE = "Media Player Classic";
    protected final static String WINDOW = "MediaPlayerClassicW";
    
    protected static final int VOLUME_SLEEP = 50;
    protected static final int SEEK_SLEEP = 50;

    protected VolumeWork volumeWork;
    protected SeekWork seekWork;

    public MPCApplication() {
        super(PROGRAM, TITLE, WINDOW);
        volumeWork = new VolumeWork();
        seekWork = new SeekWork();
    }

    public void begin(Action action) {
        logger.trace("MPCApplication: " + action);
        switch (action) {
           case FORWARD:
                seekWork.start(1);
                break;
            case REWIND:
                seekWork.start(-1);
                break;
            case VOLUME_UP:
                volumeWork.start(1);
                break;
            case VOLUME_DOWN:
                volumeWork.start(-1);
                break;
            default:
                break;
        }
    }
    
    public void end(Action action) {
        logger.trace("MPCApplication: " + action);
        switch (action) {
            case PLAY:
                command(889);
                break;
            case NEXT:
                command(921);
                break;
            case PREVIOUS:
                command(920);
                break;
            case FORWARD:
            case REWIND:
                seekWork.stop();
                break;
            case MUTE:
                command(909);
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWork.stop();
                break;
            case FULLSCREEN:
                command(830);
                break;
            default:
                break;
        }
    }

    public String getTitle() {
        return TITLE;
    }

    protected class VolumeWork extends Work {
        protected int volumeChangeSign;

        public void start(int volumeChangeSign)  {
            super.start();
            this.volumeChangeSign = volumeChangeSign;
        }

        public void work() {
            command(volumeChangeSign > 0 ? 907 : 908);
            sleep(VOLUME_SLEEP);
        }
    }

    protected class SeekWork extends Work {
        protected int seekDirection;

        public void start(int seekDirection) {
            super.start();
            this.seekDirection = seekDirection;
        }

        public void work() {
            command(seekDirection > 0 ? 900 : 889);
            sleep(SEEK_SLEEP);
        }
    };    
}
