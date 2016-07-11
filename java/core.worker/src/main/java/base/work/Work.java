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
package base.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.Control;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.DirectWorker;
import base.worker.ThreadWorker;
import base.worker.Worker;
import base.worker.pool.PooledWorker;
import base.worker.pool.WorkerPool;

public abstract class Work implements Control {
    protected static final Worker.Type WORKER_TYPE = Worker.Type.BACKGROUND;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Worker worker;

    protected Work() {
        this(WORKER_TYPE);
    }

    protected Work(Worker.Type workerType) {
        switch (workerType) {
            case FOREGROUND:
                worker = new DirectWorker(this);
                break;
            default:
                worker = new ThreadWorker(this);
                break;
        }
    }

    protected Work(Worker worker) {
        this.worker = worker;
    }

    protected Work(WorkerPool workerPool) {
        worker = new PooledWorker(this);
        workerPool.add((PooledWorker) worker);        
    }

    protected void sleep(int time) {
        worker.sleep(time);
    }

    public void start() {
        logger.trace("Work: start()");
        worker.start();
    }

    public void stop() {
        logger.trace("Work: stop()");
        worker.stop();
    }

    public boolean active() {
        logger.trace("Work: active()");
        return worker.active();
    }

    public void exit() {
        logger.debug("Work: exit()");
        worker.exit();
    }

    public void activate() throws ActivateException {}
    public void deactivate() throws DeactivateException {}
    public abstract void work();
}
