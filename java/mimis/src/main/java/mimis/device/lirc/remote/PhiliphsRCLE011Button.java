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
package mimis.device.lirc.remote;

import mimis.device.lirc.LircButton;

public enum PhiliphsRCLE011Button implements LircButton {
    POWER ("Standby"),
    RED ("Red"),
    GREEN ("Green"),
    YELLOW ("Yellow"),
    BLUE ("Blue"),
    TUNE ("Tune"),
    RADIO ("Radio"),
    SQUARE ("Square"),
    MENU ("Menu"),
    TEXT ("Text"),
    UP ("Up"),
    DOWN ("Down"),
    LEFT ("Left"),
    RIGHT ("Right"),
    VOLUME_UP ("Volume+"),
    VOLUME_DOWN ("Volume-"),
    MUTE ("Mute"),
    PROGRAM_UP ("Program+"),
    PROGRAM_DOWN ("Program-"),
    ONE ("1"),
    TWO ("2"),
    THREE ("3"),
    FOUR ("4"),
    FIVE ("5"),
    SIX ("6"),
    SEVEN ("7"),
    EIGHT ("8"),
    NINE ("9"),
    ZERO ("0"),
    CLOCK ("Clock"),
    OUT ("Out"),
    INFO ("i+"),
    SCREEN_UP ("screenup"),
    SCREEN_DOWN ("screendown"),
    QUESTION ("question");

    public static final String NAME = "Philips_RCLE011";

    protected String code;

    private PhiliphsRCLE011Button(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return NAME;
    }
}
