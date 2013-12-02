package mimis.application.cmd.windows.gomplayer;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Worker;
import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;
import mimis.value.Amount;

public class GomPlayerApplication extends WindowsApplication {
    protected final static String PROGRAM = "GOM.exe";
    protected final static String TITLE = "GOM Player";
    protected final static String WINDOW = "GomPlayer1.x";

    protected static final int VOLUME_SLEEP = 100;
    protected static final int SEEK_SLEEP = 100;

    protected VolumeWorker volumeWorker;
    protected SeekWorker seekWorker;

    public GomPlayerApplication() {
        super(PROGRAM, TITLE, WINDOW);
        volumeWorker = new VolumeWorker();
        seekWorker = new SeekWorker();
    }

    protected void deactivate() throws DeactivateException {
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
        logger.trace("GomPlayerApplication begin: " + action);
        switch (action) {
            case VOLUME_UP:
                volumeWorker.start();    
                break;
            case VOLUME_DOWN:
                volumeWorker.start();
                break;
            case FORWARD:
                seekWorker.start(Amount.SMALL, 1);
                break;
            case REWIND:
                seekWorker.start(Amount.SMALL, -1);
                break;
            case NEXT:
                seekWorker.start(Amount.MEDIUM, 1);
                break;
            case PREVIOUS:
                seekWorker.start(Amount.MEDIUM, -1);
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
                seekWorker.stop();
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWorker.stop();
                break;
            case FULLSCREEN:
                command(0x8154);
                break;
        }
    }

    protected class VolumeWorker extends Worker {
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

    protected class SeekWorker extends Worker {
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
