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
package mimis.device.wiimote.motion;

import java.io.Serializable;

import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class MotionData implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected int time;
    protected MotionSensingEvent event;

    public MotionData(long time, MotionSensingEvent event) {
        this((int) time, event);
    }

    public MotionData(int time, MotionSensingEvent event) {
        this.time = time;
        this.event = event;
    }

    public int getTime() {
        return time;
    }

    public MotionSensingEvent getEvent() {
        return event;
    }
}
