package mimis.application.lirc;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import mimis.Component;
import mimis.application.Application;
import mimis.device.lirc.LircButton;
import mimis.device.lirc.LircService;
import mimis.device.lirc.remote.WC02IPOButton;

public class LircApplication extends Component implements Application {
    protected LircService lircService;

    public LircApplication(String title) {
        super(title);
        lircService = new LircService();
        lircService.put(WC02IPOButton.NAME, WC02IPOButton.values());
    }

    public void activate() throws ActivateException {
        lircService.activate();
        super.activate();
    }

    public boolean active() {
        return active = lircService.active();
    }

    protected void deactivate() throws DeactivateException  {
        super.deactivate();
        lircService.stop();
    }

    public void exit() {
        super.exit();
        lircService.exit();
    }

    public void send(LircButton button) {
        lircService.send(button);
    }
}
