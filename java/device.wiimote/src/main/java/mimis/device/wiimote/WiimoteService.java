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
package mimis.device.wiimote;

import java.util.ArrayList;
import java.util.HashMap;

import mimis.exception.device.DeviceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.GenericEvent;
import wiiusej.wiiusejevents.physicalevents.ExpansionEvent;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.BalanceBoardInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.BalanceBoardRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.DisconnectionEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.StatusEvent;

public class WiimoteService extends WiiUseApiManager implements WiimoteListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(WiimoteService.class);
        for (Wiimote wm : WiiUseApiManager.getWiimotes(1, false)) {
            logger.debug("" + wm.getId());
        }
    }

    protected final boolean RUMBLE = false;

    protected ArrayList<Integer> wiimoteList;
    protected Wiimote[] wiimoteArray;
    protected HashMap<Integer, WiimoteDevice> wiimoteDeviceMap;

    public WiimoteService() {
       wiimoteList = new ArrayList<Integer>();
       wiimoteArray = new Wiimote[0];
       wiimoteDeviceMap = new HashMap<Integer, WiimoteDevice>();
    }

    public void exit() {
        if (wiimoteArray != null) {
            for (Wiimote wiimote : wiimoteArray) {
                wiimote.disconnect();
            }
        }
        definitiveShutdown();
    }

    public Wiimote getDevice(WiimoteDevice wiimoteDevice) throws DeviceNotFoundException {
        Wiimote[] wiimoteArray = getWiimotes(1, RUMBLE);
        for (Wiimote wiimote : wiimoteArray) {
            int id = wiimote.getId();
            if (!wiimoteList.contains(id)) {
                wiimote.addWiiMoteEventListeners(this);
                wiimoteList.add(id);
                wiimoteDeviceMap.put(id, wiimoteDevice);
            }
            return wiimote;
        }
        throw new DeviceNotFoundException();
    }

    public Wiimote getWiimote(GenericEvent event) {
        return wiimoteArray[event.getWiimoteId() - 1];
    }

    public WiimoteDevice getWiimoteDevice(GenericEvent event){
        return wiimoteDeviceMap.get(event.getWiimoteId());
    }

    public void onButtonsEvent(WiimoteButtonsEvent event) {
        getWiimoteDevice(event).onButtonsEvent(event);
    }

    public void onMotionSensingEvent(MotionSensingEvent event) {

        getWiimoteDevice(event).onMotionSensingEvent(event);
    }

    public void onStatusEvent(StatusEvent event) {
        if (event.isConnected()) {
            WiimoteDevice wiimoteDevice = getWiimoteDevice(event);            
            wiimoteDevice.connected = true;
            synchronized (wiimoteDevice) {
                wiimoteDevice.notifyAll();
            }
        }
    }

    public void onIrEvent(IREvent event) {
        getWiimoteDevice(event).onIrEvent(event);
    }

    public void onExpansionEvent(ExpansionEvent event) {}
    public void onDisconnectionEvent(DisconnectionEvent event) {}
    public void onNunchukInsertedEvent(NunchukInsertedEvent event) {}
    public void onNunchukRemovedEvent(NunchukRemovedEvent event) {}
    public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent event) {}
    public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent event) {}
    public void onClassicControllerInsertedEvent(ClassicControllerInsertedEvent event) {}
    public void onClassicControllerRemovedEvent(ClassicControllerRemovedEvent event) {}
	public void onBalanceBoardInsertedEvent(BalanceBoardInsertedEvent event) {}
	public void onBalanceBoardRemovedEvent(BalanceBoardRemovedEvent event) {}
}
