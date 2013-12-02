package mimis.manager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import base.worker.Worker;

public class ButtonManager extends Manager {
    protected static final String TITLE = "Workers";
    
    protected String title;
    protected Map<Worker, WorkerButton> buttonMap;

    public ButtonManager(Worker... workerArray) {
        this(TITLE, workerArray);
    }

    public ButtonManager(String title, Worker... workerArray) {
        super(workerArray);
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
        buttonMap = new HashMap<Worker, WorkerButton>();
        for (Worker worker : workerList) {
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

    protected void work() {
        for (Worker worker : workerList) {
            buttonMap.get(worker).setPressed(worker.active());
        }
    }
}
