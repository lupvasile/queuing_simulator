package view;

import model.Queue;
import model.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JPanel used for a simulationManager.
 * Contains two SimulationStatsPanels and a QueuePanel.
 *
 * @author Vasile Lup
 * @see SimulationManager
 */
public class SimulationPanel extends JPanel {
    private SimulationManager model;

    private SimulationStatsPanel simulationStatsPanelCurr;
    private JPanel allQueuesPanel;
    private List<QueuePanel> queuePanels;

    public SimulationPanel(SimulationManager model) {
        this.model = model;

        queuePanels = new ArrayList<>();
        allQueuesPanel = new JPanel();
        simulationStatsPanelCurr = new SimulationStatsPanel(model, false);

        List<Queue> queues = model.getQueues();
        queues.forEach(x -> queuePanels.add(new QueuePanel(x)));

        addComponentsToPanel(model);
        updatePanel();
    }

    private void addComponentsToPanel(SimulationManager model) {
        allQueuesPanel.setLayout(new GridLayout(1, 0, 10, 0));
        queuePanels.forEach(x -> allQueuesPanel.add(x));

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1));
        p.add(Box.createRigidArea(new Dimension(0, 0)));
        p.add(new JLabel("History stats", SwingConstants.LEFT));
        p.add(new SimulationStatsPanel(model, true));
        p.add(Box.createRigidArea(new Dimension(0, 0)));
        p.add(new JLabel("Current simulation stats", SwingConstants.LEFT));
        p.add(simulationStatsPanelCurr);

        setLayout(new BorderLayout(0, 10));
        add(p, BorderLayout.NORTH);
        add(allQueuesPanel, BorderLayout.CENTER);
    }

    public void updatePanel() {
        queuePanels.forEach(QueuePanel::updatePanel);
        simulationStatsPanelCurr.updatePanel();
    }


}
