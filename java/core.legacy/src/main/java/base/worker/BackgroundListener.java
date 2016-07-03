package base.worker;

import base.work.Listen;
import base.worker.pool.Listener;

public class BackgroundListener<E> extends ThreadWorker implements Listener<E> {
    protected Listen<E> listen;

    public BackgroundListener(Listen<E> listen) {
        this(listen, true);
    }

    public BackgroundListener(Listen<E> listen, boolean thread) {
       super(listen, thread);
       this.listen = listen;
    }

    public void add(E element) {
        listen.notify();
    }
}
