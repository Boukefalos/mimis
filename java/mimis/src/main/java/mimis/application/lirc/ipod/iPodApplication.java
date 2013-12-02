package mimis.application.lirc.ipod;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Worker;
import mimis.application.lirc.LircApplication;
import mimis.device.lirc.remote.WC02IPOButton;
import mimis.value.Action;

public class iPodApplication extends LircApplication {
    protected static final String TITLE = "iPod";
    protected static final int VOLUME_SLEEP = 100;

    protected VolumeWorker volumeWorker;

    public iPodApplication() {
        super(TITLE);
        volumeWorker = new VolumeWorker();
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        volumeWorker.stop();
    }

    public void exit() {
        super.exit();
        volumeWorker.exit();
    }

    protected void begin(Action action) {
        logger.trace("iPodApplication begin: " + action);
        if (!active) return;
        switch (action) {
            case VOLUME_UP:
                try {
                    volumeWorker.activate(1);
                } catch (ActivateException e) {
                    logger.error("", e);
                }
                break;
            case VOLUME_DOWN:
                try {
                    volumeWorker.activate(-1);
                } catch (ActivateException e) {
                    logger.error("", e);
                }
                break;
        }
    }

    protected void end(Action action) {
        logger.trace("iPodApplication end: " + action);
        if (!active) return;
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
                volumeWorker.stop();
                break;
        }
    }

    protected class VolumeWorker extends Worker {
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