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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import base.util.ArrayCycle;
import base.worker.Worker;

public class WorkerPool {
    protected int poolSize;
    protected BlockingQueue<Worker> activateQueue;
    protected BlockingQueue<Worker> deactivateQueue;
    protected ArrayCycle<Worker> workerCycle;
    protected ExecutorService executorService;

    public WorkerPool(int poolSize) {
        this.poolSize = poolSize;
        activateQueue = new LinkedBlockingQueue<Worker>();
        deactivateQueue = new LinkedBlockingQueue<Worker>();
        workerCycle = new ArrayCycle<Worker>();
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
        for (int i = 0; i < poolSize; ++i) {
            Runnable runnable = new WorkerPoolRunnable(activateQueue, deactivateQueue, workerCycle, i + 1);
            executorService.execute(runnable);
        }        
    }

    public void stop() {
        // Must be graceful
        executorService.shutdownNow();
    }

    public void add(PooledWorker worker) {
        worker.setActivateQueue(activateQueue);
        worker.setDeactivateQueue(deactivateQueue);
    }
}
