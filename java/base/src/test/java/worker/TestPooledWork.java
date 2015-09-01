package worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import worker.dummy.DummyWork;
import base.work.Work;
import base.worker.pool.WorkerPool;

public class TestPooledWork {
    public static void main(String[] args) {
        WorkerPool workerPool = new WorkerPool(3);

        List<DummyWork> workList = new ArrayList<DummyWork>();
        for (int i = 0; i < 10; ++i) {
            DummyWork work = new DummyWork(workerPool, i + 1);
            workList.add(work);
        }
        workerPool.start();

        System.out.println("Starting work!");
        ArrayList<Work> activeWorkList = new ArrayList<Work>();
        for (int i = 0; i < 8; ++i) {
            DummyWork work = workList.get((new Random()).nextInt(workList.size()));
            work.setWork(1000);
            work.start();
            activeWorkList.add(work);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        int i = 0;
        for (Work work : activeWorkList) {
            if (++i > 5) {
                break;
            }
            work.stop();
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {}
        System.exit(0);
    }
}
