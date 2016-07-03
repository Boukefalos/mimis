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
package mimis.application.robot;

import java.awt.AWTException;
import java.awt.Robot;

import base.exception.worker.ActivateException;
import mimis.Component;
import mimis.value.Key;

public class RobotApplication extends Component {
    protected Robot robot;

    public void activate() throws ActivateException {
        try {
            robot = new Robot();
            robot.setAutoWaitForIdle(true);
        } catch (AWTException e) {
            logger.error("", e);
            throw new ActivateException();
        }
        super.activate();
    }

    public void press(Key key) {
        robot.keyPress(key.getCode());
    }

    public void press(char key) {
        robot.keyPress(key);
    }

    public void release(Key key) {
        robot.keyRelease(key.getCode());
    }
}
