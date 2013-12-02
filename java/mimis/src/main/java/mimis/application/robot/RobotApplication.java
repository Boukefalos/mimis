package mimis.application.robot;

import java.awt.AWTException;
import java.awt.Robot;

import base.exception.worker.ActivateException;
import base.worker.Component;
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
