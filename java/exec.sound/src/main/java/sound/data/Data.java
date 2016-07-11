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
package sound.data;

public class Data {
    protected byte[] bytes;
    protected int length;

    public Data(byte[] bytes) {
        this(bytes, bytes.length);
    }

    public Data(byte[] bytes, int length) {
        this.bytes = bytes;
        this.length = length;
    }

    public byte[] get() {
        return this.bytes;
    }

    public int length() {
        return this.length;
    }
}