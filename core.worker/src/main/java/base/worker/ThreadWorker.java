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

import base.work.Work;

public class ThreadWorker extends Worker implements Runnable {
    protected static final boolean THREAD = true;

    protected boolean thread = true;

    public ThreadWorker(Work work, boolean thread) {
        this(work);
        this.thread = thread;
    }

    public ThreadWorker(Work work) {
        super(work);
    }

    public synchronized void start(boolean thread) {
        if (!active) {
            activate = true;
        }
        if (!run) {
            run = true;
            if (thread) {
                logger.debug("Start thread");
                new Thread(this, work.getClass().getName()).start();
            } else {
                logger.debug("Run directly");
                run();
            }
        } else {
            notifyAll();
        }
    }

    public synchronized void start() {
        start(thread);
    }

    public void exit() {
        run = false;
        work.stop();
    }
}
