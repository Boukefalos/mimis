package worker.dummy;

import base.worker.IntervalWork;

public class DummyIntervalWork extends IntervalWork {
    public DummyIntervalWork(int interval) {
        super(interval);
    }

    public void work() {
        System.out.println(":-)");        
    }
}