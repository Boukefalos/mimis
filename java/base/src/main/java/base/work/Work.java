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
		logger.debug("Work: start()");
		worker.start();
	}

	public void stop() {
		logger.debug("Work: stop()");
		worker.stop();
	}

	public boolean active() {
		logger.debug("Work: active()");
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
