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

public enum DenonRC176Button implements LircButton {
    TAPE_AB ("TAPE_AB"),
    TAPE_REC ("TAPE_REC"),
    TAPE_PAUSE ("TAPE_PAUSE"),
    TAPE_STOP ("TAPE_STOP"),
    TAPE_REWIND ("TAPE_REW"),
    TAPE_FORWARD ("TAPE_FF"),
    TAPE_PREIVOUS ("TAPE_PLAYREV"),
    TAPE_NEXT ("TAPE_PLAY"),
    CD_PREVIOUS ("CD_TRACK_-"),
    CD_NEXT ("CD_TRACK_+"),
    CD_SHUFFLE ("CD_RANDOM"),
    CD_REPEAT ("CD_REPEAT"),
    CD_SKIP ("CD_SKIP"),
    CD_PAUSE ("CD_PAUSE"),
    CD_STOP ("CD_STOP"),
    CD_PLAY ("CD_PLAY"),
    AMP_TAPE2 ("AMP_TAPE2"),
    AMP_TAPE1 ("AMP_TAPE1"),
    AMP_AUX ("AMP_AUX"),
    AMP_TUNER ("AMP_TUNER"),
    AMP_CD ("AMP_CD"),
    AMP_PHONO ("AMP_PHONO"),
    AMP_VOLUME_UP ("AMP_VOL_UP"),
    AMP_VOLUME_DOWN ("AMP_VOL_DOWN"),
    AMP_POWER ("AMP_POWER"),
    AMP_MUTE ("AMP_MUTE"),
    TUNER_UP ("TUN_CH_UP"),
    TUNER_DOWN ("TUN_CH_DOWN");    

    public static final String NAME = "DENON_RC-176";

    protected String code;

    private DenonRC176Button(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return NAME;
    }
}
