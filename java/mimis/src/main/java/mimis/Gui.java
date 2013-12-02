package mimis;

import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import base.exception.worker.ActivateException;
import base.worker.Component;
import mimis.input.Feedback;
import mimis.input.Input;
import mimis.manager.ButtonManager;
import mimis.util.Swing;

public class Gui extends Component {
    public static final String ICON = "M.png";
    public static final String TITLE = "MIMIS Manager";

    protected JFrame frame;
    protected Component component;
    protected TextArea textArea;

    public Gui(final Component component, ButtonManager... buttonManagerArray) {
        frame = new JFrame(TITLE) {
            protected static final long serialVersionUID = 1L;
            protected void processWindowEvent(WindowEvent event) {
                if (event.getID() == WindowEvent.WINDOW_CLOSING) {
                    logger.debug("Window closing");
                    component.exit();
                }
            }
        };
        this.component = component;
        frame.setIconImage(Swing.getImage(ICON));
        createFrame(buttonManagerArray);
    }

    protected void activate() throws ActivateException {
        listen(Feedback.class);
        super.activate();
    }

    public void exit() {
        super.exit();
        frame.dispose();
    }

    protected void createFrame(ButtonManager... buttonManagerArray) {
        frame.setLayout(new GridLayout(0, 1));
        JPanel controlPanel = createControlPanel(buttonManagerArray);
        frame.add(controlPanel);
        JPanel feedbackPanel = createTextPanel();
        frame.add(feedbackPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
    }

    protected JPanel createControlPanel(ButtonManager... buttonManagerArray) {
        JPanel controlPanel = new JPanel(new GridLayout(1, 0));
        for (ButtonManager buttonManager  : buttonManagerArray) {
            if (buttonManager.count() > 0) {
                controlPanel.add(buttonManager.createPanel());
            }
        }
        return controlPanel;
    }

    protected JPanel createTextPanel() {
        JPanel textPanel = new JPanel();
        textArea = new TextArea();
        textArea.setEditable(false);
        textPanel.add(textArea);
        return textPanel;
    }

    public void input(Input input) {
        if (input instanceof Feedback) {
            writeLine(((Feedback) input).getText());
        }
    }

    public void write(String string) {
        textArea.append(string);
    }

    public void writeLine(String string) {
        write(string + "\n");
    }

    public void clear() {
        textArea.setText(null);
    }
}
