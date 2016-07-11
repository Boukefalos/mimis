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
package mimis.application.lirc.ipod;

import mimis.application.lirc.LircApplication;
import mimis.device.lirc.remote.WC02IPOButton;
import mimis.value.Action;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public class iPodApplication extends LircApplication {
    protected static final String TITLE = "iPod";
    protected static final int VOLUME_SLEEP = 100;

    protected VolumeWork volumeWork;

    public iPodApplication() {
        super(TITLE);
        volumeWork = new VolumeWork();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        volumeWork.stop();
    }

    public void exit() {
        super.exit();
        volumeWork.exit();
    }

    protected void begin(Action action) {
        logger.trace("iPodApplication begin: " + action);
        if (!active()) return;
        switch (action) {
            case VOLUME_UP:
                try {
                    volumeWork.activate(1);
                } catch (ActivateException e) {
                    logger.error("", e);
                }
                break;
            case VOLUME_DOWN:
                try {
                    volumeWork.activate(-1);
                } catch (ActivateException e) {
                    logger.error("", e);
                }
                break;
            default:
                break;
        }
    }

    protected void end(Action action) {
        logger.trace("iPodApplication end: " + action);
        if (!active()) return;
        switch (action) {
            case PLAY:
                send(WC02IPOButton.PLAY);
                break;
            case NEXT:
                send(WC02IPOButton.NEXT);
                break;
            case PREVIOUS:
                send(WC02IPOButton.PREVIOUS);
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWork.stop();
                break;
            default:
                break;
        }
    }

    protected class VolumeWork extends Work {
        protected int volumeChangeRate;

        public void activate(int volumeChangeRate) throws ActivateException {
            super.activate();
            this.volumeChangeRate = volumeChangeRate;
            send(volumeChangeRate > 0 ? WC02IPOButton.PLUS : WC02IPOButton.MINUS);
        }

        public void work() {
            lircService.send(WC02IPOButton.HOLD);
            sleep(VOLUME_SLEEP);
        }
    };
}