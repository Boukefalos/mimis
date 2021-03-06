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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public abstract class Worker {
    public enum Type {
        DIRECT, FOREGROUND, BACKGROUND, POOLED
    }

    public static final int SLEEP = 100;

    protected Logger logger;

    protected boolean run = false;
    protected boolean active = false;
    protected boolean activate = false;
    protected boolean deactivate = false;

    protected Work work;

    public Worker(Work work) {
        this.work = work;
        logger = LoggerFactory.getLogger(work.getClass());
    }

    public boolean active() {
        logger.trace("Worker: active()");
        return deactivate || active;
    }

    public final void run() {
        logger.debug("Worker: run()");
        while (run || deactivate) {
            runActivate();
            runDeactivate();
            runWork();
        }
    }

    public void runActivate() {        
        if (activate && !active) {
            logger.trace("Worker: runActivate()");
            try {
                work.activate();
                active = true;
            } catch (ActivateException e) {
                logger.error("", e);
            } finally {
                activate = false;
            }
        }
    }

    public void runDeactivate() {
        if (deactivate && active) {
            logger.trace("Worker: runDeactivate()");
            try {
               work.deactivate();
            } catch (DeactivateException e) {
                logger.error("", e);
            } finally {
                deactivate = false;
                active = false;
            }
        }
    }

    public void runWork() {
        if (active) {
            logger.trace("Worker: runWork() > work");
            work.work();
        } else if (run) {
            try {
                logger.trace("Worker: runWork() > wait");
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                logger.info("", e);
            }
        }
    }

    public void sleep() {
        sleep(SLEEP);
    }

    public void sleep(int time) {
        try {
            if (time > 0) {
                Thread.sleep(time);
            }
        } catch (InterruptedException e) {
            logger.info("", e);
        }
    }

    public abstract void start();

    public void stop() {
        logger.trace("Worker: stop()");
        if (active && !activate) {
            deactivate = true;
        }
        activate = false;
    }

    abstract public void exit();
}
