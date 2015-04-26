package base.worker.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ListenerPool<E> {
	protected int poolSize;
	protected BlockingQueue<Wrapper<E>> queue;
	protected ExecutorService executorService;

	public ListenerPool(int poolSize) {
		this.poolSize = poolSize;
		queue = new LinkedBlockingQueue<Wrapper<E>>();
		executorService = Executors.newFixedThreadPool(poolSize);
	}

	public PooledListener<E> add(PooledListener<E> listener) {
		listener.setPoolQueue(queue);
		return listener;
	}

	public void start() {
    	for (int i = 0; i < poolSize; ++i) {
    		Runnable runnable = new ListenerPoolRunnable<E>(queue, i);
        	executorService.execute(runnable);
    	}
	}

	public void await() {
    	try {
			executorService.awaitTermination(0, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}		
	}
	
}
