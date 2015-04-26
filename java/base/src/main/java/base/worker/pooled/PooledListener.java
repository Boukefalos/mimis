package base.worker.pooled;

import java.util.concurrent.BlockingQueue;

public abstract class PooledListener<E> {
    protected BlockingQueue<Wrapper<E>> poolQueue;

	public void setPoolQueue(BlockingQueue<Wrapper<E>> poolQueue) {
		this.poolQueue = poolQueue;
	}

    public synchronized void add(E element) {
    	Wrapper<E> wrapper = new Wrapper<E>(this, element);
    	poolQueue.add(wrapper);
    }

	abstract void input(E element);
}