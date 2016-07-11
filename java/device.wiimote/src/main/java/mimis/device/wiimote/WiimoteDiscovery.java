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
package mimis.device.wiimote;

import java.io.IOException;
import java.util.Scanner;

import mimis.exception.device.DeviceNotFoundException;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Work;

public class WiimoteDiscovery extends Work {
    protected static final String WIISCAN = "wiiscan";
    protected static final int TIMEOUT = 1000;
    protected WiimoteDevice wiimoteDevice;
    protected Process process;
    protected boolean disconnect;

    public WiimoteDiscovery(WiimoteDevice wiimoteDevice) {
        this.wiimoteDevice = wiimoteDevice;
    }

    protected boolean connect() {
        logger.debug("Connect");
        return execute("-c nintendo"); // Nintendo RVL-CNT-01 RVL-WBC-01 Nintendo RVL-CNT-01-TR
    }

    protected boolean disconnect() {
        logger.debug("Disconnect");
        return execute("-d nintendo");
    }

    public boolean execute(String parameters) {
        String command = WIISCAN + " -l wiiuse " + parameters;
        try {
            process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains("error: BluetoothSetServiceState()")) {
                    disconnect = true;
                    scanner.close();
                    return false;
                } else if (line.contains("[OK]")) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            process = null;
        }
        return false;
    }

    public void activate() throws ActivateException {
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        if (process != null) {
            process.destroy();
        }
    }

    public void work() {
        if (disconnect) {
            disconnect();
            disconnect = false;
        }
        if (connect()) {
            logger.debug("Connected");
            try {
                sleep(TIMEOUT);
                wiimoteDevice.connect();
            } catch (DeviceNotFoundException e) {
                disconnect = true;
            }
        }
    }
}
