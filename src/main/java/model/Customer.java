package model;

/**
 * Class used to represent a customer.
 *
 * @author Vasile Lup
 */
public class Customer {
    private String name;
    private int arrivalTime, processingTime, finnishTime;

    public Customer(String name, int arrivalTime, int processingTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.finnishTime = -1;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getFinnishTime() {
        return finnishTime;
    }

    public void setFinnishTime(int finnishTime) {
        this.finnishTime = finnishTime;
    }

    /**
     * Currently, the service time is defined as finnishTime - arrivalTime;
     * @return the service time
     */
    public int getServiceTime() {
        return finnishTime - arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public String toString() {
        return name + "(" + arrivalTime + "," + processingTime + ")";
    }
}
