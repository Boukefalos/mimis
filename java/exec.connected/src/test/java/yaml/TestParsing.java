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
package yaml;

import java.util.HashMap;

import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.implementation.Local;

import base.work.ReflectiveListen;
import lirc.Lirc;

public class TestParsing extends ReflectiveListen {
	public static void main(String[] args) {
		new TestParsing().start();
	}

	protected HashMap<String, LircButton[]> buttonMap;
    //protected LircTaskMapCycle taskMapCycle;
    //buttonMap = new HashMap<String, LircButton[]>();

	public void start() {
		Local lirc = new Local();

		   /*public void put(String name, LircButton[] LircButtonArray) {
        buttonMap.put(name, LircButtonArray);
    }*/
		
		
    /*public LircButton parseButton(Scanner scanner) throws UnknownButtonException {
    try {

        LircButton[] buttonArray = buttonMap.get(remote);
        f (buttonArray != null) {            
            for (LircButton button : buttonArray) {
                if (button.getCode().equals(code)) {
                    return button;
                }
            }
        }
    } catch (InputMismatchException e) {
        logger.error("", e);
    } catch (NoSuchElementException e) {
        logger.error("", e);
    }
    throw new UnknownButtonException();
}*/
		
        /*put(PhiliphsRCLE011Button.NAME, PhiliphsRCLE011Button.values());
        put(DenonRC176Button.NAME, DenonRC176Button.values());
        put(SamsungBN5901015AButton.NAME, SamsungBN5901015AButton.values());*/

		lirc.Lirc.Signal x = Lirc.Signal.BEGIN;
		//lirc.put(PhiliphsRCLE011Button.NAME, PhiliphsRCLE011Button.values());
		lirc.start();
		super.start();
		
		try {
			Thread.sleep(100000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    public void put(String name, LircButton[] LircButtonArray) {
        buttonMap.put(name, LircButtonArray);
    }
}
