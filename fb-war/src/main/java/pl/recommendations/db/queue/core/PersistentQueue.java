package pl.recommendations.db.queue.core;

/**
 * Created by marekmagik on 2015-05-11.
 */
public interface PersistentQueue {

    int QUEUE_SIZE = 100;

    void enqueue(Long userId, int recursiveLimit, int friendsLimit);

    QueueNode dequeue();

    boolean contains(Long userId);

    boolean isEmpty();

    void reload();
}
