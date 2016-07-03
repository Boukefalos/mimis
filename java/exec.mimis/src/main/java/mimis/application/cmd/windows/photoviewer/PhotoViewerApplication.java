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
package mimis.application.cmd.windows.photoviewer;

import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;
import mimis.value.Key;
import mimis.value.Type;
import base.exception.worker.DeactivateException;
import base.work.Work;

public class PhotoViewerApplication extends WindowsApplication {
    protected final static String TITLE = "Photo Viewer";
    protected final static String WINDOW = "Photo_Lightweight_Viewer";

    protected static final int ZOOM_SLEEP = 100;
    protected static final int DELETE_SLEEP = 2000;

    protected ZoomWork zoomWork;
    protected boolean fullscreen;

    public PhotoViewerApplication() {
        super(TITLE, WINDOW);
        zoomWork = new ZoomWork();
        fullscreen = false;
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        zoomWork.stop();
    }

    public void exit() {
        super.exit();
        zoomWork.exit();
    }

    public void begin(Action action) {
        switch (action) {
            case VOLUME_UP:
                zoomWork.start(1);    
                break;
            case VOLUME_DOWN:
                zoomWork.start(-1);
                break;
            default:
                break;
        }
    }

    public void end(Action action) {
        logger.trace("PhotoViewerApplication end: " + action);
        switch (action) {
            case VOLUME_UP:
            case VOLUME_DOWN:
                zoomWork.stop();
                break;
            case NEXT:
                key(Type.DOWN, Key.RIGHT);
                break;
            case PREVIOUS:
                key(Type.DOWN, Key.LEFT);
                break;
            case FORWARD:
                key(Type.DOWN, Key.CONTROL);
                //key(Type.DOWN, '.');
                //key(Type.DOWN, Key.DECIMAL);
                key(Type.DOWN, Key.OEM_PERIOD);
                //key(Type.UP, Key.OEM_PERIOD);
                //key(Type.UP, Key.CONTROL);
                break;
            case MUTE:
                key(Type.DOWN, Key.CONTROL);
                key(Type.DOWN, Key.NUMPAD0);
                //press(Key.CONTROL);
                //press(Key.NUMPAD0);
                //release(Key.CONTROL);
                break;
            case FULLSCREEN:
                key(Type.DOWN, fullscreen ? Key.ESCAPE : Key.F11);
                fullscreen = !fullscreen;
                break;
            case DISLIKE:
                /*boolean restore = false;
                if (fullscreen) {
                    end(Action.FULLSCREEN);
                    sleep(DELETE_SLEEP);
                    restore = true;
                }
                key(Type.DOWN, Key.F16);
                key(Type.DOWN, 'Y');
                if (restore) {
                    sleep(DELETE_SLEEP);
                    end(Action.FULLSCREEN);
                }*/
                break;
            default:
                break;
        }
    }

    protected class ZoomWork extends Work {
        protected int zoomDirection;

        public void start(int zoomDirection) {
            super.start();
            this.zoomDirection = zoomDirection;
        }

        public void work() {
            Key key = zoomDirection > 0 ? Key.ADD : Key.SUBTRACT;
            key(Type.DOWN, key);
            sleep(ZOOM_SLEEP);
        }
    }
}
