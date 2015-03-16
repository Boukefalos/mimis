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
package mimis.device.jintellitype;

import mimis.exception.button.UnknownButtonException;
import mimis.input.Button;

import com.melloware.jintellitype.JIntellitype;


public enum CommandButton implements Button {
    BROWSER_BACKWARD       (JIntellitype.APPCOMMAND_BROWSER_BACKWARD),
    BROWSER_FORWARD        (JIntellitype.APPCOMMAND_BROWSER_FORWARD),
    BROWSER_REFRESH        (JIntellitype.APPCOMMAND_BROWSER_REFRESH),
    BROWSER_STOP           (JIntellitype.APPCOMMAND_BROWSER_STOP),
    BROWSER_SEARCH         (JIntellitype.APPCOMMAND_BROWSER_SEARCH),
    BROWSER_FAVOURITES     (JIntellitype.APPCOMMAND_BROWSER_FAVOURITES),
    BROWSER_HOME           (JIntellitype.APPCOMMAND_BROWSER_HOME),
    VOLUME_MUTE            (JIntellitype.APPCOMMAND_VOLUME_MUTE),
    VOLUME_DOWN            (JIntellitype.APPCOMMAND_VOLUME_DOWN),
    VOLUME_UP              (JIntellitype.APPCOMMAND_VOLUME_UP),
    MEDIA_NEXTTRACK        (JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK),
    MEDIA_PREVIOUSTRACK    (JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK),
    MEDIA_STOP             (JIntellitype.APPCOMMAND_MEDIA_STOP),
    MEDIA_PLAY_PAUSE       (JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE),
    LAUNCH_MAIL            (JIntellitype.APPCOMMAND_LAUNCH_MAIL),
    LAUNCH_MEDIA_SELECT    (JIntellitype.APPCOMMAND_LAUNCH_MEDIA_SELECT),
    LAUNCH_APP1            (JIntellitype.APPCOMMAND_LAUNCH_APP1),
    LAUNCH_APP2            (JIntellitype.APPCOMMAND_LAUNCH_APP2),
    BASS_DOWN              (JIntellitype.APPCOMMAND_BASS_DOWN),
    BASS_BOOST             (JIntellitype.APPCOMMAND_BASS_BOOST),
    BASS_UP                (JIntellitype.APPCOMMAND_BASS_UP),
    TREBLE_DOWN            (JIntellitype.APPCOMMAND_TREBLE_DOWN),
    TREBLE_UP              (JIntellitype.APPCOMMAND_TREBLE_UP),
    MICROPHONE_VOLUME_MUTE (JIntellitype.APPCOMMAND_MICROPHONE_VOLUME_MUTE),
    MICROPHONE_VOLUME_DOWN (JIntellitype.APPCOMMAND_MICROPHONE_VOLUME_DOWN),
    MICROPHONE_VOLUME_UP   (JIntellitype.APPCOMMAND_MICROPHONE_VOLUME_UP),
    HELP                   (JIntellitype.APPCOMMAND_HELP),
    FIND                   (JIntellitype.APPCOMMAND_FIND),
    NEW                    (JIntellitype.APPCOMMAND_NEW),
    OPEN                   (JIntellitype.APPCOMMAND_OPEN),
    CLOSE                  (JIntellitype.APPCOMMAND_CLOSE),
    SAVE                   (JIntellitype.APPCOMMAND_SAVE),
    PRINT                  (JIntellitype.APPCOMMAND_PRINT),
    UNDO                   (JIntellitype.APPCOMMAND_UNDO),
    REDO                   (JIntellitype.APPCOMMAND_REDO),
    COPY                   (JIntellitype.APPCOMMAND_COPY),
    CUT                    (JIntellitype.APPCOMMAND_CUT),
    PASTE                  (JIntellitype.APPCOMMAND_PASTE),
    REPLY_TO_MAIL          (JIntellitype.APPCOMMAND_REPLY_TO_MAIL),
    FORWARD_MAIL           (JIntellitype.APPCOMMAND_FORWARD_MAIL),
    SEND_MAIL              (JIntellitype.APPCOMMAND_SEND_MAIL),
    SPELL_CHECK            (JIntellitype.APPCOMMAND_SPELL_CHECK),
    DICTATE_OR_COMMAND_CONTROL_TOGGLE (JIntellitype.APPCOMMAND_DICTATE_OR_COMMAND_CONTROL_TOGGLE),
    MIC_ON_OFF_TOGGLE      (JIntellitype.APPCOMMAND_MIC_ON_OFF_TOGGLE),
    CORRECTION_LIST        (JIntellitype.APPCOMMAND_CORRECTION_LIST);

    protected int code;

    private CommandButton(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CommandButton create(int code) throws UnknownButtonException {
        for (CommandButton button : CommandButton.values()) {
            if (button.getCode() == code) {
                return button;
            }
        }
        throw new UnknownButtonException();
    }
}
