package mimis.application.cmd.windows.wmp;

import base.worker.Worker;
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

    protected class VolumeWorker extends Worker {
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
