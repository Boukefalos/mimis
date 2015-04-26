package base.worker.pool;

import java.util.concurrent.BlockingQueue;

class ListenerPoolRunnable<E> implements Runnable {
	protected BlockingQueue<Wrapper<E>> queue;
	protected int id;

	public ListenerPoolRunnable(BlockingQueue<Wrapper<E>> queue, int id) {
		this.queue = queue;
		this.id = id;
	}

	public void run() {
		try {
			while (true) {
				System.out.println("Thread #" + id + " waiting...");
				Wrapper<E> wrapper = queue.take();
				wrapper.deliver();
				Thread.sleep((int) (Math.random() * 1000));
			}
		} catch (InterruptedException e) {}			
	}
}