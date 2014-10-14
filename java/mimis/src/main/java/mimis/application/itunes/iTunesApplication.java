package mimis.application.itunes;

import mimis.Component;
import mimis.application.Application;
import mimis.value.Action;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Worker;

import com.dt.iTunesController.ITCOMDisabledReason;
import com.dt.iTunesController.ITTrack;
import com.dt.iTunesController.iTunes;
import com.dt.iTunesController.iTunesEventsInterface;

public class iTunesApplication extends Component implements Application, iTunesEventsInterface {
    protected static final String TITLE = "iTunes";
    protected static final boolean EVENTS = false;

    protected static final int VOLUME_CHANGE_RATE = 5;
    protected static final int VOLUME_SLEEP = 100;
    protected static final String PLAYLIST_LIKE = "Like";
    protected static final String PLAYLIST_DISLIKE = "Dislike";

    protected iTunes iTunes;
    protected VolumeWorker volumeWorker;
    protected boolean events;

    public iTunesApplication() {
        this(EVENTS);
    }

    public iTunesApplication(boolean events) {
        super(TITLE);
        this.events = events;
        volumeWorker = new VolumeWorker();
    }

    protected synchronized void activate() throws ActivateException {
        iTunes = new iTunes();
        iTunes.connect();
        if (events) {
            iTunes.addEventHandler(this);
        }
        super.activate();
    }

    public synchronized boolean active() {
        try {
            iTunes.getMute();
            active = true;
        } catch (Exception e) {
            active = false;
        }
        return active;
    }

    protected synchronized void deactivate() throws DeactivateException  {
        if (events) {
            exit();
        } else {
            super.deactivate();
            volumeWorker.stop();
            try {
                iTunes.release();
            } catch (Exception e) {
                logger.error("", e);
                throw new DeactivateException();
            }
        }
    }

    public synchronized void exit() {
        try {
            iTunes.quit();
        } catch (Exception e) {}
        volumeWorker.exit();
        super.exit();
    }

    protected void begin(Action action) {
        logger.trace("iTunesApplication begin: " + action);
        if (!active) return;
        switch (action) {
            case FORWARD:
                iTunes.fastForward();
                break;
            case REWIND:
                iTunes.rewind();
                break;
            case VOLUME_UP:
                volumeWorker.start(VOLUME_CHANGE_RATE);
                break;
            case VOLUME_DOWN:
                volumeWorker.start(-VOLUME_CHANGE_RATE);
                break;
        }
    }

    protected void end(Action action) {
        logger.trace("iTunesApplication end: " + action);
        if (!active) return;
        switch (action) {
            case PLAY:
                iTunes.playPause();
                break;
            case NEXT:
                iTunes.nextTrack();
                break;
            case PREVIOUS:
                iTunes.previousTrack();
                break;
            case FORWARD:
                iTunes.resume();
                break;
            case REWIND:
                iTunes.resume();
                break;
            case MUTE:
                iTunes.toggleMute();
                break;
            case VOLUME_UP:
            case VOLUME_DOWN:
                volumeWorker.stop();
                break;
            case SHUFFLE:
                iTunes.toggleShuffle();
                break;
            case REPEAT:
                iTunes.cycleSongRepeat();
                break;
            case LIKE:
                iTunes.playlistAddCurrentTrack(PLAYLIST_LIKE);
                break;
            case DISLIKE:
                iTunes.playlistAddCurrentTrack(PLAYLIST_DISLIKE);
                break;
        }
    }

    protected int getVolume() {
        return iTunes.getSoundVolume();
    }

    public void onDatabaseChangedEvent(int[][] deletedObjectIDs, int[][] changedObjectIDs) {}
    public void onPlayerPlayEvent(ITTrack iTrack) {
        if (active) {
            logger.trace("iTunesEvent: play");
        }
    }

    public void onPlayerStopEvent(ITTrack iTrack) {
        if (active) {
            logger.trace("iTunesEvent: stop");
        }
    }

    public void onPlayerPlayingTrackChangedEvent(ITTrack iTrack) {}
    public void onCOMCallsDisabledEvent(ITCOMDisabledReason reason) {}
    public void onCOMCallsEnabledEvent() {}
    public void onQuittingEvent() {}
    public void onAboutToPromptUserToQuitEvent() {}
    public void onSoundVolumeChangedEvent(int newVolume) {}    

    protected class VolumeWorker extends Worker {
        protected int volumeChangeRate;

        public void start(int volumeChangeRate) {
            super.start();
            this.volumeChangeRate = volumeChangeRate;
        }

        public void work() {
            iTunes.setSoundVolume(getVolume() + volumeChangeRate);
            sleep(VOLUME_SLEEP);
        }
    };
}