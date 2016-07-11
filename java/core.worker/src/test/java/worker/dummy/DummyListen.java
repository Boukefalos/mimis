package worker.dummy;

import base.work.Listen;
import base.worker.pool.ListenerPool;

public class DummyListen<T> extends Listen<T> {
    protected int id;

    public DummyListen(ListenerPool<T> listenerPool, int id) {
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

    public void input(byte[] input) {
        System.out.println("#" + id + ", input = " + new String(input).trim());
    }
}
