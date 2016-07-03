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
package mimis.device.wiimote;

import mimis.exception.button.UnknownButtonException;
import mimis.input.Button;

public enum WiimoteButton implements Button {
    TWO   (0x0001),
    ONE   (0x0002),
    B     (0x0004),
    A     (0x0008),
    MINUS (0x0010),
    HOME  (0x0080),
    LEFT  (0x0100),
    RIGHT (0x0200),
    DOWN  (0x0400),
    UP    (0x0800),
    PLUS  (0x1000),
    ALL   (0x1F9F);

    protected int code;

    private WiimoteButton(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WiimoteButton create(int code) throws UnknownButtonException {
        for (WiimoteButton button : WiimoteButton.values()) {
            if (button.getCode() == code) {
                return button;
            }
        }
        throw new UnknownButtonException();
    }
}
