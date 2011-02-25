package pm.device.javainput.rumblepad;

import pm.Button;
import pm.device.javainput.DirectionButton;
import pm.device.javainput.JavaInputDevice;
import pm.event.Task;
import pm.exception.button.UnknownButtonException;
import pm.exception.button.UnknownDirectionException;
import pm.exception.device.DeviceInitialiseException;
import pm.macro.state.Hold;
import pm.macro.state.Press;
import pm.task.Continuous;
import pm.task.Dynamic;
import pm.value.Action;
import pm.value.Target;
import de.hardcode.jxinput.event.JXInputButtonEvent;
import de.hardcode.jxinput.event.JXInputDirectionalEvent;

public class RumblepadDevice extends JavaInputDevice {
    protected static final String NAME = "Logitech RumblePad 2 USB";

    public void initialise() throws DeviceInitialiseException {
        super.initialise(NAME);
        add(
            new Press(RumblepadButton.ONE),
            new Task(Action.PLAY, Target.APPLICATION));
        add(
            new Press(RumblepadButton.TWO),
            new Task(Action.PAUSE, Target.APPLICATION));
        add(
            new Press(RumblepadButton.THREE),
            new Task(Action.RESUME, Target.APPLICATION));
        add(
            new Press(RumblepadButton.SIX),
            new Task(Action.NEXT, Target.APPLICATION));
        add(
            new Press(RumblepadButton.EIGHT),
            new Task(Action.PREVIOUS, Target.APPLICATION));
        add(
            new Hold(RumblepadButton.FIVE),
            new Dynamic(Action.FORWARD, Target.APPLICATION, 200, -30));
        add(
            new Hold(RumblepadButton.SEVEN),
            new Dynamic(Action.REWIND, Target.APPLICATION, 200, -30));
        add(
            new Hold(RumblepadButton.NINE),
            new Continuous(Action.VOLUME_DOWN, Target.APPLICATION, 100));
        add(
            new Hold(RumblepadButton.TEN),
            new Continuous(Action.VOLUME_UP, Target.APPLICATION, 100));
    }

    protected Button getButton(JXInputButtonEvent event) throws UnknownButtonException {
        return RumblepadButton.create(event);
    }

    protected Button getButton(JXInputDirectionalEvent event) throws UnknownDirectionException {
        return DirectionButton.create(event);
    }
}
