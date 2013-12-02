package mimis.util;

import base.worker.Worker;
import mimis.util.multiplexer.SignalListener;
import mimis.value.Signal;

public class Multiplexer<T> extends Worker {
    public static final int TIMEOUT = 150;

    protected int threshold;
    protected T type;
    protected SignalListener<T> signalListener;
    protected boolean end;

    public Multiplexer(SignalListener<T> signalListener) {
        this(signalListener, TIMEOUT);
    }

    public Multiplexer(SignalListener<T> signalListener, int treshold) {
        this.signalListener = signalListener;
    }

    public synchronized void add(T object) {
        if (this.type == null) {
            signalListener.add(Signal.BEGIN, object);
            this.type = object;
            end = true;
            start();
        } else if (this.type.equals(object)) {
            end = false;
            notifyAll();
        } else {
            end = true;
            synchronized (this) {
                notifyAll();
            }
            add(object);
        }
    }

    protected void work() {
        try {
            synchronized (this) {
                wait(TIMEOUT);
            }
        } catch (InterruptedException e) {}
        if (end) {
            signalListener.add(Signal.END, type);
            type = null;
            stop();
        }
        end = !end;
    }
}
