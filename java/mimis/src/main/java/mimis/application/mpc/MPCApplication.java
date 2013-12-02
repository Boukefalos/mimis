package mimis.application.mpc;

import base.worker.Worker;
import mimis.application.cmd.windows.WindowsApplication;
import mimis.value.Action;

public class MPCApplication extends WindowsApplication {
    protected final static String PROGRAM = "mpc-hc.exe";
    protected final static String TITLE = "Media Player Classic";
    protected final static String WINDOW = "MediaPlayerClassicW";
    
    protected static final int VOLUME_SLEEP = 50;
    protected static final int SEEK_SLEEP = 50;

    protected VolumeWorker volumeWorker;
    protected SeekWorker seekWorker;
    
    public MPCApplication() {
        super(PROGRAM, TITLE, WINDOW);
        volumeWorker = new VolumeWorker();
        seekWorker = new SeekWorker();
    }

    public void begin(Action action) {
        logger.trace("MPCApplication: " + action);
        switch (action) {
           case FORWARD:
                seekWorker.start(1);
                break;
            case REWIND:
                seekWorker.start(-1);
                break;
            case VOLUME_UP:
                volumeWorker.start(1);
                break;
            case VOLUME_DOWN:
                volumeWorker.start(-1);
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
                seekWorker.stop();
                break;
            case MUTE:
                command(909);
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWorker.stop();
                break;
            case FULLSCREEN:
                command(830);
                break;
        }
    }

    public String getTitle() {
        return TITLE;
    }

    protected class VolumeWorker extends Worker {
        protected int volumeChangeSign;

        public void start(int volumeChangeSign)  {
            super.start();
            this.volumeChangeSign = volumeChangeSign;
        }

        public void work() {
            command(volumeChangeSign > 0 ? 907 : 908);
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
            command(seekDirection > 0 ? 900 : 889);
            sleep(SEEK_SLEEP);
        }
    };    
}
