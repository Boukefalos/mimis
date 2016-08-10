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
package mimis.device.javainput.rumblepad;

import mimis.exception.button.UnknownButtonException;
import mimis.input.Button;
import de.hardcode.jxinput.event.JXInputButtonEvent;

public enum RumblepadButton implements Button {
    ONE    ("Button 0"),
    TWO    ("Button 1"),
    THREE  ("Button 2"),
    FOUR   ("Button 3"),
    FIVE   ("Button 4"),
    SIX    ("Button 5"),
    SEVEN  ("Button 6"),
    EIGHT  ("Button 7"),
    NINE   ("Button 8"),
    TEN    ("Button 9");

    protected String code;

    private RumblepadButton(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static RumblepadButton create(String code) throws UnknownButtonException {
        for (RumblepadButton button : RumblepadButton.values()) {
            if (button.getCode().equals(code)) {
                return button;
            }
        }
        throw new UnknownButtonException();
    }

    public static RumblepadButton create(JXInputButtonEvent event) throws UnknownButtonException {
        return create(event.getButton().getName());      
    }
}