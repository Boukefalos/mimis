package base.worker;

import java.util.Timer;
import java.util.TimerTask;

import base.work.Work;

public class ThreadIntervalWorker extends ThreadWorker {
	protected static final int INTERVAL = 500;
	protected int interval;

    public ThreadIntervalWorker(Work work) {
		super(work);
		interval = INTERVAL;
	}

    public ThreadIntervalWorker(Work work, boolean thread) {
    	super(work, thread);
		interval = INTERVAL;
	}

	public ThreadIntervalWorker(Work work, int interval) {
		super(work);
		this.interval = interval;
	}

	protected Timer timer;

    public synchronized void start(boolean thread) {
        if (!active) {
            activate = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
    	    	public void run() {
    	    		Worker worker = ThreadIntervalWorker.this;    	    		
    	    		worker.runActivate();
    	    		worker.runDeactivate();
    	    		worker.runWork();
    	    	}}, 0, interval);
    		active = true;
        }
        if (!thread) {
        	 try {
                 synchronized (this) {
                     wait();
                 }
             } catch (InterruptedException e) {
                 logger.info("", e);
             }
        }
    }

    public synchronized void stop() {
        if (active) {
        	timer.cancel();
            deactivate = true;
            run();
            notifyAll();
        }
    }
}