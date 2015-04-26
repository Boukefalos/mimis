package worker.dummy;

import base.work.Listen;
import base.worker.pool.ListenerPool;

public class DummyListen extends Listen<Integer> {
	protected int id;

	public DummyListen(ListenerPool<Integer> listenerPool, int id) {
		super(listenerPool);
		this.id = id;
	}

	public DummyListen(int id) {
		super();
		this.id = id;
	}

	public void input(Integer input) {
		System.out.println("#" + id + ", input = " + input);
	}
}
