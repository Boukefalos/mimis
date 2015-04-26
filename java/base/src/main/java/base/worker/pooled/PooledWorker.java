package base.worker.pooled;

import java.util.concurrent.BlockingQueue;

import base.worker.Worker;

public abstract class PooledWorker extends Worker {
	protected BlockingQueue<Worker> activateQueue;
	protected BlockingQueue<Worker> deactivateQueue;

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
}
