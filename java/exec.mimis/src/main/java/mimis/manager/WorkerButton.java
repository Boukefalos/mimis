/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mimis.manager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JToggleButton;

import mimis.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.work.Work;

public class WorkerButton extends JToggleButton implements MouseListener {
    protected static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Work work;

    public WorkerButton(Work worker) {
        this.work = worker;
        setFocusable(false);
        addMouseListener(this);
        if (worker instanceof Component) {
            setText(((Component) worker).getTitle());
        }
    }

    public void mouseClicked(MouseEvent event) {
        if (work.active()) {
            logger.trace("Stop");
            work.stop();
        } else {
            logger.trace("Start");
            work.start();
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
