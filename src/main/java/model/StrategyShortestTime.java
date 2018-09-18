package model;

import java.util.List;

/**
 * Adds the customer to the queue with least waiting time.
 *
 * @author Vasile Lup
 * @see Scheduler
 */
public class StrategyShortestTime implements Strategy {
    @Override
    public void addTask(List<Queue> queues, Customer customer) {
        int min = queues.get(0).getWaintingTime();
        int minInd = 0;

        for (int i = 0; i < queues.size(); ++i)
            if (min > queues.get(i).getWaintingTime()) {
                min = queues.get(i).getWaintingTime();
                minInd = i;
            }

        queues.get(minInd).addCustomer(customer);
    }
}
