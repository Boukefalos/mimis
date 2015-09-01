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
