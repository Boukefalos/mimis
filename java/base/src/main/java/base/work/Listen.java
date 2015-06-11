package base.work;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

	public void input(Object object) {
		MethodType methodType = MethodType.methodType(void.class, object.getClass());
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle methodHandle;
		try {
			methodHandle = lookup.findVirtual(getClass(), "input", methodType);
			methodHandle.invoke(this, object);
		} catch (Throwable e) {
			logger.error("", e);
		}
	}
}
