package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * JPanel used for inputing simulation constraints.
 *
 * @author Vasile Lup
 */
public class SimulationParametersPanel extends JPanel {

    private JTextField minArrivalTimeField, maxArrivalTimeField, minServiceTimeField, maxServiceTimeField, nrOfQueuesField, nrOfCustomersField, simulationDurationField;
    private JButton startSimulationButton;

    public SimulationParametersPanel(ActionListener startSimulationButtonListener) {
        minArrivalTimeField = new JTextField("0");
        maxArrivalTimeField = new JTextField("3");
        minServiceTimeField = new JTextField("2");
        maxServiceTimeField = new JTextField("15");
        nrOfQueuesField = new JTextField("5");
        nrOfCustomersField = new JTextField("100");
        simulationDurationField = new JTextField("60");
        startSimulationButton = new JButton("Start");

        startSimulationButton.addActionListener(startSimulationButtonListener);

        addComponents();
    }

    private void addComponents() {
        setLayout(new GridLayout(0, 4));

        addWithLabel("Min arrival time: ", minArrivalTimeField, true);
        addWithLabel("Max arrival time: ", maxArrivalTimeField, true);
        addWithLabel("Min service time: ", minServiceTimeField, true);
        addWithLabel("Max service time: ", maxServiceTimeField, false);
        addWithLabel("Nr. of queues: ", nrOfQueuesField, true);
        addWithLabel("Nr. of customers: ", nrOfCustomersField, true);
        addWithLabel("Simulation duration: ", simulationDurationField, true);
        add(startSimulationButton);
    }

    private void addWithLabel(String str, JComponent comp, boolean hasSpaceAfter) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(str), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        if (hasSpaceAfter) p.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
        add(p);
    }

    public String getMinArrivalTimeField() {
        return minArrivalTimeField.getText();
    }

    public String getMaxArrivalTimeField() {
        return maxArrivalTimeField.getText();
    }

    public String getMinServiceTimeField() {
        return minServiceTimeField.getText();
    }

    public String getMaxServiceTimeField() {
        return maxServiceTimeField.getText();
    }

    public String getNrOfQueuesField() {
        return nrOfQueuesField.getText();
    }

    public String getNrOfCustomersField() {
        return nrOfCustomersField.getText();
    }

    public String getSimulationDurationField() {
        return simulationDurationField.getText();
    }

    public JButton getStartSimulationButton() {
        return startSimulationButton;
    }
}
