package model;

import java.util.List;

/**
 * Adds a customer to the queue with least number of customers.
 *
 * @author Vasile Lup
 * @see Scheduler
 */
public class StrategyShortestQueue implements Strategy {
    @Override
    public void addTask(List<Queue> queues, Customer customer) {
        int min = queues.get(0).getWaintingTime();
        int minInd = 0;

        for (int i = 0; i < queues.size(); ++i)
            if (min > queues.get(i).getNrOfCustomers()) {
                min = queues.get(i).getNrOfCustomers();
                minInd = i;
            }

        queues.get(minInd).addCustomer(customer);
    }
}
