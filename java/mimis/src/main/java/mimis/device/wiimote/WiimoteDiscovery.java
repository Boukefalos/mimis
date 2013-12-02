package mimis.device.wiimote;

import java.io.IOException;
import java.util.Scanner;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Worker;
import mimis.exception.device.DeviceNotFoundException;

public class WiimoteDiscovery extends Worker {
    protected static final String WIISCAN = "wiiscan";
    protected static final int TIMEOUT = 1000;
    protected WiimoteDevice wiimoteDevice;
    protected Process process;
    protected boolean disconnect;

    public WiimoteDiscovery(WiimoteDevice wiimoteDevice) {
        this.wiimoteDevice = wiimoteDevice;
    }

    protected boolean connect() {
        logger.debug("Connect");
        return execute("-c nintendo"); // Nintendo RVL-CNT-01 RVL-WBC-01
    }

    protected boolean disconnect() {
        logger.debug("Disconnect");
        return execute("-d nintendo");
    }

    public boolean execute(String parameters) {
        String command = WIISCAN + " -l wiiuse " + parameters;
        try {
            process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains("error: BluetoothSetServiceState()")) {
                    disconnect = true;
                    return false;
                } else if (line.contains("[OK]")) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            process = null;
        }
        return false;
    }

    public void activate() throws ActivateException {
        super.activate();
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        if (process != null) {
            process.destroy();
        }
    }

    protected void work() {
        if (disconnect) {
            disconnect();
            disconnect = false;
        }
        if (connect()) {
            logger.debug("Connected");
            try {
                sleep(TIMEOUT);
                wiimoteDevice.connect();
            } catch (DeviceNotFoundException e) {
                disconnect = true;
            }
        }
    }
}
