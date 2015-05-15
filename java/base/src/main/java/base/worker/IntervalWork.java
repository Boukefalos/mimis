package base.worker;

import base.work.Work;

public abstract class IntervalWork extends Work {
	protected IntervalWork() {
		this(WORKER_TYPE);
	}

	protected IntervalWork(int interval) {
		this(WORKER_TYPE, interval);
	}

	protected IntervalWork(Worker.Type workerType) {
		switch (workerType) {
			case DIRECT:
				worker = new DirectIntervalWorker(this);
				break;
			default:
			case THREAD:
				worker = new ThreadIntervalWorker(this);
				break;
		}
	}

	protected IntervalWork(Worker.Type workerType, int interval) {
		switch (workerType) {
			case DIRECT:
				worker = new DirectIntervalWorker(this, interval);
				break;
			default:
			case THREAD:
				worker = new ThreadIntervalWorker(this, interval);
				break;
		}
	}
}
