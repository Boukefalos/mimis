package worker;

import worker.dummy.DummyWork;


public class TestDirectWork {
	public static void main(String[] args) {
		DummyWork work = new DummyWork(1);
		work.setWork(100);
		work.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
	}
}
