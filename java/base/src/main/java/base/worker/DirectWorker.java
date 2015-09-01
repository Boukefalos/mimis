package base.worker;

import base.work.Work;

public class DirectWorker extends ThreadWorker {
    public DirectWorker(Work work) {
        super(work, false);
    }
}