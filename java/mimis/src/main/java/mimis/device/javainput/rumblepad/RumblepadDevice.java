package mimis.device.javainput.rumblepad;

import base.exception.worker.ActivateException;
import mimis.device.javainput.DirectionButton;
import mimis.device.javainput.JavaInputDevice;
import mimis.exception.button.UnknownButtonException;
import mimis.exception.button.UnknownDirectionException;
import mimis.input.Button;
import mimis.value.Action;
import de.hardcode.jxinput.event.JXInputButtonEvent;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;

public class RumblepadDevice extends JavaInputDevice {
    protected static final String TITLE = "RumblePad";
    protected static final String NAME = "Logitech RumblePad 2 USB";

    protected static RumblepadTaskMapCycle taskMapCycle;

    public RumblepadDevice() {
        super(TITLE, NAME);
        taskMapCycle = new RumblepadTaskMapCycle(); 
    }

    protected void activate() throws ActivateException {
        super.activate();
        parser(Action.ADD, taskMapCycle.mimis);
        parser(Action.ADD, taskMapCycle.player);
        parser(Action.ADD, taskMapCycle.like);
    }

    protected Button getButton(JXInputButtonEvent event) throws UnknownButtonException {
        return RumblepadButton.create(event);
    }

    protected Button getButton(JXInputDirectionalEvent event) throws UnknownDirectionException {
        return DirectionButton.create(event);
    }
}
