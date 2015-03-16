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
package mimis.device.jintellitype;

import java.util.ArrayList;

import mimis.Component;
import mimis.device.Device;
import mimis.exception.button.UnknownButtonException;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.value.Action;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

public class JIntellitypeDevice extends Component implements Device, HotkeyListener, IntellitypeListener {
    protected static final String TITLE = "Keyboard";

    protected JIntellitypeTaskMapCycle taskMapCycle;
    protected ArrayList<Hotkey> hotkeyList;
    protected JIntellitype jit;

    public JIntellitypeDevice() {
        super(TITLE);
        hotkeyList = new ArrayList<Hotkey>();
        jit = JIntellitype.getInstance();
        Hotkey.initialise(hotkeyList, jit);
        taskMapCycle = new JIntellitypeTaskMapCycle();
    }

    protected void activate() throws ActivateException {
        super.activate();
        jit.addHotKeyListener(this);
        jit.addIntellitypeListener(this);
        parser(Action.ADD, taskMapCycle.mimis);
        parser(Action.ADD, taskMapCycle.player);
    }

    public void onIntellitype(int command) {
        if (active) {
            try {
            CommandButton commandButton = CommandButton.create(command);
            route(new Press(commandButton));
            route(new Release(commandButton));
            } catch (UnknownButtonException e) {
                logger.error("", e);
            }
        }
    }

    public void onHotKey(int id) {
        if (active) {
            Hotkey hotkey = hotkeyList.get(id);
            route(new Press(hotkey));
            route(new Release(hotkey));
        }
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        jit.removeHotKeyListener(this);
        jit.removeIntellitypeListener(this);
    }

    public void exit() {
        super.exit();
        jit.cleanUp();
    }
}
