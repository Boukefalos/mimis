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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import base.work.Work;

public class ButtonManager extends Manager {
    protected static final String TITLE = "Workers";
    
    protected String title;
    protected Map<Work, WorkerButton> buttonMap;

    public ButtonManager(Work... workArray) {
        this(TITLE, workArray);
    }

    public ButtonManager(String title, Work... workArray) {
        super(workArray);
        this.title = title;
        createButtons();
    }

    public String getTitle() {
        return title;
    }

    public WorkerButton[] getButtons() {
        return buttonMap.values().toArray(new WorkerButton[]{});
    }

    protected void createButtons() {
        buttonMap = new HashMap<Work, WorkerButton>();
        for (Work worker : workList) {
            WorkerButton button = new WorkerButton(worker);
            buttonMap.put(worker, button);
        }
    }

    public JPanel createPanel() {        
        /* Initialize components */
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel panel = new JPanel(gridBagLayout);

        /* Set border */
        TitledBorder border = new TitledBorder(getTitle());
        border.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(border);

        /* Initialize constraints */
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
    
        /* Add buttons */
        for (JToggleButton button : getButtons()) {
            gridBagLayout.setConstraints(button, gridBagConstraints);
            panel.add(button);
        }
        return panel;
    }

    public void work() {
        for (Work worker : workList) {
            buttonMap.get(worker).setPressed(worker.active());
        }
    }
}
