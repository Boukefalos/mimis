package base.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.DirectWorker;
import base.worker.ThreadWorker;
import base.worker.Worker;
import base.worker.pool.PooledWorker;
import base.worker.pool.WorkerPool;

public abstract class Work {
	protected static final Worker.Type WORKER_TYPE = Worker.Type.THREAD;
	
    protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Worker worker;

	protected Work() {
		this(WORKER_TYPE);
	}

	protected Work(Worker.Type workerType) {
		switch (workerType) {
			case DIRECT:
				worker = new DirectWorker(this);
				break;
			default:
			case THREAD:
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
		worker.start();
	}

	public void stop() {
		logger.debug("Stop work");
		worker.stop();
	}

	public boolean active() {
		return worker.active();
	}

	public void exit() {
		worker.exit();		
	}

	public void activate() throws ActivateException {}
	public void deactivate() throws DeactivateException {}
	public abstract void work();
}
