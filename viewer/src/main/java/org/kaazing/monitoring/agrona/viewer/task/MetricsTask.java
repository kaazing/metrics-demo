package org.kaazing.monitoring.agrona.viewer.task;


/**
 * Interface for tasks responsible with retrieving collector messages and metrics
 *
 */
public interface MetricsTask {

    /**
     * Getter for the file name
     * @return
     */
    String getFileName();

    /**
     * Method performing cleanup
     */
    void cleanup();

}
