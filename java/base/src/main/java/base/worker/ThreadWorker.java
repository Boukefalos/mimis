package base.worker;

import base.work.Work;

public class ThreadWorker extends Worker implements Runnable {
    protected static final boolean THREAD = true;

    protected boolean thread = true;

    public ThreadWorker(Work work, boolean thread) {
    	this(work);
		this.thread = thread;
	}

	public ThreadWorker(Work work) {
		super(work);
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
    	super.stop();
        if (active) {
            deactivate = true;
        }
        notifyAll();
    }

    public void exit() {
        run = false;
        stop();
    }
}
