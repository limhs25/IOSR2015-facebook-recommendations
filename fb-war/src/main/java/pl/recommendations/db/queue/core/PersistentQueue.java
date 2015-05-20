package pl.recommendations.db.queue.core;

import pl.recommendations.db.queue.exceptions.EmptyQueueException;
import pl.recommendations.db.queue.exceptions.EntityAlreadyEnqueuedException;
import pl.recommendations.db.queue.exceptions.FullQueueException;

/**
 * Created by marekmagik on 2015-05-11.
 */
public interface PersistentQueue {

    int QUEUE_SIZE = 100;

    void enqueue(Long userId, int recursiveLimit, int friendsLimit) throws EntityAlreadyEnqueuedException, FullQueueException;

    QueueNode dequeue() throws EmptyQueueException;

    boolean contains(Long userId);

    boolean isEmpty();

    void reload();

    int size();
}
