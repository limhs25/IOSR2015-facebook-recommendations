package pl.recommendations.db.queue.exceptions;

/**
 * Created by marekmagik on 2015-05-20.
 */
public class EntityAlreadyEnqueuedException extends Exception {

    public EntityAlreadyEnqueuedException(String message) {
        super(message);
    }
}
