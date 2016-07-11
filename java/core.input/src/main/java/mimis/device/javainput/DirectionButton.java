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
package mimis.device.javainput;

import mimis.exception.UnknownDirectionException;
import mimis.input.Button;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;

public enum DirectionButton implements Button {
    NORTH     (0),
    NORTHEAST (45),
    EAST      (90),
    SOUTHEAST (135),
    SOUTH     (180),
    SOUTHWEST (225),
    WEST      (270),
    NORTHWEST (315);

    protected int code;

    private DirectionButton(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DirectionButton create(int angle) throws UnknownDirectionException  {
        for (DirectionButton button : DirectionButton.values()) {
            if (button.getCode() == angle) {
                return button;
            }
        }
        throw new UnknownDirectionException();
    }

    public static DirectionButton create(JXInputDirectionalEvent event) throws UnknownDirectionException {
        return create(event.getDirectional().getDirection() / 100);      
    }
}
