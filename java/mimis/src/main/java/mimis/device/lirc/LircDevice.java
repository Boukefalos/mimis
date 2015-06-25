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
package mimis.device.lirc;

import mimis.application.cmd.CMDApplication;
import mimis.device.Device;
import mimis.device.lirc.remote.DenonRC176Button;
import mimis.device.lirc.remote.PhiliphsRCLE011Button;
import mimis.device.lirc.remote.SamsungBN5901015AButton;
import mimis.input.Button;
import mimis.input.button.ColorButton;
import mimis.input.button.NumberButton;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.util.Multiplexer;
import mimis.util.Native;
import mimis.util.multiplexer.SignalListener;
import mimis.value.Action;
import mimis.value.Signal;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

public class LircDevice extends CMDApplication implements Device, LircButtonListener, SignalListener<Button> {
    protected final static String PROGRAM = "winlirc.exe";
    protected static final String TITLE = "Lirc";

    protected Multiplexer<Button> multiplexer;
    protected LircService lircService;
    protected LircTaskMapCycle taskMapCycle;

    public LircDevice() {
        super(PROGRAM, TITLE);
        multiplexer = new Multiplexer<Button>(this);
        lircService = new LircService();
        lircService.put(PhiliphsRCLE011Button.NAME, PhiliphsRCLE011Button.values());
        lircService.put(DenonRC176Button.NAME, DenonRC176Button.values());
        lircService.put(SamsungBN5901015AButton.NAME, SamsungBN5901015AButton.values());
        lircService.add(this);
        taskMapCycle = new LircTaskMapCycle();
    }

    public void activate() throws ActivateException {
        super.activate();
        lircService.start();
        parser(Action.ADD, taskMapCycle.denonRC176);
        parser(Action.ADD, taskMapCycle.philiphsRCLE011);
        parser(Action.ADD, taskMapCycle.samsungBN5901015A);
    }

    public boolean active() {
        if (detect) {
            if (active() && !lircService.active()) {
                stop();
            } else if (!active()) {
                running = Native.isRunning(PROGRAM);
                if (running) {
                    start();
                }
            }
        }
        return super.active();
    }

    public void deactivate() throws DeactivateException {
        logger.debug("Deactivate LircDevice");
        super.deactivate();
        lircService.stop();
        multiplexer.stop();
    }

    public void exit() {
        logger.debug("Exit LircDevice");
        super.exit();
        lircService.exit();
        multiplexer.exit();
    }

    public void add(LircButton lircButton) {
        multiplexer.add(lircButton);
    }

    public void add(Signal signal, Button button) {
        add(signal, button, true);
    }

    public void add(Signal signal, Button button, boolean general) {
        switch (signal) {
            case BEGIN:
                route(new Press(button));
                break;
            case END:
                route(new Release(button));
                break;
			default:
				break;
        }

        if (general) {
            String string = button.toString();
            for (Button colorButton : ColorButton.values()) {
                if (colorButton.toString().equals(string)) {
                    add(signal, ColorButton.valueOf(string), false);
                }
            }
            for (Button numberButton : NumberButton.values()) {
                if (numberButton.toString().equals(string)) {
                    add(signal, NumberButton.valueOf(string), false);
                }
            }
        }
    }    
}
