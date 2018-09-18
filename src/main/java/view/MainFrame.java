package view;

import model.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main JFrame, containing the parameters and simulation panels
 *
 * @author Vasile Lup
 * @see SimulationPanel
 * @see SimulationParametersPanel
 */
public class MainFrame extends JFrame {
    private SimulationPanel simulationPanel;
    private SimulationParametersPanel simulationParametersPanel;

    public MainFrame(ActionListener startSimulationListener) {
        setTitle("Queue simulator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(400, 300);
        setMinimumSize(new Dimension(600, 500));

        simulationParametersPanel = new SimulationParametersPanel(startSimulationListener);

        setLayout(new BorderLayout());
        add(simulationParametersPanel, BorderLayout.NORTH);

        pack();
    }

    public void setModel(SimulationManager model) {
        simulationPanel = new SimulationPanel(model);
        add(simulationPanel, BorderLayout.CENTER);
        pack();
    }

    public SimulationPanel getSimulationPanel() {
        return simulationPanel;
    }

    public SimulationParametersPanel getSimulationParametersPanel() {
        return simulationParametersPanel;
    }

    public void showError(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

}
