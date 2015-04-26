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
