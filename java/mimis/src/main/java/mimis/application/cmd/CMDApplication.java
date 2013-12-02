package mimis.application.cmd;

import java.io.IOException;
import java.util.Map;

import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.worker.Component;
import mimis.application.Application;
import mimis.util.Native;
import mimis.value.Registry;

public abstract class CMDApplication extends Component implements Application {
    protected final static Registry REGISTRY = Registry.LOCAL_MACHINE;
    protected final static String KEY = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths";

    protected String program;
    protected String title;
    protected Process process;
    protected boolean detect, running;

    public CMDApplication(String program, String title) {
        super(title);
        this.program = program;
        this.title = title;
        detect = true;
    }

    protected void activate() throws ActivateException {
        detect = true;
        if (!running) {
            String path = getPath();
            if (path == null) {
                throw new ActivateException();
            }
            try {
                String command = path.startsWith("\"") ? path : String.format("\"%s\"", path);
                command = replaceVariables(command);
                process = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new ActivateException();
            }
        }
        super.activate();
    }

    public boolean active() {
        if (detect) {
            running = Native.isRunning(program);
            if (!active && running) {
                active = true;
                start();
            }
        }
        return active;
    }

    protected synchronized void deactivate() throws DeactivateException {
        detect = false;
        super.deactivate();
        if (process != null) {
            process.destroy();
        }
    }

    public String getPath() {
        String key = String.format("%s\\%s", KEY, program);
        System.out.println(Native.getValue(REGISTRY, key));
        return Native.getValue(REGISTRY, key);         
    }

    public static String replaceVariables(String string) {
        Map<String, String> env = System.getenv();
        for (String key : env.keySet()) {
            string = string.replace(String.format("%%%s%%", key), env.get(key));
        }
        return string;
    }
}
