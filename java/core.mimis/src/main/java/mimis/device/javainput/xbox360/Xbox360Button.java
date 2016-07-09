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
package mimis.device.javainput.xbox360;

import mimis.exception.button.UnknownButtonException;
import mimis.input.Button;
import de.hardcode.jxinput.event.JXInputButtonEvent;

public enum Xbox360Button implements Button {
    GREEN       ("Button 0"), // A
    RED         ("Button 1"), // B
    BLUE        ("Button 2"), // X
    YELLOW      ("Button 3"), // Y
    LB          ("Button 4"),
    RB          ("Button 5"),
    BACK        ("Button 6"),
    START       ("Button 7"),
    LEFT_STICK  ("Button 8"),
    RIGHT_STICK ("Button 9");

    protected String code;

    private Xbox360Button(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Xbox360Button create(String code) throws UnknownButtonException {
        for (Xbox360Button button : Xbox360Button.values()) {
            if (button.getCode().equals(code)) {
                return button;
            }
        }
        throw new UnknownButtonException();
    }

    public static Xbox360Button create(JXInputButtonEvent event) throws UnknownButtonException {
        return create(event.getButton().getName());      
    }
}