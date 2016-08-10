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

import base.exception.worker.ActivateException;
import mimis.device.javainput.DirectionButton;
import mimis.device.javainput.JavaInputDevice;
import mimis.exception.UnknownDirectionException;
import mimis.exception.button.UnknownButtonException;
import mimis.input.Button;
import mimis.value.Action;
import de.hardcode.jxinput.event.JXInputButtonEvent;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;

public class RumblepadDevice extends JavaInputDevice {
    protected static final String TITLE = "RumblePad";
    protected static final String NAME = "Logitech RumblePad 2 USB";

    protected static RumblepadTaskMapCycle taskMapCycle;

    public RumblepadDevice() {
        super(TITLE, NAME);
        taskMapCycle = new RumblepadTaskMapCycle(); 
    }

    public void activate() throws ActivateException {
        super.activate();
        parser(Action.ADD, taskMapCycle.mimis);
        parser(Action.ADD, taskMapCycle.player);
        parser(Action.ADD, taskMapCycle.like);
    }

    protected Button getButton(JXInputButtonEvent event) throws UnknownButtonException {
        return RumblepadButton.create(event);
    }

    protected Button getButton(JXInputDirectionalEvent event) throws UnknownDirectionException {
        return DirectionButton.create(event);
    }
}
