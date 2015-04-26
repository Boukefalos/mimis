package base.worker.pooled;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestPooledListener extends PooledListener<Integer> {
	protected int id;

	public TestPooledListener(int id) {		
		this.id = id;
	}

	public void input(Integer element) {
		System.out.println("#" + id + ": " + element);				
	}

    public static void main(String[] args) {
    	ListenerPool<Integer> listenerPool = new ListenerPool<Integer>(5);    	
    	List<PooledListener<Integer>> listenerList = new ArrayList<PooledListener<Integer>>();
    	for (int i = 0; i < 20; ++i) {
    		PooledListener<Integer> pooledListener = listenerPool.add(new TestPooledListener(i + 1));
    		listenerList.add(pooledListener);
    	}

    	listenerPool.start();

    	System.out.println("Starting to give out elements!");
    	for (int i = 0; i < 100; ++i) {
	    	PooledListener<Integer> randomListener = listenerList.get((new Random()).nextInt(listenerList.size()));
	    	randomListener.add(i);
    	}
    	
    	//listenerPool.await();
    }
}
