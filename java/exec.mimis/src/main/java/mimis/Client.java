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
package mimis;

import base.exception.worker.ActivateException;
import mimis.application.Application;
import mimis.device.Device;
import mimis.input.Task;
import mimis.router.GlobalRouter;
import mimis.util.swing.Dialog;

public class Client extends Main {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 6789;

    public Client(String ip, int port) {
        super();
        router = new GlobalRouter(ip, port) {
            protected boolean target(Task task, Component component) {
                switch (task.getTarget()) {
            		case ALL:
                        return true;
            		case MAIN:
                    case CURRENT:
                        return component instanceof Main;
                    case DEVICES:
                        return component instanceof Device;
                    case APPLICATIONS:
                        return component instanceof Application;
                    default:
                        return false;
                }
            }
        };
    }

    public void activate() throws ActivateException {
        super.activate();
    }

    public static void main(String[] args) {
        String ip = Dialog.question("Server IP:", IP);
        int port = Integer.valueOf(Dialog.question("Server Port:", PORT));
        new Client(ip, port).start();
    }
}