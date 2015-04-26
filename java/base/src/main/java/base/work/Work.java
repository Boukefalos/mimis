package base.work;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.DirectWorker;
import base.worker.ThreadWorker;
import base.worker.Worker;
import base.worker.pool.PooledWorker;
import base.worker.pool.WorkerPool;

public abstract class Work {
	protected static final Worker.Type WORKER_TYPE = Worker.Type.THREAD;

	protected Worker work;

	protected Work() {
		this(WORKER_TYPE);
	}

	protected Work(Worker.Type workerType) {
		switch (workerType) {
			case DIRECT:
				work = new DirectWorker(this);
				break;
			default:
			case THREAD:
				work = new ThreadWorker(this);
				break;
		}
	}

	protected Work(Worker worker) {
		this.work = worker;
	}

	protected Work(WorkerPool workerPool) {
		work = new PooledWorker(this);
		workerPool.add((PooledWorker) work);		
	}

	protected void sleep(int time) {
		work.sleep(time);
	}

	public void start() {
		work.start();
	}

	public void stop() {
		work.stop();
	}

	public void activate() throws ActivateException {}
	public void deactivate() throws DeactivateException {}
	public abstract void work();
}
