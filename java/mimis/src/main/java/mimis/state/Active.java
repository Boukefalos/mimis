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
package mimis.state;

import mimis.input.state.Hold;
import mimis.input.state.Press;
import mimis.input.state.State;
import mimis.input.state.sequence.Sequence;

public class Active {
    protected Sequence sequence;
    protected int step;

    public Active(Sequence sequence) {
        this.sequence = sequence;
        step = -1;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public boolean next(State state) {
        State next = sequence.get(++step);
        if (next == null) {
            return false;
        }
        boolean type = state instanceof Press && next instanceof Hold;
        return state.equals(next, type);
    }

    public boolean first() {
        return step == 0;
    }

    public boolean last() {
        return step == sequence.count() - 1;
    }
}
