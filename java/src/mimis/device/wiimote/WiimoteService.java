package mimis.device.wiimote;

import java.util.ArrayList;
import java.util.HashMap;

import mimis.exception.device.DeviceNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    protected Log log = LogFactory.getLog(getClass());
    
    public static void main(String[] args) {
        Log log = LogFactory.getLog(WiimoteService.class);
        for (Wiimote wm : WiiUseApiManager.getWiimotes(1, false)) {
            log.debug(wm);
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
        log.debug(event);
        if (event.isConnected()) {
            WiimoteDevice wiimoteDevice = getWiimoteDevice(event);            
            wiimoteDevice.connected = true;
            synchronized (wiimoteDevice) {
                wiimoteDevice.notifyAll();
            }
        }
    }

    public void onIrEvent(IREvent event) {}
    public void onExpansionEvent(ExpansionEvent event) {}
    public void onDisconnectionEvent(DisconnectionEvent event) {}
    public void onNunchukInsertedEvent(NunchukInsertedEvent event) {}
    public void onNunchukRemovedEvent(NunchukRemovedEvent event) {}
    public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent event) {}
    public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent event) {}
    public void onClassicControllerInsertedEvent(ClassicControllerInsertedEvent event) {}
    public void onClassicControllerRemovedEvent(ClassicControllerRemovedEvent event) {}

    public void onBalanceBoardInsertedEvent(BalanceBoardInsertedEvent e) {
        log.debug(e);        
    }

    public void onBalanceBoardRemovedEvent(BalanceBoardRemovedEvent e) {
        
    }
}
