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
package mimis.application.itunes;

import mimis.Component;
import mimis.application.Application;
import mimis.value.Action;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

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
    protected VolumeWork volumeWork;
    protected boolean events;

    public iTunesApplication() {
        this(EVENTS);
    }

    public iTunesApplication(boolean events) {
        super(TITLE);
        this.events = events;
        volumeWork = new VolumeWork();
    }

    public synchronized void activate() throws ActivateException {
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
        } catch (Exception e) {
            stop();
        }
        return super.active();
    }

    public synchronized void deactivate() throws DeactivateException  {
        if (events) {
            exit();
        } else {
            super.deactivate();
            volumeWork.stop();
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
        volumeWork.exit();
        super.exit();
    }

    protected void begin(Action action) {
        logger.trace("iTunesApplication begin: " + action);
        if (!active()) return;
        switch (action) {
            case FORWARD:
                iTunes.fastForward();
                break;
            case REWIND:
                iTunes.rewind();
                break;
            case VOLUME_UP:
                volumeWork.start(VOLUME_CHANGE_RATE);
                break;
            case VOLUME_DOWN:
                volumeWork.start(-VOLUME_CHANGE_RATE);
                break;
            default:
                break;
        }
    }

    protected void end(Action action) {
        logger.trace("iTunesApplication end: " + action);
        if (!active()) return;
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
                volumeWork.stop();
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
            default:
                break;
        }
    }

    protected int getVolume() {
        return iTunes.getSoundVolume();
    }

    public void onDatabaseChangedEvent(int[][] deletedObjectIDs, int[][] changedObjectIDs) {}
    public void onPlayerPlayEvent(ITTrack iTrack) {
        if (active()) {
            logger.trace("iTunesEvent: play");
        }
    }

    public void onPlayerStopEvent(ITTrack iTrack) {
        if (active()) {
            logger.trace("iTunesEvent: stop");
        }
    }

    public void onPlayerPlayingTrackChangedEvent(ITTrack iTrack) {}
    public void onCOMCallsDisabledEvent(ITCOMDisabledReason reason) {}
    public void onCOMCallsEnabledEvent() {}
    public void onQuittingEvent() {}
    public void onAboutToPromptUserToQuitEvent() {}
    public void onSoundVolumeChangedEvent(int newVolume) {}    

    protected class VolumeWork extends Work {
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