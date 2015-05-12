package pl.recommendations.db.queue;

import pl.recommendations.db.queue.core.QueueNode;

/**
 * Created by marekmagik on 2015-05-13.
 */

public interface PersistentQueueFacade {

    boolean enqueueUser(Long id, boolean highPriority);

    boolean enqueueFriend(Long id, int recursiveLimit, int friendsLimit, boolean highPriority);

    boolean enqueueInterest(Long id, boolean highPriority);

    QueueNode dequeueUser();

    QueueNode dequeueFriend();

    QueueNode dequeueInterest();
}
