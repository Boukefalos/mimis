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
package mimis.device.wiimote.gesture;

import mimis.value.Signal;

import org.wiigee.device.Device;
import org.wiigee.event.AccelerationEvent;
import org.wiigee.event.AccelerationListener;
import org.wiigee.event.ButtonListener;
import org.wiigee.event.ButtonPressedEvent;
import org.wiigee.event.ButtonReleasedEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.event.MotionStartEvent;
import org.wiigee.event.MotionStopEvent;

import wiiusej.values.GForce;

public class GestureDevice extends Device implements AccelerationListener {
    public static final boolean AUTOFILTERING = true;
    public static final boolean AUTOMOTION = false;

    public GestureDevice() {
        this(AUTOFILTERING, AUTOMOTION);
    }

    public GestureDevice(boolean autofiltering, boolean automotion) {
        super(autofiltering);
        if (automotion) {
            addAccelerationListener(this);
        }
        this.setRecognitionButton(ButtonPressedEvent.BUTTON_A);
        this.setTrainButton(ButtonPressedEvent.BUTTON_B);
        this.setCloseGestureButton(ButtonPressedEvent.BUTTON_HOME);
    }

    public void add(GestureListener gestureListener) {
        addGestureListener(gestureListener);
    }

    public void add(GForce gforce) {
        add(new double[] {
            gforce.getX(),
            gforce.getY(),
            gforce.getY()});        
    }

    public void add(double[] vector) {
        //System.out.printf("%f %f %f\n", vector[0], vector[1], vector[2]);
        fireAccelerationEvent(vector);
    }

    public void recognize(Signal signal) { 
        switch (signal) {
            case BEGIN:
                fireButtonPressedEvent(ButtonPressedEvent.BUTTON_A);
                break;
            case END:
                fireButtonReleasedEvent(ButtonPressedEvent.BUTTON_A);
                break;
        }
    }

    public void train(Signal signal) {
        switch (signal) {
            case BEGIN:
                fireButtonPressedEvent(ButtonPressedEvent.BUTTON_B);
                break;
            case END:
                fireButtonReleasedEvent(ButtonPressedEvent.BUTTON_B);
                break;
        }
    }

    public void close(Signal signal) {
        switch (signal) {
            case BEGIN:
                fireButtonPressedEvent(ButtonPressedEvent.BUTTON_HOME);
                break;
            case END:
                fireButtonReleasedEvent(ButtonPressedEvent.BUTTON_HOME);
                break;
        }
    }

    public void fireButtonPressedEvent(int button) {
        ButtonPressedEvent buttonPressedEvent = new ButtonPressedEvent(this, button);
        for (ButtonListener buttonListener : buttonlistener) {
            buttonListener.buttonPressReceived(buttonPressedEvent);
        }
        if (buttonPressedEvent.isRecognitionInitEvent() || buttonPressedEvent.isTrainInitEvent()) {
            resetAccelerationFilters();
        }
    }

    public void fireButtonReleasedEvent(int button) {
        ButtonReleasedEvent buttonReleasedEvent = new ButtonReleasedEvent(this, button); 
        for (ButtonListener buttonListener : buttonlistener) {
            buttonListener.buttonReleaseReceived(buttonReleasedEvent);
        }
    }

    public void accelerationReceived(AccelerationEvent event) {}

    public void motionStartReceived(MotionStartEvent event) {
        System.out.println("Motion start !" + System.currentTimeMillis());
        recognize(Signal.BEGIN);
    }

    public void motionStopReceived(MotionStopEvent event) {
        System.out.println("Motion stop! " +  + System.currentTimeMillis());
        recognize(Signal.END);
    }
}
