package mimis.worker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public abstract class Listener<E> extends Worker {
    protected Queue<E> queue;

    public Listener() {
        queue = new ConcurrentLinkedQueue<E>();
    }

    public synchronized void add(E element) {
        queue.add(element);
        notifyAll();
    }

    public final void work() {
        while (!queue.isEmpty()) {
            input(queue.poll());
        }
        if (!deactivate) {
            synchronized (this) {
                try {
                    wait();                
                } catch (InterruptedException e) {
                    logger.info("", e);
                }
            }
        }
    }

    public abstract void input(E element);
}