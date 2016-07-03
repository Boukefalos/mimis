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

import mimis.input.Button;
import mimis.value.Key;

import com.melloware.jintellitype.JIntellitype;

public class Hotkey implements Button {
    protected static final long serialVersionUID = 1L;

    protected static ArrayList<Hotkey> hotkeyList;
    protected static JIntellitype jit;

    public Hotkey(int modifier, int keycode) {
        int id = hotkeyList.size();
        jit.registerHotKey(id, modifier, keycode);
        hotkeyList.add(this);
    }

    public Hotkey(int modifier, char character) {
        this(modifier, (int) Character.toUpperCase(character));
    }

    public Hotkey(char character) {
        this(0, (int) Character.toUpperCase(character));
    }

    public Hotkey(int keycode) {
        this(0, keycode);
    }

    public Hotkey(Key key) {
        this(key.getCode());
    }

    public Hotkey(int modifier, Key key) {
        this(modifier, key.getCode());
    }

    public static void initialise(ArrayList<Hotkey> actionList, JIntellitype jit) {
        Hotkey.hotkeyList = actionList;
        Hotkey.jit = jit;
    }
}
