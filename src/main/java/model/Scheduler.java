package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for receiving new customers and dispatching them to the appropiate queue,
 * in accordance with a strategy.
 *
 * @author Vasile Lup
 * @see Customer
 * @see Queue
 * @see Strategy
 * @see StrategySelectionPolicy
 */
public class Scheduler {
    private List<Queue> queues;
    private int nrOfQueues;
    private Clock clock;
    private Strategy strategy;

    public Scheduler(int nrOfQueues, Clock clock, StrategySelectionPolicy policy) {
        this.nrOfQueues = nrOfQueues;
        queues = new ArrayList<>();
        this.clock = clock;
        changeStrategy(policy);

        startQueues();
    }

    /**
     * Changes the current selection policy to a new one.
     * @param policy the new policy to be applied.
     */
    public void changeStrategy(StrategySelectionPolicy policy) {
        if (policy == StrategySelectionPolicy.SHORTEST_QUEUE) {
            strategy = new StrategyShortestQueue();
        }
        if (policy == StrategySelectionPolicy.SHORTEST_TIME) {
            strategy = new StrategyShortestTime();
        }
    }

    public void addCustomer(Customer newCustomer) {
        MyLogger.write("Displatching Customer " + newCustomer);
        strategy.addTask(getQueues(), newCustomer);
    }

    /**
     * Generates nrOfQueues Queue objects and for each object creates a thread and starts it.
     */
    private void startQueues() {
        for (int i = 1; i <= nrOfQueues; ++i)
            queues.add(new Queue("Q" + i, clock));

        for (Queue q : queues) {
            new Thread(q).start();
        }
    }

    /**
     * Computes the average waiting time of the queues.
     * @return the average waiting time of the queues
     */
    public float getAverageWaitingTime() {
        float res = 0;
        for (Queue q : queues)
            res += q.getWaintingTime();

        return res / nrOfQueues;
    }

    public int getNrOfCustomers() {
        int res = 0;
        for (Queue q : queues)
            res += q.getNrOfCustomers();

        return res;
    }

    public int getNrOfEmptyQueues() {
        int res = 0;
        for (Queue q : queues)
            if (q.getNrOfCustomers() == 0)
                ++res;

        return res;
    }

    public List<Queue> getQueues() {
        return queues;
    }
}
