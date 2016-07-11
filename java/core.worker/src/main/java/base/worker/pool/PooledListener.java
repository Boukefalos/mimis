package base.worker.pool;

import java.util.concurrent.BlockingQueue;

import base.work.Listen;

public class PooledListener<E> extends PooledWorker implements Listener<E> {
    protected BlockingQueue<Wrapper<E>> poolQueue;
    protected Listen<E> listen;

    public PooledListener(Listen<E> listen) {
        super(listen);
        this.listen = listen;
    }

    public void setPoolQueue(BlockingQueue<Wrapper<E>> poolQueue) {
        this.poolQueue = poolQueue;
    }

    public synchronized void add(E element) {
        Wrapper<E> wrapper = new Wrapper<E>(this, element);
        poolQueue.add(wrapper);
    }

    void input(E element) {
        listen.input(element);
    }
}