package worker;

import worker.dummy.DummyIntervalWork;
import base.work.Work;

public class TestIntervalWork {
    public static void main(String[] args) {
        Work work = new DummyIntervalWork(500);
        for (int i = 0; i < 10; ++i) {
            work.start();
            System.out.println("--");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            work.stop();
        }
    }
}
