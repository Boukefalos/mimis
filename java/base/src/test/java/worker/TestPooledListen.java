package worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import worker.dummy.DummyListen;
import base.worker.pool.ListenerPool;

public class TestPooledListen {
	protected int id;

	public TestPooledListen(int id) {		
		this.id = id;
	}

	public void input(Integer element) {
		System.out.println("#" + id + ": " + element);				
	}

    public static void main(String[] args) {
    	ListenerPool<Integer> listenerPool = new ListenerPool<Integer>(5);	
    	List<DummyListen<Integer>> listenList = new ArrayList<DummyListen<Integer>>();
    	for (int i = 0; i < 20; ++i) {
    		DummyListen<Integer> listen = new DummyListen<Integer>(listenerPool, i + 1);
    		listenList.add(listen);
    	}
    	listenerPool.start();

    	System.out.println("Starting to give out elements!");
    	for (int i = 0; i < 100; ++i) {
	    	DummyListen<Integer> randomListen = listenList.get((new Random()).nextInt(listenList.size()));
	    	randomListen.add(i);
    	}

    	//listenerPool.await();
    }
}
