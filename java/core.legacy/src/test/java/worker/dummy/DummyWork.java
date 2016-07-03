package worker.dummy;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;
import base.worker.pool.WorkerPool;

public class DummyWork extends Work {
    protected int id;
    protected volatile int work;

    public DummyWork(int id) {
        super();
        this.id = id;
    }

    public DummyWork(WorkerPool workerPool, int id) {
        super(workerPool);
        this.id = id;
    }

    public void setWork(int work) {
        System.out.println("#" + id + ", set work @ " + work);
        this.work = work;
    }

    public void work() {
        System.out.println("#" + id + ", work = " + work);
        if (--work < 1) {
            stop();
        }
        sleep(300);
    }

    public void activate() throws ActivateException {
        System.out.println("#" + id + ", activating...");
    }

    public void deactivate() throws DeactivateException {
        System.out.println("#" + id + ", deactivating...");
    }
}
