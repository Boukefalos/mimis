/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mimis.manager;

import java.util.ArrayList;
import java.util.Arrays;

import base.exception.worker.DeactivateException;
import base.worker.ThreadIntervalWorker;
import base.worker.ThreadWorker;

public class Manager extends ThreadIntervalWorker {
    protected static final int INTERVAL = 1000;

    protected ArrayList<ThreadWorker> workerList;

    public Manager(ThreadWorker... workerArray) {
        workerList = new ArrayList<ThreadWorker>();
        add(workerArray);
    }

    public void add(ThreadWorker... workerArray) {
        workerList.addAll(Arrays.asList(workerArray));
    }

    public void remove(ThreadWorker... workerArray) {
        workerList.removeAll(Arrays.asList(workerArray));
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        for (ThreadWorker worker : workerList) {
            worker.stop();
        }
    }

    public void exit() {
        super.exit();
        for (ThreadWorker worker : workerList) {
            worker.exit();
        }
    }

    public int count() {
        return workerList.size();
    }

    protected void work() {
        for (ThreadWorker worker : workerList) {
            worker.active();
        }
    }
}
