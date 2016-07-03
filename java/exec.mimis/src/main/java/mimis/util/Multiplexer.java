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
package mimis.util;

import mimis.util.multiplexer.SignalListener;
import mimis.value.Signal;
import base.work.Work;

public class Multiplexer<T> extends Work {
    public static final int TIMEOUT = 150;

    protected int threshold;
    protected T type;
    protected SignalListener<T> signalListener;
    protected boolean end;

    public Multiplexer(SignalListener<T> signalListener) {
        this(signalListener, TIMEOUT);
    }

    public Multiplexer(SignalListener<T> signalListener, int treshold) {
        this.signalListener = signalListener;
    }

    public synchronized void add(T object) {
        if (type == null) {
            signalListener.add(Signal.BEGIN, object);
            type = object;
            end = true;
            start();
        } else if (type.equals(object)) {
            end = false;
            notifyAll();
        } else {
            end = true;
            synchronized (this) {
                notifyAll();
            }
            add(object);
        }
    }

    public void work() {
        try {
            synchronized (this) {
                wait(TIMEOUT);
            }
        } catch (InterruptedException e) {}
        if (end) {
            signalListener.add(Signal.END, type);
            type = null;
            stop();
        }
        end = !end;
    }
}
