package model;

import view.SimulationPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main class handling the simulation.
 *
 * @author Vasile Lup
 * @see Scheduler
 * @see Customer
 */
public class SimulationManager implements Clock, Runnable {
    private int minArrivalTime, maxArrivalTime;
    private int minServiceTime, maxServiceTime;
    private int nrOfQueues, nrOfCustomers;
    private int simulationDuration;

    private Scheduler scheduler;
    private List<Customer> customers;
    private AtomicInteger currTime;
    private SimulationPanel simulationPanel;

    private List<Float> averageWaitingTime, averageServiceTime, averageEmptyQueueTime;
    private List<Integer> peakHourNrCustomers, peakHourNrCustomersTime;
    private int emptyQueueTime;

    /**
     * @param minArrivalTime     the minimum arrival time between new customers
     * @param maxArrivalTime     the maximum arrival time between new customers
     * @param minServiceTime     the minumum service time for a customer
     * @param maxServiceTime     the maximum service time for a customer
     * @param nrOfQueues         the number of generated queues
     * @param nrOfCustomers      the number of generated customers
     * @param simulationDuration the time duration of the simulation
     */
    public SimulationManager(int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, int nrOfQueues, int nrOfCustomers, int simulationDuration) {
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.nrOfQueues = nrOfQueues;
        this.nrOfCustomers = nrOfCustomers;
        this.simulationDuration = simulationDuration;

        emptyQueueTime = 0;

        scheduler = new Scheduler(nrOfQueues, this, StrategySelectionPolicy.SHORTEST_TIME);
        customers = new ArrayList<>();
        currTime = new AtomicInteger(0);

        averageWaitingTime = new ArrayList<>(Collections.nCopies(simulationDuration + 1, 0f));
        averageServiceTime = new ArrayList<>(Collections.nCopies(simulationDuration + 1, 0f));
        averageEmptyQueueTime = new ArrayList<>(Collections.nCopies(simulationDuration + 1, 0f));
        peakHourNrCustomers = new ArrayList<>(Collections.nCopies(simulationDuration + 1, 0));
        peakHourNrCustomersTime = new ArrayList<>(Collections.nCopies(simulationDuration + 1, 0));

        generateCustomers();
    }

    /**
     * Generates nrOfCustomers customers, according to the parameters.
     */
    private void generateCustomers() {
        Random rand = new Random();

        for (int i = 0; i < nrOfCustomers; ++i) {
            int arrivalTime;

            if (i > 0)
                arrivalTime = customers.get(customers.size() - 1).getArrivalTime() + minArrivalTime + rand.nextInt(maxArrivalTime - minArrivalTime + 1);
            else
                arrivalTime = 1 + rand.nextInt(Math.min(10, simulationDuration));//first customer must have an arrival time >=1

            customers.add(new Customer("cst #" + (i + 1), arrivalTime,
                    minServiceTime + rand.nextInt(maxServiceTime - minServiceTime + 1)));
        }
        customers.sort((x, y) -> x.getArrivalTime() - y.getArrivalTime());
    }

    @Override
    public int getCurrTime() {
        return currTime.get();
    }

    /**
     * Increases the current time while it is less than the simulationDuration
     * Sends customers whose arrivalTime is equal to the new current time to the scheduler.
     * Updates the statistics.
     * Updates the simulationPanel
     */
    @Override
    public void run() {

        currTime.set(0);

        while (currTime.get() < simulationDuration) {

            try {
                //simulationPanel.updatePanel();
                Thread.sleep(TIME_UNIT);
                currTime.incrementAndGet();
                MyLogger.write("Current time: " + currTime);

                for (Customer c : customers)
                    if (c.getArrivalTime() == currTime.get())
                        scheduler.addCustomer(c);

                updateStats(currTime.get());

                simulationPanel.updatePanel();

            } catch (InterruptedException e) {
                e.fillInStackTrace();
            }

        }
    }

    /**
     * Updates the peak hour, average waiting time, average service time, average empty queue time.
     * Also, keeps track of all last updates.
     * @param currTime the time for which the statistics are updated.
     */
    private void updateStats(int currTime) {

        int currNrCustomers = scheduler.getNrOfCustomers();
        if (currNrCustomers > peakHourNrCustomers.get(currTime - 1)) {
            peakHourNrCustomers.set(currTime, currNrCustomers);
            peakHourNrCustomersTime.set(currTime, currTime);

            MyLogger.write("new peak number of customers: " + peakHourNrCustomers.get(currTime) + " at time: " + peakHourNrCustomersTime.get(currTime));
        } else {
            peakHourNrCustomers.set(currTime, peakHourNrCustomers.get(currTime - 1));
            peakHourNrCustomersTime.set(currTime, peakHourNrCustomersTime.get(currTime - 1));
        }

        emptyQueueTime += scheduler.getNrOfEmptyQueues();

        averageWaitingTime.set(currTime, scheduler.getAverageWaitingTime());
        averageServiceTime.set(currTime, getAverageServiceTime());
        averageEmptyQueueTime.set(currTime, 1.0f * emptyQueueTime / nrOfQueues);
    }

    public void setSimulationPanel(SimulationPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
    }

    public List<Queue> getQueues() {
        return scheduler.getQueues();
    }

    /**
     * Returns the average waiting time.
     * @param time the time for which the statistic is returned
     * @return the average waiting time.
     */
    public float getAverageWaitingTime(int time) {
        if (time > simulationDuration || time < 0) return 0;
        return averageWaitingTime.get(time);
    }

    /**
     * Returns the average customer service time.
     * @param time the time for which the statistic is returned
     * @return the average customer service time
     */
    public float getAverageServiceTime(int time) {
        if (time > simulationDuration || time < 0) return 0;
        return averageServiceTime.get(time);
    }

    /**
     * Computes the current time customer service time.
     * @return the current time customer service time.
     */
    private float getAverageServiceTime() {
        int nrCustomers = 0;
        int serviceTime = 0;

        for (Customer c : customers)
            if (c.getFinnishTime() != -1) {
                nrCustomers++;
                serviceTime += c.getServiceTime();
            }

        return 1.0f * serviceTime / nrCustomers;
    }

    /**
     * Returns the average empty queue time.
     * @param time the time for which the statistic is returned
     * @return the average empty queue time.
     */
    public float getAverageEmptyQueueTime(int time) {
        if (time > simulationDuration || time < 0) return 0;
        return averageEmptyQueueTime.get(time);
    }

    /**
     * Returns the maximum number of customers which has occured concurently until time parameter.
     * @param time the time for which the statistic is returned
     * @return the maximum number of customers which has occured concurently until time parameter.
     */
    public int getPeakHourNrCustomers(int time) {
        if (time > simulationDuration || time < 0) return 0;
        return peakHourNrCustomers.get(time);
    }

    /**
     * Returns the time at which the maximum number of customers has occured concurently until time parameter.
     * @param time the time for which the statistic is returned
     * @return the time at which the maximum number of customers has occured concurently until time parameter.
     */
    public int getPeakHourNrCustomersTime(int time) {
        if (time > simulationDuration || time < 0) return 0;
        return peakHourNrCustomersTime.get(time);
    }
}