package base.worker;

import java.util.Timer;
import java.util.TimerTask;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public class IntervalWorker extends Worker {
    protected static final boolean THREAD = true;
    protected static final int INTERVAL = 500;

	protected Timer timer;

    public synchronized void start(boolean thread) {
        if (!active) {
            activate = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
    	    	public void run() {
    	    		IntervalWorker.this.run();
    	    	}}, 0, INTERVAL);
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

    public void run() { 
        if (activate && !active) {
            try {
            	super.activate();
            } catch (ActivateException e) {
                logger.error("", e);
            } finally {
                activate = false;
            }
        } else if (deactivate && active) {
            try {            	
            	super.deactivate();
            } catch (DeactivateException e) {
				logger.error("", e);
			} finally {
                deactivate = false;
            }
        }
        if (active) {
            work();
        }
    }

	protected void work() {
		System.out.println("(-:");
	}

	public static void main(String[] args) {
		IntervalWorker intervalWorker = new IntervalWorker();
		for (int i = 0; i < 3; ++i) {
			intervalWorker.start(false);
			System.out.println("--");
			intervalWorker.sleep(200);
			intervalWorker.stop();
		}
	}
}