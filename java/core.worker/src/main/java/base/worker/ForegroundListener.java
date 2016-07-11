package base.worker;

import base.work.Listen;
import base.worker.pool.Listener;

public class ForegroundListener<E> extends BackgroundListener<E> implements Listener<E> {
    public ForegroundListener(Listen<E> listen) {
        super(listen, false);
    }
}