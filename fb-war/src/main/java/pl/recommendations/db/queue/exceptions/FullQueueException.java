package pl.recommendations.db.queue.exceptions;

/**
 * Created by marekmagik on 2015-05-20.
 */
public class FullQueueException extends Exception {

    public FullQueueException(String message) {
        super(message);
    }
}
