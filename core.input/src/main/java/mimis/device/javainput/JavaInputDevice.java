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
package mimis.device.javainput;

import mimis.Component;
import mimis.device.Device;
import mimis.exception.ButtonException;
import mimis.exception.UnknownDirectionException;
import mimis.exception.button.UnknownButtonException;
import mimis.exception.device.DeviceNotFoundException;
import mimis.input.Button;
import mimis.input.state.Press;
import mimis.input.state.Release;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import de.hardcode.jxinput.Axis;
import de.hardcode.jxinput.JXInputDevice;
import de.hardcode.jxinput.JXInputManager;
import de.hardcode.jxinput.event.JXInputAxisEvent;
import de.hardcode.jxinput.event.JXInputButtonEvent;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;

public abstract class JavaInputDevice extends Component implements Device {
    protected String name;
    protected JavaInputListener javaInputListener;
    protected Button previousDirectionalButton;

    public JavaInputDevice(String title, String name) {
        super(title);
        this.name = name;
    }

    public void activate() throws ActivateException {
        try {
            JXInputDevice jxinputDevice = getDevice(name);
            logger.debug(jxinputDevice.getName());
            javaInputListener = new JavaInputListener(this, jxinputDevice);
        } catch (DeviceNotFoundException e) {
            throw new ActivateException();
        }
        javaInputListener.start();
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        javaInputListener.stop();
    }

    public void processEvent(JXInputAxisEvent event) {
        logger.error("AxisEvent");
        Axis axis = event.getAxis();
        double delta = event.getDelta();
        System.out.println(axis.getName());
        System.out.println(axis.getResolution());
        System.out.println(axis.getType());
        System.out.println(axis.getValue());
        System.out.println(delta);
    }

    public void processEvent(JXInputButtonEvent event) throws ButtonException {
        Button button = getButton(event);
        if (event.getButton().getState()) {
            route(new Press(button));
        } else {
            route(new Release(button));
        }
    }

    public void processEvent(JXInputDirectionalEvent event) throws UnknownDirectionException {
        Button button = getButton(event);
        if (event.getDirectional().isCentered()) {
            if (previousDirectionalButton != null) {
                route(new Release(previousDirectionalButton));
            }
        } else {
            route(new Press(button));
            previousDirectionalButton = button;
        }
    }

    protected abstract Button getButton(JXInputButtonEvent event) throws UnknownButtonException;
    protected abstract Button getButton(JXInputDirectionalEvent event) throws UnknownDirectionException;

    public static JXInputDevice getDevice(String name) throws DeviceNotFoundException {
        int numberOfDevices = JXInputManager.getNumberOfDevices();
        for (int i = 0; i < numberOfDevices; ++i) {
            JXInputDevice device = JXInputManager.getJXInputDevice(i);
            if (device.getName().startsWith(name)) {
                return device;
            }
        }
        throw new DeviceNotFoundException();
    }
}
