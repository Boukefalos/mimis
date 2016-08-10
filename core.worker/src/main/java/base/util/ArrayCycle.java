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
package base.util;

import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayCycle<E> extends CopyOnWriteArrayList<E> {
    protected static final long serialVersionUID = 1L;

    protected int index = 0;

    @SuppressWarnings("unchecked")
    public ArrayCycle(E... elementArray) {
        if (elementArray != null) {
            for (E element : elementArray) {
                add(element);
            }
        }
    }

    public E current() {
        return this.get(index);
    }

    public synchronized E previous() {
        if (--index < 0) {
            index = Math.max(0, size() - 1);
        }
        return get(index);
    }

    public synchronized E next() {
        if (++index >= size()) {
            index = 0;
        }
        return size() == 0 ? null : get(index);
    }

    public E reset() {
        return get(index = 0);
    }
}
