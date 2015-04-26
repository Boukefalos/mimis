package base.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public abstract class Worker {
	public enum Type {
		DIRECT, THREAD, POOLED
	}

    protected static final int SLEEP = 100;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean run = false;
    protected boolean active = false;
    protected boolean activate = false;
    protected boolean deactivate = false;

    protected Work work;

	public Worker(Work work) {
		this.work = work;
	}

	public abstract void start();

	public abstract void stop();

    public boolean active() {
        return active;
    }

    public void run() {
        while (run || deactivate) {
    		runActivate();
    		runDeactivate();
    		runWork();
        }
    }

    public void runActivate() {
    	if (activate && !active) {
            try {
            	work.activate();            	
            } catch (ActivateException e) {
                logger.error("", e);
            } finally {
                activate = false;
                active = true;
            }
        }
    }
    
    public void runDeactivate() {
    	if (deactivate && active) {
            try {
               work.deactivate();
            } catch (DeactivateException e) {
                logger.error("", e);
            } finally {
                deactivate = false;
                active = false;
            }
        }
    }

    public void runWork() {
        if (active) {
        	work.work();
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

    protected void sleep() {
        sleep(SLEEP);
    }

    public void sleep(int time) {
        try {
            if (time > 0) {
                Thread.sleep(time);
            }
        } catch (InterruptedException e) {
            logger.info("", e);
        }
    }
}
