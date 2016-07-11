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
package dummy;

import lirc.Lirc.Color;
import lirc.Lirc.Direction;
import lirc.Lirc.Number;
import lirc.Lirc.Signal;
import base.work.Listen;

import com.github.boukefalos.ibuddy.iBuddy;
import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.util.SignalObject;

public class Dummy extends Listen<Object> {
	protected Lirc lirc;
	protected iBuddy iBuddy;

	public Dummy(Lirc lirc, iBuddy iBuddy) {
		this.lirc = lirc;
		this.iBuddy = iBuddy;
		lirc.register(this);
	}

	public void start() {
		lirc.start();
		super.start();
	}

	public void input(SignalObject<Object> signalObject) {
		Signal signal = signalObject.signal;
		Object object = signalObject.object;
		System.out.println(object);
		try {
			// Move these mappings to config file?
			if (object instanceof LircButton) {
				LircButton lircButton = (LircButton) object;
		        String code = lircButton.code;
		        logger.error(signal.name() + " : " + code + " @ " + lircButton.remote);
			} else if (object instanceof Color) {
				Color color = (Color) object;
				System.err.println("Color: " + color);
				try {
					switch (color) {					
						case RED:
							iBuddy.setHeadRed(signal.equals(Signal.BEGIN));
							break;
						case GREEN:
							iBuddy.setHeadGreen(signal.equals(Signal.BEGIN));
							break;
						case YELLOW:
							if (signal.equals(Signal.BEGIN)) {
								iBuddy.setHead(proto.Ibuddy.Color.YELLOW);
							} else {
								iBuddy.setHead(proto.Ibuddy.Color.NONE);
							}
							break;
						case BLUE:
							iBuddy.setHeadBlue(signal.equals(Signal.BEGIN));
							break;
						default:
							break;
					}
				} catch (Exception e) {}			
			} else if (object instanceof Number) {
				Number number = (Number) object;
				// Find way to reuse enum?
				proto.Ibuddy.Color color = proto.Ibuddy.Color.valueOf(number.getNumber());
				if (signal.equals(Signal.BEGIN)) {
					System.err.println("Number: " + number + " Color: " + color);
					iBuddy.setHead(color);
				} else {
					iBuddy.setHead(proto.Ibuddy.Color.NONE);
				}	
			} else if (object instanceof Direction && signal.equals(Signal.BEGIN)) {
				Direction direction = (Direction) object;
				System.err.println("Direction: " + direction);
				switch (direction) {
					case LEFT:
						iBuddy.setRotateLeft();
						break;
					case RIGHT:
						iBuddy.setRotateRight();
						break;
					case UP:
						iBuddy.setWingsUp();
						break;
					case DOWN:
						iBuddy.setWingsDown();
					default:
						iBuddy.setRotateCenter();
						iBuddy.setWingsCenter();
						break;
				}
			}
		} catch (Exception e) {}
	}
}
