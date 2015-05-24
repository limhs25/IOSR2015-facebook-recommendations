package pl.recommendations.db.queue.exceptions;

/**
 * Created by marekmagik on 2015-05-20.
 */
public class EmptyQueueException extends Exception {

    public EmptyQueueException(String message) {
        super(message);
    }
}
