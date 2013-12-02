package mimis.device.javainput.extreme3d;

import base.exception.worker.ActivateException;
import mimis.device.javainput.DirectionButton;
import mimis.device.javainput.JavaInputDevice;
import mimis.exception.button.UnknownButtonException;
import mimis.exception.button.UnknownDirectionException;
import mimis.input.Button;
import mimis.value.Action;
import de.hardcode.jxinput.event.JXInputButtonEvent;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;

public class Extreme3DDevice extends JavaInputDevice {
    protected static final String TITLE = "Extreme 3D";
    protected static final String NAME = "Logitech Extreme 3D";

    protected static Extreme3DTaskMapCycle taskMapCycle;

    public Extreme3DDevice() {
        super(TITLE, NAME);
        taskMapCycle = new Extreme3DTaskMapCycle();
    }

    protected void activate() throws ActivateException {
        super.activate();
        parser(Action.ADD, taskMapCycle.mimis);
        parser(Action.ADD, taskMapCycle.player);
        parser(Action.ADD, taskMapCycle.like);
    }

    protected Button getButton(JXInputButtonEvent event) throws UnknownButtonException {
        return Extreme3DButton.create(event);
    }

    protected Button getButton(JXInputDirectionalEvent event) throws UnknownDirectionException {
        return DirectionButton.create(event);
    }
}
