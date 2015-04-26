package worker;

import worker.dummy.DummyListen;

public class TestListen {
	public static void main(String[] args) {
		DummyListen listen = new DummyListen(0);
		listen.start();
		for (int i = 0; i < 10; ++i) {
			listen.add(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
	}
}
