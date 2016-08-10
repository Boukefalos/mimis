/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
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
package base.worker;

import base.work.Work;

public abstract class IntervalWork extends Work {
    protected IntervalWork() {
        this(WORKER_TYPE);
    }

    protected IntervalWork(int interval) {
        this(WORKER_TYPE, interval);
    }

    protected IntervalWork(Worker.Type workerType) {
        switch (workerType) {
            case FOREGROUND:
                worker = new DirectIntervalWorker(this);
                break;
            default:
            case BACKGROUND:
                worker = new ThreadIntervalWorker(this);
                break;
        }
    }

    protected IntervalWork(Worker.Type workerType, int interval) {
        switch (workerType) {
            case FOREGROUND:
                worker = new DirectIntervalWorker(this, interval);
                break;
            default:
            case BACKGROUND:
                worker = new ThreadIntervalWorker(this, interval);
                break;
        }
    }
}
