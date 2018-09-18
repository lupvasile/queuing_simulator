package model;

/**
 * Interface for a clock.
 *
 * @author Vasile Lup
 */
interface Clock {
    int TIME_UNIT = 1000;//a unit in milliseconds

    int getCurrTime();
}

