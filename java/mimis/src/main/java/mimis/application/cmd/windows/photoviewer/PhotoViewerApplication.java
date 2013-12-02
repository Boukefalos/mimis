package mimis.application.cmd.windows.photoviewer;

import base.exception.worker.DeactivateException;
import base.worker.Worker;
import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;
import mimis.value.Key;
import mimis.value.Type;

public class PhotoViewerApplication extends WindowsApplication {
    protected final static String TITLE = "Photo Viewer";
    protected final static String WINDOW = "Photo_Lightweight_Viewer";

    protected static final int ZOOM_SLEEP = 100;
    protected static final int DELETE_SLEEP = 2000;

    protected ZoomWorker zoomWorker;
    protected boolean fullscreen;

    public PhotoViewerApplication() {
        super(TITLE, WINDOW);
        zoomWorker = new ZoomWorker();
        fullscreen = false;
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        zoomWorker.stop();
    }

    public void exit() {
        super.exit();
        zoomWorker.exit();
    }

    public void begin(Action action) {
        switch (action) {
            case VOLUME_UP:
                zoomWorker.start(1);    
                break;
            case VOLUME_DOWN:
                zoomWorker.start(-1);
                break;
        }
    }

    public void end(Action action) {
        logger.trace("PhotoViewerApplication end: " + action);
        switch (action) {
            case VOLUME_UP:
            case VOLUME_DOWN:
                zoomWorker.stop();
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
        }
    }

    protected class ZoomWorker extends Worker {
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
