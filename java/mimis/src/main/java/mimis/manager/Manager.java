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
import base.worker.IntervalWorker;
import base.worker.Worker;

public class Manager extends IntervalWorker {
    protected static final int INTERVAL = 1000;

    protected ArrayList<Worker> workerList;

    public Manager(Worker... workerArray) {
        workerList = new ArrayList<Worker>();
        add(workerArray);
    }

    public void add(Worker... workerArray) {
        workerList.addAll(Arrays.asList(workerArray));
    }

    public void remove(Worker... workerArray) {
        workerList.removeAll(Arrays.asList(workerArray));
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        for (Worker worker : workerList) {
            worker.stop();
        }
    }

    public void exit() {
        super.exit();
        for (Worker worker : workerList) {
            worker.exit();
        }
    }

    public int count() {
        return workerList.size();
    }

    protected void work() {
        for (Worker worker : workerList) {
            worker.active();
        }
    }
}
