package base.work;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import base.exception.worker.ActivateException;
import base.worker.ForegroundListener;
import base.worker.BackgroundListener;
import base.worker.Worker;
import base.worker.pool.Listener;
import base.worker.pool.ListenerPool;
import base.worker.pool.PooledListener;

public abstract class Listen<E> extends Work implements Listener<E> {
    protected static final Worker.Type WORKER_TYPE = Worker.Type.DIRECT;

    protected Listener<E> listener;
    protected Worker.Type workerType;
    public Queue<E> queue;

    public Listen() {
        this(WORKER_TYPE);
    }

    protected Listen(Worker.Type workerType) {
        this.workerType = workerType;
        switch (workerType) {
            case DIRECT:
                return;
            case FOREGROUND:
                listener = new ForegroundListener<E>(this);
                break;
            default:
                listener = new BackgroundListener<E>(this);
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
        if (workerType.equals(Worker.Type.DIRECT)) {
            input(element);
        } else {
            listener.add(element);
        }
    }

    public void start() {
        if (workerType.equals(Worker.Type.DIRECT)) {
            try {
                activate();
            } catch (ActivateException e) {
                logger.error("Failed to start directly", e);
            }
        } else {
            super.start();
        }
    }

    public void stop() {
        super.stop();
        synchronized (this) {
            notifyAll();
        }
    }

    public void work() {
        while (!queue.isEmpty()) {
            logger.debug("Listen: work() > input");
            input(queue.poll());
        }
        synchronized (this) {
            logger.debug("Listen: work() > wait");
            try {                
                wait();                
            } catch (InterruptedException e) {}
            logger.debug("Listen: work() > notified");
        }
    }

    public void input(Object object) {
        MethodType methodType = MethodType.methodType(void.class, object.getClass());
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle;
        try {
            methodHandle = lookup.findVirtual(getClass(), "input", methodType);
            methodHandle.invoke(this, object);
        } catch (Exception e) {
            logger.error("", e);
        } catch (Throwable e) {
            logger.error("", e);
        }
    }
}
