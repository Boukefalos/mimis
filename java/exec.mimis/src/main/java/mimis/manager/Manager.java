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
import base.work.Work;
import base.worker.IntervalWork;

public class Manager extends IntervalWork {
    protected static final int INTERVAL = 1000;

    protected ArrayList<Work> workList;

    public Manager(Work... workArray) {
        workList = new ArrayList<Work>();
        add(workArray);
    }

    public void add(Work... workArray) {
        workList.addAll(Arrays.asList(workArray));
    }

    public void remove(Work... workArray) {
        workList.removeAll(Arrays.asList(workArray));
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        for (Work worker : workList) {
            worker.stop();
        }
    }

    public void exit() {
        super.exit();
        for (Work worker : workList) {
            worker.exit();
        }
    }

    public int count() {
        return workList.size();
    }

    public void work() {
        for (Work worker : workList) {
            worker.active();
        }
    }
}
