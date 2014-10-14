package mimis.manager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JToggleButton;

import mimis.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.worker.Worker;

public class WorkerButton extends JToggleButton implements MouseListener {
    protected static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Worker worker;

    public WorkerButton(Worker worker) {
        this.worker = worker;
        setFocusable(false);
        addMouseListener(this);
        if (worker instanceof Component) {
            setText(((Component) worker).getTitle());
        }
    }

    public void mouseClicked(MouseEvent event) {
        if (worker.active()) {
            logger.trace("Stop");
            worker.stop();
        } else {
            logger.trace("Start");
            worker.start();
        }        
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    public void setPressed(boolean pressed) {
        if ((!isSelected() && pressed) || (isSelected() && !pressed)) {
            doClick();
        }
    }
}
