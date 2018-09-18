package model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class used to represent a queue.
 * It can receive and process customers, on a separate thread.
 *
 * @author Vasile Lup
 */
public class Queue implements Runnable {
    private BlockingDeque<Customer> customers;
    private AtomicInteger waitingTime;
    private String name;
    private Clock clock;
    private Customer currCustomer;

    public Queue(String name, Clock clock) {
        customers = new LinkedBlockingDeque<>();
        waitingTime = new AtomicInteger(0);
        this.name = name;
        this.clock = clock;
        currCustomer = null;
    }

    /**
     * Enqueues a new customer to be serverd.
     * @param newCustomer the new customer.
     *
     * @see Customer
     */
    public void addCustomer(Customer newCustomer) {
        try {
            customers.put(newCustomer);
            waitingTime.addAndGet(newCustomer.getProcessingTime());

            MyLogger.write("Customer " + newCustomer + " arrived at queue " + this);
            MyLogger.write("New waiting time for queue: " + waitingTime.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes customers from the BlockingQueue and processes them as soon as they are available.
     * After that, the customers are thworm away.
     */
    @Override
    public void run() {
        while (true) {
            try {
                currCustomer = customers.take();
                MyLogger.write("Customer " + currCustomer + " is processed at queue " + this);

                Thread.sleep(currCustomer.getProcessingTime() * clock.TIME_UNIT);

                currCustomer.setFinnishTime(clock.getCurrTime());
                waitingTime.addAndGet(-currCustomer.getProcessingTime());
                MyLogger.write("Customer " + currCustomer + " has left queue " + this);

                currCustomer = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getWaintingTime() {
        return waitingTime.get();
    }

    public List<Customer> getCustomers() {
        List<Customer> res = new LinkedList<>(customers);
        if (currCustomer != null) res.add(0, currCustomer);
        return res;
    }

    public String getName() {
        return name;
    }

    public int getNrOfCustomers() {
        int res = customers.size();
        if (currCustomer != null) ++res;

        return res;
    }


    @Override
    public String toString() {
        return name;
    }
}
