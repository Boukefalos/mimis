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
package mimis.application.lirc;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import mimis.Component;
import mimis.application.Application;
import mimis.device.lirc.LircButton;
import mimis.device.lirc.LircService;
import mimis.device.lirc.remote.WC02IPOButton;

public class LircApplication extends Component implements Application {
    protected LircService lircService;

    public LircApplication(String title) {
        super(title);
        lircService = new LircService();
        lircService.put(WC02IPOButton.NAME, WC02IPOButton.values());
    }

    public void activate() throws ActivateException {
        lircService.activate();
        super.activate();
    }

    public boolean active() {
        return active = lircService.active();
    }

    protected void deactivate() throws DeactivateException  {
        super.deactivate();
        lircService.stop();
    }

    public void exit() {
        super.exit();
        lircService.exit();
    }

    public void send(LircButton button) {
        lircService.send(button);
    }
}
