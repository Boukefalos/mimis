package base.worker;

import base.work.Listen;
import base.worker.pool.Listener;

public class ThreadListener<E> extends ThreadWorker implements Listener<E> {
	protected Listen<E> listen;

	public ThreadListener(Listen<E> listen) {
		super(listen);
		this.listen = listen;
	}

	public ThreadListener(Listen<E> listen, boolean start) {
		super(listen);
	}

	public void add(E element) {
		listen.queue.add(element);
		listen.notify();
	}
}
