package pl.recommendations.db.queue;

import pl.recommendations.db.queue.core.QueueNode;
import pl.recommendations.db.queue.exceptions.EmptyQueueException;

/**
 * Created by marekmagik on 2015-05-13.
 */

public interface PersistentQueueFacade {

    boolean enqueueUser(Long id, boolean highPriority);

    boolean enqueueFriend(Long id, int recursiveLimit, int friendsLimit, boolean highPriority);

    boolean enqueueInterest(Long id, boolean highPriority);

    QueueNode dequeueUser() throws EmptyQueueException;

    QueueNode dequeueFriend() throws EmptyQueueException;

    QueueNode dequeueInterest() throws EmptyQueueException;

    int getUserQueueSize();

    int getFriendsQueueSize();

    int getInterestsQueueSize();
}
