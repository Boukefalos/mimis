package base.worker;

import base.work.Work;

public class DirectIntervalWorker extends ThreadIntervalWorker {
    public DirectIntervalWorker(Work work, int interval) {
        super(work, false);
        this.interval = interval;
    }

    public DirectIntervalWorker(IntervalWork intervalWork) {
        super(intervalWork);
    }
}
