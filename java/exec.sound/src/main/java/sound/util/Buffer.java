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
package sound.util;

public class Buffer {
    protected byte[] elements;
    public int capacity;
    protected int index;
    protected int size;

    public Buffer(int capacity) {
        elements = new byte[capacity];
        this.capacity = capacity;
        index = 0;
        size = 0;
    }

    public synchronized void add(byte[] elements) {
        for (byte element : elements) {
            elements[index++ % capacity] = element;
            if (size < capacity) {
                ++size;
            }
        }
    }

    public synchronized void write(byte[] elements, int offset, int length) {
        for (int i = offset; i < length; ++i) {
            this.elements[(index++ % capacity)] = elements[i];
            if (size < capacity) {
                ++size;
            }
        }
    }

    public synchronized byte[] get() {
        byte[] elements = new byte[size];
        for (int i = 0; i < size; i++) {
            elements[i] = elements[((index + i) % size)];
        }
        return elements;
    }
}