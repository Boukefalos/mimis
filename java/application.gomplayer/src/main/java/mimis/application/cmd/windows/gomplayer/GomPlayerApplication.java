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
package mimis.application.cmd.windows.gomplayer;

import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;
import mimis.value.Amount;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public class GomPlayerApplication extends WindowsApplication {
    protected final static String PROGRAM = "GOM.exe";
    protected final static String TITLE = "GOM Player";
    protected final static String WINDOW = "GomPlayer1.x";

    protected static final int VOLUME_SLEEP = 100;
    protected static final int SEEK_SLEEP = 100;

    protected VolumeWork volumeWork;
    protected SeekWork seekWork;

    public GomPlayerApplication() {
        super(PROGRAM, TITLE, WINDOW);
        volumeWork = new VolumeWork();
        seekWork = new SeekWork();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        volumeWork.stop();
        seekWork.stop();
    }

    public void exit() {
        super.exit();
        volumeWork.exit();
        seekWork.exit();
    }

    public void begin(Action action) {
        logger.trace("GomPlayerApplication begin: " + action);
        switch (action) {
            case VOLUME_UP:
                volumeWork.start();    
                break;
            case VOLUME_DOWN:
                volumeWork.start();
                break;
            case FORWARD:
                seekWork.start(Amount.SMALL, 1);
                break;
            case REWIND:
                seekWork.start(Amount.SMALL, -1);
                break;
            case NEXT:
                seekWork.start(Amount.MEDIUM, 1);
                break;
            case PREVIOUS:
                seekWork.start(Amount.MEDIUM, -1);
                break;
            default:
                break;
        }
    }

    public void end(Action action) {
        logger.trace("GomPlayerApplication end: " + action);
        switch (action) {
            case PLAY:
                command(0x800C);
                break;
            case MUTE:
                command(0x8016);
                break;
            case FORWARD:
            case REWIND:
            case NEXT:
            case PREVIOUS:
                seekWork.stop();
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWork.stop();
                break;
            case FULLSCREEN:
                command(0x8154);
                break;
            default:
                break;
        }
    }

    protected class VolumeWork extends Work {
        protected int volumeChangeSign;

        public void start(int volumeChangeSign) throws ActivateException {
            super.start();
            this.volumeChangeSign = volumeChangeSign;
        }

        public void work() {
            command(volumeChangeSign > 0 ? 0x8014 : 0x8013);
            sleep(VOLUME_SLEEP);
        }
    };

    protected class SeekWork extends Work {
        protected Amount amount;
        protected int seekDirection;

        public void start(Amount amount, int seekDirection) {
            super.start();
            this.amount = amount;
            this.seekDirection = seekDirection;
        }

        public void work() {
            switch (amount) {
                case SMALL:
                    command(seekDirection > 0 ? 0x8009 : 0x8008);
                    break;
                case MEDIUM:
                    command(seekDirection > 0 ? 0x800B : 0x800A);
                    break;
                case LARGE:
                    command(seekDirection > 0 ? 0x8012 : 0x8011);
                    break;
            }
            sleep(SEEK_SLEEP);
        }
    };
}
