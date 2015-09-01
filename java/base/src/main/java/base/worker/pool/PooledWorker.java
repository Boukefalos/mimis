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
