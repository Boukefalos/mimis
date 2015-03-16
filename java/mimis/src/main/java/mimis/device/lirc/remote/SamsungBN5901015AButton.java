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

public enum SamsungBN5901015AButton implements LircButton {
    POWER ("Power"),
    SOURCE ("Source"),
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
    TEXT ("TTX/Mix"),
    CHANNEL_TOGGLE ("Pre-Ch"),
    VOLUME_DOWN ("Vol+"),
    VOLUME_UP ("Vol-"),
    MUTE ("Mute"),
    CHANNEL_LIST ("Ch.List"),
    CHANNEL_NEXT ("Ch+"),
    CHANNEL_PREVIOUS ("Ch-"),
    MEDIA ("Media.P"),
    MENU ("Menu"),
    GUIDE ("Guide"),
    TOOLS ("Tools"),
    UP ("Up"),
    INFO ("Info"),
    RETURN ("Return"),
    EXIT ("Exit"),
    LEFT ("Left"),
    ENTER ("Enter"),
    RIGHT ("Right"),
    DOWN ("Down"),
    RED ("Red"),
    GREEN ("Green"),
    YELLOW ("Yellow"),
    BLUE ("Blue"),
    MODE_P ("P.Mode"),
    MODE_S ("S.Mode"),
    SIZE_P ("P.Size"),
    Dual ("Dual"),
    AUDIO ("AD"),
    SUBTITLE ("Subt."),
    REWIND ("Rewind"),
    PAUSE ("Pause"),
    FORWARD ("Forward"),
    RECORD ("Record"),
    PLAY ("Play"),
    STOP ("Stop");

    public static final String NAME = "Samsung_BN59-01015A";

    protected String code;

    private SamsungBN5901015AButton(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public String getName() {
        return NAME;
    }
}
