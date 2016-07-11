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
package base.worker.pool;

import java.util.concurrent.BlockingQueue;

import base.work.Work;
import base.worker.Worker;

public class PooledWorker extends Worker {
    protected BlockingQueue<Worker> activateQueue;
    protected BlockingQueue<Worker> deactivateQueue;

    public PooledWorker(Work work) {
        super(work);
    }

    public void setActivateQueue(BlockingQueue<Worker> activateQueue) {
        this.activateQueue = activateQueue;
    }

    public void setDeactivateQueue(BlockingQueue<Worker> deactivateQueue) {
        this.deactivateQueue = deactivateQueue;
    }

    public void start() {
        if (!active) {
            activate = true;
        }
        if (!run) {
            run = true;
        }
        try {
            deactivateQueue.remove(this);
            activateQueue.put(this);
        } catch (InterruptedException e) {}
    }

    public void stop() {
        System.out.println("stop!! " + active);
        if (active) {
            deactivate = true;
        }
        activateQueue.remove(this);
        deactivateQueue.add(this);
    }

    public void exit() {
        stop();        
    }
}
