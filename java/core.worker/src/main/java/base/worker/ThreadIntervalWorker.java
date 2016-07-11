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
package base.worker;

import java.util.Timer;
import java.util.TimerTask;

import base.work.Work;

public class ThreadIntervalWorker extends ThreadWorker {
    protected static final int INTERVAL = 500;
    protected int interval;

    public ThreadIntervalWorker(Work work) {
        super(work);
        interval = INTERVAL;
    }

    public ThreadIntervalWorker(Work work, boolean thread) {
        super(work, thread);
        interval = INTERVAL;
    }

    public ThreadIntervalWorker(Work work, int interval) {
        super(work);
        this.interval = interval;
    }

    protected Timer timer;

    public synchronized void start(boolean thread) {
        if (!active) {
            activate = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    Worker worker = ThreadIntervalWorker.this;                    
                    worker.runActivate();
                    worker.runDeactivate();
                    worker.runWork();
                }}, 0, interval);
            active = true;
        }
        if (!thread) {
             try {
                 synchronized (this) {
                     wait();
                 }
             } catch (InterruptedException e) {
                 logger.info("", e);
             }
        }
    }

    public synchronized void stop() {
        if (active) {
            timer.cancel();
            deactivate = true;
            run();
            notifyAll();
        }
    }
}