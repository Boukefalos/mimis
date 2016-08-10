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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import base.exception.worker.ActivateException;
import base.worker.BackgroundListener;
import base.worker.ForegroundListener;
import base.worker.Worker;
import base.worker.pool.Listener;
import base.worker.pool.ListenerPool;
import base.worker.pool.PooledListener;

public abstract class Listen<E> extends Work implements Listener<E> {
    protected static final Worker.Type WORKER_TYPE = Worker.Type.DIRECT;

    protected Listener<E> listener;
    protected Worker.Type workerType;
    public Queue<E> queue;

    public Listen() {
        this(WORKER_TYPE);
    }

    protected Listen(Worker.Type workerType) {
        queue = new ConcurrentLinkedQueue<E>();
        this.workerType = workerType;
        switch (workerType) {
            case DIRECT:
                return;
            case FOREGROUND:
                listener = new ForegroundListener<E>(this);
                break;
            default:
                listener = new BackgroundListener<E>(this);
                break;
        }
    }

    protected Listen(Worker worker) {
        this.worker = worker;
        queue = new ConcurrentLinkedQueue<E>();
    }

    protected Listen(ListenerPool<E> listenerPool) {
        listener = new PooledListener<E>(this);
        listenerPool.add((PooledListener<E>) listener);
        queue = new ConcurrentLinkedQueue<E>();
    }

    public synchronized void add(E element) {
        if (workerType.equals(Worker.Type.DIRECT)) {
            input(element);
        } else {
            queue.add(element);
            listener.add(element);
        }
    }

    public void start() {
        if (workerType.equals(Worker.Type.DIRECT)) {
            try {
                activate();
            } catch (ActivateException e) {
                logger.error("Failed to start directly", e);
            }
        } else {
            super.start();
        }
    }

    public void stop() {
        super.stop();
        synchronized (this) {
            notifyAll();
        }
    }

    public void work() {
        while (!queue.isEmpty()) {
            logger.debug("Listen: work() > input");
            input(queue.poll());
        }
        synchronized (this) {
            logger.debug("Listen: work() > wait");
            try {                
                wait();                
            } catch (InterruptedException e) {}
            logger.debug("Listen: work() > notified");
        }
    }

    public abstract void input(E element);
}
