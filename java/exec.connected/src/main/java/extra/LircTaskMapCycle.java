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
package extra;

import map.DenonRC176EventMap;
import map.PhiliphsRCLE011EventMap;
import map.SamsungBN5901015AEventMap;

import com.github.boukefalos.lirc.state.TaskMap;
import com.github.boukefalos.lirc.state.TaskMapCycle;

public class LircTaskMapCycle extends TaskMapCycle {
    protected static final long serialVersionUID = 1L;

    public TaskMap denonRC176, philiphsRCLE011, samsungBN5901015A;

    public LircTaskMapCycle() {      
        register(denonRC176 = new DenonRC176EventMap());
        register(philiphsRCLE011 = new PhiliphsRCLE011EventMap());
        register(samsungBN5901015A = new SamsungBN5901015AEventMap());
    }
}