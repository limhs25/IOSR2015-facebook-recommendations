package pl.recommendations.db.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.queue.core.PersistentQueue;
import pl.recommendations.db.queue.core.QueueNode;

/**
 * Created by marekmagik on 2015-05-13.
 */
@Transactional
@Service
public class PersistentQueueFacadeImpl implements PersistentQueueFacade {

    private static final Logger log = LogManager.getLogger(PersistentQueueFacadeImpl.class);

    @Autowired
    @Qualifier("hpUsersQueue")
    private PersistentQueue highPriorityUsersQueue;

    @Autowired
    @Qualifier("lpUsersQueue")
    private PersistentQueue lowPriorityUsersQueue;

    @Autowired
    @Qualifier("hpFriendsQueue")
    private PersistentQueue highPriorityFriendsQueue;

    @Autowired
    @Qualifier("lpFriendsQueue")
    private PersistentQueue lowPriorityFriendsQueue;

    @Autowired
    @Qualifier("hpInterestsQueue")
    private PersistentQueue highPriorityInterestsQueue;

    @Autowired
    @Qualifier("lpInterestsQueue")
    private PersistentQueue lowPriorityInterestsQueue;


    @Override
    public boolean enqueueUser(Long id, boolean highPriority) {
        try {
            if (highPriority) {
                highPriorityUsersQueue.enqueue(id, 0, 0);
            } else {
                lowPriorityUsersQueue.enqueue(id, 0, 0);
            }
            return true;
        } catch (Exception e) {
            log.warn("Error occured while enqueuing user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean enqueueFriend(Long id, int recursiveLimit, int friendsLimit, boolean highPriority) {
        try {
            if (highPriority) {
                highPriorityFriendsQueue.enqueue(id, recursiveLimit, friendsLimit);
            } else {
                lowPriorityFriendsQueue.enqueue(id, recursiveLimit, friendsLimit);
            }
            return true;
        } catch (Exception e) {
            log.warn("Error occured while enqueuing friend: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean enqueueInterest(Long id, boolean highPriority) {
        try {
            if (highPriority) {
                highPriorityInterestsQueue.enqueue(id, 0, 0);
            } else {
                lowPriorityInterestsQueue.enqueue(id, 0, 0);
            }
            return true;
        } catch (Exception e) {
            log.warn("Error occured while enqueuing interest: " + e.getMessage());
            return false;
        }
    }

    @Override
    public QueueNode dequeueUser() {
        if (!highPriorityUsersQueue.isEmpty()) {
            return highPriorityUsersQueue.dequeue();
        }
        if (!lowPriorityUsersQueue.isEmpty()) {
            return lowPriorityUsersQueue.dequeue();
        }
        return null;
    }

    @Override
    public QueueNode dequeueFriend() {
        if (!highPriorityFriendsQueue.isEmpty()) {
            return highPriorityFriendsQueue.dequeue();
        }
        if (!lowPriorityFriendsQueue.isEmpty()) {
            return lowPriorityFriendsQueue.dequeue();
        }
        return null;
    }

    @Override
    public QueueNode dequeueInterest() {
        if (!highPriorityInterestsQueue.isEmpty()) {
            return highPriorityInterestsQueue.dequeue();
        }
        if (!lowPriorityInterestsQueue.isEmpty()) {
            return lowPriorityInterestsQueue.dequeue();
        }
        return null;
    }
}
