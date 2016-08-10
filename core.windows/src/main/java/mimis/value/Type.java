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
package mimis.value;

public enum Type {
    UP      (0x0101), // WM_KEYUP
    DOWN    (0x0100), // WM_KEYDOWN
    SYSUP   (0x0105), // WM_SYSKEYUP
    SYSDOWN (0x0104); // WM_SYSKEYDOWN

    protected int code;

    private Type(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
