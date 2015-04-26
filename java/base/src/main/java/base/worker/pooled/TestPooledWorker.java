package base.worker.pooled;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Worker;

public class TestPooledWorker extends PooledWorker {
	protected int id;
	protected volatile int work;

	public TestPooledWorker(int id) {
		this.id = id;
	}

	protected void setWork(int work) {
		System.out.println("#" + id + ", set work @ " + work);
		this.work = work;
	}

	public void work() {
		System.out.println("#" + id + ", work = " + work);
		if (--work < 1) {
			stop();
		}
		sleep(300);		
	}

	protected void activate() throws ActivateException {
		System.out.println("#" + id + ", activating...");
		super.activate();
	}

	protected void deactivate() throws DeactivateException {
		super.deactivate();
		System.out.println("#" + id + ", deactivating...");
	}

	public static void main(String[] args) {
		WorkerPool workerPool = new WorkerPool(3);    	

    	List<TestPooledWorker> workerList = new ArrayList<TestPooledWorker>();
    	for (int i = 0; i < 10; ++i) {
    		TestPooledWorker worker = new TestPooledWorker(i + 1);
    		workerPool.add(worker);
    		workerList.add(worker);
    	}
    	workerPool.start();

    	System.out.println("Starting work!");
    	int work = 1000;
    	ArrayList<Worker> activeWorkerList = new ArrayList<Worker>();
    	for (int i = 0; i < 8; ++i) {
	    	TestPooledWorker worker = workerList.get((new Random()).nextInt(workerList.size()));
	    	worker.setWork(work);    		
			worker.start();
			activeWorkerList.add(worker);
    	}
    	int i = 0;


    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}

    	for (Worker worker : activeWorkerList) {
    		if (++i > 5) {
    			break;
    		}
			worker.stop();
    	}
    	try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {}
    	System.exit(0);
	}
}
