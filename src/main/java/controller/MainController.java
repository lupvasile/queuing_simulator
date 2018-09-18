package controller;

import model.MyLogger;
import model.SimulationManager;
import view.MainFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller providing listeners for the MainFrame.
 *
 * @author Vasile Lup
 * @see MainFrame
 */
public class MainController implements ActionListener {
    private MainFrame view;

    public void setView(MainFrame view) {
        this.view = view;

        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, nrOfQueues, nrOfCustomers, simulationDuration;

        try {
            minArrivalTime = Integer.parseInt(view.getSimulationParametersPanel().getMinArrivalTimeField());
            maxArrivalTime = Integer.parseInt(view.getSimulationParametersPanel().getMaxArrivalTimeField());
            minServiceTime = Integer.parseInt(view.getSimulationParametersPanel().getMinServiceTimeField());
            maxServiceTime = Integer.parseInt(view.getSimulationParametersPanel().getMaxServiceTimeField());
            nrOfQueues = Integer.parseInt(view.getSimulationParametersPanel().getNrOfQueuesField());
            nrOfCustomers = Integer.parseInt(view.getSimulationParametersPanel().getNrOfCustomersField());
            simulationDuration = Integer.parseInt(view.getSimulationParametersPanel().getSimulationDurationField());
        } catch (NumberFormatException ee) {
            view.showError("all inputs must be integers");
            return;
        }

        if (minArrivalTime > maxArrivalTime) {
            view.showError("min arrival time can't be bigger tha max arrival time");
            return;
        }

        if (minServiceTime > maxServiceTime) {
            view.showError("min service time can't be bigger tha max service time");
            return;
        }

        if (minServiceTime < 0 || minArrivalTime < 0 || simulationDuration < 0) {
            view.showError("a time interval can't be less than 0");
            return;
        }

        if (nrOfQueues <= 0) {
            view.showError("nr of queues must be positive integer");
            return;
        }

        if (simulationDuration == 0) {
            view.showError("simulation duration can't be 0");
            return;
        }

        SimulationManager model = new SimulationManager(minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, nrOfQueues, nrOfCustomers, simulationDuration);
        view.setModel(model);
        model.setSimulationPanel(view.getSimulationPanel());

        view.getSimulationParametersPanel().getStartSimulationButton().setEnabled(false);
        view.getSimulationParametersPanel().getStartSimulationButton().setText("Running");
        view.getSimulationParametersPanel().getStartSimulationButton().setBackground(new Color(211, 255, 0));

        Thread th = new Thread(model);
        th.start();

        //we need separate thread for finish
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }

                view.getSimulationParametersPanel().getStartSimulationButton().setText("Finished");
                view.getSimulationParametersPanel().getStartSimulationButton().setBackground(new Color(255, 179, 5));

                MyLogger.writeToFile();
            }
        }).start();
    }
}
