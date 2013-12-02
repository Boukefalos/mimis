package mimis.device.panel;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Component;
import mimis.device.Device;
import mimis.input.state.Press;
import mimis.input.state.Release;
import mimis.value.Action;

public class PanelDevice extends Component implements Device {
    protected static final String TITLE = "Panel";
    protected Panel panel;
    protected PanelTaskMapCycle taskMapCycle;

    public PanelDevice() {
        super(TITLE);
        taskMapCycle = new PanelTaskMapCycle();
    }

    protected void activate() throws ActivateException {
        panel = new Panel(this);
        parser(Action.ADD, taskMapCycle.player);
        super.activate();
    }

    public boolean active() {
        return active = panel != null;
    }

    protected void deactivate() throws DeactivateException {
        super.deactivate();
        panel.dispose();
        panel = null;
    }

    public void buttonPressed(PanelButton panelButton) {
        route(new Press(panelButton));
    }

    public void buttonReleased(PanelButton panelButton) {
        route(new Release(panelButton));
    }
}
