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
package map;

import com.github.boukefalos.lirc.button.remote.SamsungBN5901015AButton;
import com.github.boukefalos.lirc.state.TaskMap;
import com.github.boukefalos.lirc.value.Action;
import com.github.boukefalos.lirc.value.Target;

import extra.Task;

public class SamsungBN5901015AEventMap extends TaskMap {
    protected static final long serialVersionUID = 1L;

    public SamsungBN5901015AEventMap() {
        receive(SamsungBN5901015AButton.VOLUME_UP, new Task(Action.VOLUME_UP, Target.CURRENT));
    }
}