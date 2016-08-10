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
package mimis.input;

import mimis.input.Input;
import mimis.value.Action;
import mimis.value.Signal;
import mimis.value.Target;

public class Task implements Input {
    protected static final long serialVersionUID = 1L;

    public static final Target TARGET = Target.ALL;
    public static final Signal SIGNAL = Signal.NONE;

    protected Target target;
    protected Action action;
    protected Signal signal;

    public Task(Action action) {
        this(action, TARGET);
    }

    public Task(Action action, Target target) {
        this(action, target, SIGNAL);
    }

    public Task(Action action, Target target, Signal signal) {
        this.target = target;
        this.action = action;
        this.signal = signal;
    }

    public Target getTarget() {
        return target;
    }

    public Action getAction() {
        return action;
    }

    public Signal getSignal() {
        return signal;
    }

    public Task setSignal(Signal signal) {
        return new Task(action, target, signal);
    }
}
