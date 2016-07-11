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

import base.util.ArrayCycle;
import base.worker.Worker;

public class WorkerPoolRunnable implements Runnable {
    protected BlockingQueue<Worker> activateQueue;
    protected BlockingQueue<Worker> deactivateQueue;
    protected ArrayCycle<Worker> workerCycle;
    protected int id;

    public WorkerPoolRunnable(BlockingQueue<Worker> activateQueue, BlockingQueue<Worker> deactivateQueue, ArrayCycle<Worker> workerCycle, int id) {
        this.activateQueue = activateQueue;
        this.deactivateQueue = deactivateQueue;
        this.workerCycle = workerCycle;
        this.id = id;
    }

    public void run() {
        while (true) {
            if (!deactivateQueue.isEmpty()) {
                try {
                    Worker worker = deactivateQueue.take();
                    worker.runDeactivate();
                    workerCycle.remove(worker);
                } catch (InterruptedException e) {}
            } else if (!activateQueue.isEmpty() || workerCycle.isEmpty()) {
                try {
                    Worker worker = activateQueue.take();
                    worker.runActivate();
                    workerCycle.add(worker);
                } catch (InterruptedException e) {}            
            } else {
                Worker worker = workerCycle.next();
                worker.runWork();
            }
        }
    }
}
