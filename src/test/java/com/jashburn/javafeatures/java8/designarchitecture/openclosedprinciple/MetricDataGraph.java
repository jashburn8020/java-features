package com.jashburn.javafeatures.java8.designarchitecture.openclosedprinciple;

/**
 * One way of designing the MetricDataGraph class would be to have each of the new metric points
 * pushed into it from the agent that gathers the data. This would mean that every time we wanted to
 * add in a new set of time points to the plot, we would have to modify the MetricDataGraph class.
 */
public class MetricDataGraph {

    public void updateUserTime(int value) {
        // Plot how much time the computer spends in user space
    }

    public void updateSystemTime(int value) {
        // Plot how much time the computer spends in kernel space
    }

    public void updateIoTime(int value) {
        // Plot how much time the computer spends performing I/O
    }
}


/**
 * MetricDataGraph API can be simplified to not depend upon the different types of metric that it
 * needs to display. Each set of metric data can then implement the TimeSeries interface and be
 * plugged in. For example, we might have concrete classes called UserTimeSeries, SystemTimeSeries,
 * and IoTimeSeries.
 * <p>
 * If we wanted to add, say, the amount of CPU time that gets stolen from a machine if it's
 * virtualized, then we would add a new implementation of TimeSeries called StealTimeSeries.
 * <p>
 * MetricDataGraph has been extended but hasn't been modified.
 */
class MetricDataGraphOpenClosed {

    public void addTimeSeries(TimeSeries values) {

    }
}


/**
 * Represents a series of points in time.
 */
interface TimeSeries {

}
