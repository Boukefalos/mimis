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
package mimis.application.cmd.windows;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import mimis.application.cmd.CMDApplication;
import mimis.util.Native;
import mimis.value.Command;
import mimis.value.Key;
import mimis.value.Type;
import mimis.value.Windows;

public abstract class WindowsApplication extends CMDApplication {
    protected final static int TERMINATE_SLEEP = 500;
    protected final static int START_SLEEP = 500;

    protected String window;
    protected int handle;

    public WindowsApplication(String title, String window) {
        this(null, title, window);
    }

    public WindowsApplication(String program, String title, String window) {
        super(program, title);
        this.window = window;
        handle = 0;
    }

    protected void activate() throws ActivateException {
        if (program != null) {
            super.activate();
        }
        handle = Native.getHandle(window);
        if (handle < 1) {
            sleep(START_SLEEP);
            handle = Native.getHandle(window);
        }
        active = handle > 0;
        if (!active) {
            throw new ActivateException();
        }
    }

    public boolean active() {
        if (!active || program == null) {
            handle = Native.getHandle(window);
            if (handle > 0 && program == null) {
                start();
            }
        }
        return program == null ? handle > 0 : super.active();
    }

    protected void deactivate() throws DeactivateException {
        if (process == null) {
            active = false;
        } else {
            super.deactivate();
        }
        close();
    }

    protected void close() {
        Native.sendMessage(handle, Windows.WM_CLOSE, 0, 0);
    }

    protected void command(Command command) {
        Native.sendMessage(handle, Windows.WM_APPCOMMAND, handle, command.getCode() << 16);
    }

    protected void command(int command) {
        Native.sendMessage(handle, Windows.WM_COMMAND, command, 0);
    }

    protected int user(int wParam, int lParam) {
        return Native.sendMessage(handle, Windows.WM_USER, wParam, lParam);
    }

    protected void system(Command.System system) {
        system(system, 0);
    }

    protected void system(Command.System system, int lParam) {
        Native.sendMessage(handle, Windows.WM_SYSCOMMAND, system.getCode(), lParam);
    }

    protected void key(Type type, int code) {
        int scanCode = Native.mapVirtualKey(code, Windows.MAPVK_VK_TO_VSC);
        Native.postMessage(handle, type.getCode(), code, 1 | (scanCode << 16) | 1 << 30);
        sleep(200);
    }

    protected void key(Type type, char character) {
        key(type, (int) Character.toUpperCase(character));
    }

    protected void key(Type key, Key virtualKey) {
        key(key, virtualKey.getCode());
    }
}
