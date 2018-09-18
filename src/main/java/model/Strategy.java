package model;

import java.util.List;

/**
 * Interface used for a Scheduler strategy.
 *
 * @author Vasile Lup
 * @see Scheduler
 */
public interface Strategy {

    /**
     * Adds the customer to the appropriate queue.
     * @param queues the list of available queues
     * @param customer the customer which needs to be added.
     */
    void addTask(List<Queue> queues, Customer customer);
}
