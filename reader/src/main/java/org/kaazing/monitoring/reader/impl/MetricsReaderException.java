package org.kaazing.monitoring.reader.impl;

public class MetricsReaderException extends Exception {
    private static final long serialVersionUID = 1997753363232807009L;

    public MetricsReaderException(String message) {
        super(message);
    }

    public MetricsReaderException(Throwable cause) {
        super(cause);
    }

    public MetricsReaderException(String message, Throwable cause) {
        super(message, cause);
    }

}
