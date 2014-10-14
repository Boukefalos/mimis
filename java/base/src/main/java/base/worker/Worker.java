package base.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public abstract class Worker implements Runnable {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final boolean THREAD = true;
    protected static final int SLEEP = 100;

    protected boolean thread = true;
    protected boolean run = false;
    protected boolean active = false;
    protected boolean activate = false;
    protected boolean deactivate = false;

    public Worker(boolean thread) {
    	this.thread = thread;
    }

    public Worker() {
    	this(THREAD);
    }

    public synchronized void start(boolean thread) {
        if (!active) {
            activate = true;
        }
        if (!run) {
            run = true;
            if (thread) {
                logger.debug("Start thread");
                new Thread(this, getClass().getName()).start();
            } else {
                logger.debug("Run directly");
                run();
            }
        } else {
            notifyAll();
        }
    }

    public synchronized void start() {
        start(thread);
    }

    public synchronized void stop() {
        if (active) {
            deactivate = true;
        }
        notifyAll();
    }

    public void exit() {
        stop();
        run = false;
    }

    protected void sleep(int time) {
        try {
            if (time > 0) {
                Thread.sleep(time);
            }
        } catch (InterruptedException e) {
            logger.info("", e);
        }
    }

    protected void sleep() {
        sleep(SLEEP);
    }

    public boolean active() {
        return active;
    }

    protected void activate() throws ActivateException {
        active = true;
    }

    protected void deactivate() throws DeactivateException {
        active = false;
    }

    public void run() {
        while (run || deactivate) {
            if (activate && !active) {
                try {
                    activate();
                } catch (ActivateException e) {
                    logger.error("", e);
                } finally {
                    activate = false;
                }
            } else if (deactivate && active) {
                try {
                   deactivate();
                } catch (DeactivateException e) {
                    logger.error("", e);
                } finally {
                    deactivate = false;
                }
            }
            if (active) {
                work();
            } else if (run) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    logger.info("", e);
                }
            }
        }
    }

    protected abstract void work();
}
