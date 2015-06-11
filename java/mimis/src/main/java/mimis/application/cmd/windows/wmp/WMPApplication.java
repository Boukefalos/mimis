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
package mimis.application.cmd.windows.wmp;

import base.worker.ThreadWorker;
import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;

public class WMPApplication extends WindowsApplication {
    protected final static String PROGRAM = "wmplayer.exe";
    protected final static String TITLE = "Windows Media Player";
    protected final static String WINDOW = "WMPlayerApp";

    protected static final int VOLUME_SLEEP = 120;

    protected VolumeWorker volumeWorker;
    
    public WMPApplication() {
        super(PROGRAM, TITLE, WINDOW);
        volumeWorker = new VolumeWorker();
    }

    public void begin(Action action) {
        logger.trace("WMPApplication begin: " + action);
        switch (action) {
            case PLAY:
                command(18808);
                break;
            case NEXT:
                command(18811);
                break;
            case PREVIOUS:
                command(18810);
                break;
            case FORWARD:
                command(18813);
                break;
            case REWIND:
                command(18812);
                break;
            case MUTE:
                command(18817);
                break;
            case VOLUME_UP:
                volumeWorker.start(1);
                break;
            case VOLUME_DOWN:
                volumeWorker.start(-1);
                break;
            case SHUFFLE:
                command(18842);
                break;
            case REPEAT:
                command(18843);
                break;
        }
    }

    public void end(Action action) {
        logger.trace("WMPApplication end: " + action);
        switch (action) {
            case FORWARD:
                command(18813);
                break;
            case REWIND:
                command(18812);
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWorker.stop();
                break;
        }
    }

    protected class VolumeWorker extends ThreadWorker {
        protected int volumeChangeSign;

        public void start(int volumeChangeSign) {
            super.start();
            this.volumeChangeSign = volumeChangeSign;
        }

        public void work() {
            command (volumeChangeSign > 0 ? 18815 : 18816);
            sleep(VOLUME_SLEEP);
        }
    };
}
