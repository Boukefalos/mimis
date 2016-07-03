/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import mimis.Component;
import mimis.input.Task;
import mimis.router.Router;
import mimis.value.Action;
import mimis.value.Signal;
import mimis.value.Target;
import base.util.ArrayCycle;
import base.work.Work;

public class CurrentButtonManager extends ButtonManager implements ActionListener {
    protected Router router;
    protected ArrayCycle<Component> componentCycle;
    protected Map<JRadioButton, Work> radioButtonMap;

    public CurrentButtonManager(Router router, ArrayCycle<Component> componentCycle, String title, Work... workArray) {
        super(title, workArray);
        this.componentCycle = componentCycle;
        this.router = router;
        radioButtonMap = new HashMap<JRadioButton, Work>();
    }

    public JPanel createPanel() {
        /* Initialize components */
        ButtonGroup buttonGroup = new ButtonGroup();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel panel = new JPanel(gridBagLayout);

        /* Set border */
        TitledBorder border = new TitledBorder(getTitle());
        border.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(border);

        /* Initialize constraints */
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1;

        for (WorkerButton button : getButtons()) {
            /* Add button */
            gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
            gridBagConstraints.weightx = 1;
            gridBagLayout.setConstraints(button, gridBagConstraints);
            panel.add(button);            

            /* Add radio button */
            JRadioButton radioButton = new JRadioButton();
            buttonGroup.add(radioButton);
            radioButton.addActionListener(this);
            radioButtonMap.put(radioButton,  button.work);
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 0;
            gridBagLayout.setConstraints(radioButton, gridBagConstraints);            
            panel.add(radioButton);
        }
        return panel;
    }

    public void actionPerformed(ActionEvent event) {
        JRadioButton radioButton = (JRadioButton) event.getSource();
        if (radioButtonMap.containsKey(radioButton)) {
            Work worker = radioButtonMap.get(radioButton);
            if (componentCycle.contains(worker)) {
                while (!componentCycle.current().equals(worker)) {
                    componentCycle.next();
                }
                router.add(new Task(Action.CURRENT, Target.MAIN, Signal.END));
            }        
        }
    }
    
    public void currentChanged() {
        Work worker = componentCycle.current();
        if (radioButtonMap.containsValue(worker)) {
            for (Entry<JRadioButton, Work> entry  : radioButtonMap.entrySet()) {
                if (entry.getValue().equals(worker)) {
                    JRadioButton radioButton = (JRadioButton) entry.getKey();
                    radioButton.setSelected(true);
                    return;
                }
            }
        }
        
    }
}
