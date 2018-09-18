package view;

import model.SimulationManager;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel used to show stats about a simulation.
 * If hasHistory is true, the user is provided with a JTextField to enter time for stats.
 * If hasHistory is false, the stats are shown for current simulation time.
 *
 * @author Vasile Lup
 * @see SimulationManager
 */
public class SimulationStatsPanel extends JPanel {
    private SimulationManager model;
    private JLabel waitingTimeLabel, serviceTimeLabel, emptyQueueTimeLabel, peakHourLabel, currTimeLabel;

    private JLabel userTimeLabel;
    private JTextField userTimeInput;

    private boolean hasHistory;

    public SimulationStatsPanel(SimulationManager model, boolean hasHistory) {
        this.model = model;
        this.hasHistory = hasHistory;

        setLayout(new GridLayout(0, 5));
        setBorder(BorderFactory.createEtchedBorder());

        waitingTimeLabel = new JLabel();
        serviceTimeLabel = new JLabel();
        emptyQueueTimeLabel = new JLabel();
        peakHourLabel = new JLabel();
        currTimeLabel = new JLabel();
        if (hasHistory) {
            userTimeInput = new JTextField("0");
            userTimeLabel = new JLabel("Input stats time: ");
        }

        add(waitingTimeLabel);
        add(serviceTimeLabel);
        add(emptyQueueTimeLabel);
        add(peakHourLabel);
        add(currTimeLabel);

        if (hasHistory) {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(userTimeLabel, BorderLayout.WEST);
            p.add(userTimeInput, BorderLayout.CENTER);
            add(p);

            userTimeInput.addActionListener(x -> updatePanel());
        }

        currTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        updatePanel();
    }

    public void updatePanel() {
        int shownTime = 0;

        if (hasHistory) {
            try {
                shownTime = Integer.parseInt(userTimeInput.getText());
            } catch (NumberFormatException e) {
                shownTime = 0;
            }
        } else shownTime = model.getCurrTime();

        waitingTimeLabel.setText("average waiting time: " + model.getAverageWaitingTime(shownTime));
        serviceTimeLabel.setText(" average service time: " + model.getAverageServiceTime(shownTime));
        emptyQueueTimeLabel.setText(" empty queue time: " + model.getAverageEmptyQueueTime(shownTime));
        peakHourLabel.setText(" peak hour clients: " + model.getPeakHourNrCustomers(shownTime) + " at time: " + model.getPeakHourNrCustomersTime(shownTime));
        currTimeLabel.setText("Time: " + shownTime);


    }
}
