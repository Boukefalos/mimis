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
package mimis.device.javainput.xbox360;

import base.exception.worker.ActivateException;
import de.hardcode.jxinput.event.JXInputButtonEvent;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;
import mimis.device.javainput.DirectionButton;
import mimis.device.javainput.JavaInputDevice;
import mimis.exception.UnknownDirectionException;
import mimis.exception.button.UnknownButtonException;
import mimis.input.Button;
import mimis.value.Action;

public class Xbox360Device extends JavaInputDevice {
    protected static final String TITLE = "Xbox360";
    protected static final String NAME = "Controller (XBOX 360 For Windows)";

    protected static Xbox360TaskMapCycle taskMapCycle;

    public Xbox360Device() {
        super(TITLE, NAME);
        taskMapCycle = new Xbox360TaskMapCycle(); 
    }

    public void activate() throws ActivateException {
        super.activate();
        parser(Action.ADD, taskMapCycle.mimis);
        parser(Action.ADD, taskMapCycle.player);
    }

    protected Button getButton(JXInputButtonEvent event) throws UnknownButtonException {
        return Xbox360Button.create(event);
    }

    protected Button getButton(JXInputDirectionalEvent event) throws UnknownDirectionException {
        return DirectionButton.create(event);
    }
}
