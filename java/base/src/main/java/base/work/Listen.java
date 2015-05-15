package base.work;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import base.worker.DirectListener;
import base.worker.ThreadListener;
import base.worker.Worker;
import base.worker.pool.Listener;
import base.worker.pool.ListenerPool;
import base.worker.pool.PooledListener;

public abstract class Listen<E> extends Work {
    protected Listener<E> listener;
    public Queue<E> queue;

    public Listen() {
    	this(WORKER_TYPE);
    }

	protected Listen(Worker.Type workerType) {
		switch (workerType) {
			case DIRECT:
				listener = new DirectListener<E>(this);
				break;
			default:
			case THREAD:
				listener = new ThreadListener<E>(this);
				break;
		}
        queue = new ConcurrentLinkedQueue<E>();
	}

	protected Listen(Worker worker) {
		this.worker = worker;
        queue = new ConcurrentLinkedQueue<E>();
	}

	protected Listen(ListenerPool<E> listenerPool) {
		listener = new PooledListener<E>(this);
		listenerPool.add((PooledListener<E>) listener);
        queue = new ConcurrentLinkedQueue<E>();
	}

    public synchronized void add(E element) {
    	listener.add(element);
    }

	public void work() {
        while (!queue.isEmpty()) {
            input(queue.poll());
        }
        synchronized (this) {
            try {
                wait();                
            } catch (InterruptedException e) {}
        }		
	}

	public void input(E element) {}
}
