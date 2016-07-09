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

package mimis.application.cmd.windows.lightroom;

import mimis.application.cmd.windows.WindowsApplication;
import mimis.input.Task;
import mimis.value.Action;
import winapi.Amount;
import winapi.Slider;
import winapi.Test;
import base.exception.worker.ActivateException;
import base.util.ArrayCycle;

public class LightroomApplication extends WindowsApplication {
    protected final static String PROGRAM = "lightroom.exe";
    protected final static String TITLE = "Lightroom";
    protected final static String WINDOW = "";

    protected static Test test;
    protected ArrayCycle<Slider> sliderCycle;

    public LightroomApplication() {
        super(PROGRAM, TITLE, WINDOW);
        detect = false;
        running = true;
        test = new Test();
        try {
            test.start();
            sliderCycle = new ArrayCycle<Slider>(Slider.values());
            test.moveSlider(sliderCycle.current(), Amount.INCREASE_LITTLE);
        } catch (Exception e) {
            logger.error("", e);
            //throw new ActivateException();
        }
    }

    public static void main(String[] args) {
        new LightroomApplication();
    }

    /*public boolean active() {
        return true;
    }*/

    public void activate() throws ActivateException {
        listen(Task.class);
        super.activate();
    }

    public void begin(Action action) {
        logger.trace("LightroomApplication begin: " + action);
        try {
            switch (action) {
                case NEXT:    
                    test.moveSlider(sliderCycle.current(), Amount.INCREASE_LITTLE);
                    break;
                case PREVIOUS:
                    test.moveSlider(sliderCycle.current(), Amount.DECREASE_LITTLE);
                    break;
                case VOLUME_UP:
                    sliderCycle.previous();
                    break;
                case VOLUME_DOWN:
                    sliderCycle.next();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("Slider = " + test.getValue(sliderCycle.current()));
    }

    public void end(Action action) {
        logger.trace("LightroomApplication end: " + action);
        switch (action) {
            default:
                break;
        }
    }
}