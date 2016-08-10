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
package mimis.device.panel;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import mimis.Component;
import mimis.device.Device;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.value.Action;

public class PanelDevice extends Component implements Device {
    protected static final String TITLE = "Panel";
    protected Panel panel;
    protected PanelTaskMapCycle taskMapCycle;

    public PanelDevice() {
        super(TITLE);
        taskMapCycle = new PanelTaskMapCycle();
    }

    public void activate() throws ActivateException {
        panel = new Panel(this);
        parser(Action.ADD, taskMapCycle.player);
        super.activate();
    }

    public boolean active() {
        return panel != null;
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        panel.dispose();
        panel = null;
    }

    public void buttonPressed(PanelButton panelButton) {
        route(new Press(panelButton));
    }

    public void buttonReleased(PanelButton panelButton) {
        route(new Release(panelButton));
    }
}
