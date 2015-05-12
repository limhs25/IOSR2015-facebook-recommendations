package pl.recommendations.db.queue.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.queue.core.counter.CursorNode;
import pl.recommendations.db.queue.core.counter.CursorRepository;
import pl.recommendations.db.queue.types.QueueType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by marekmagik on 2015-05-10.
 */
@Transactional
public class PersistentQueueImpl implements PersistentQueue {

    private final QueueType queueType;

    private Map<Long, QueueNode> queue;

    private CursorNode counter;

    @Autowired
    private QueueRepository queueRepo;

    @Autowired
    private CursorRepository counterRepo;

    public PersistentQueueImpl(QueueType queueType){
        this.queueType = queueType;
    }

    @PostConstruct
    private void init() {
        reload();
    }

    @Override
    public synchronized void enqueue(Long userId, int recursiveLimit, int friendsLimit) {
        Long index = counter.getEnqueueIndex();
        QueueNode node = queue.get(index);
        if (node != null) {
            throw new IndexOutOfBoundsException("Queue is full. Requested index: " + index);
        }

        if (contains(userId)) {
            throw new EntityExistsException("Element " + userId + " already exists in queue.");
        }

        QueueNodeConfig config = (new QueueNodeConfig.Builder())
                .setUserId(userId) //
                .setFriendsLimit(friendsLimit) //
                .setRecursiveLimit(recursiveLimit) //
                .setIndex(index) //
                .setQueueType(getQueueType()) //
                .build();

        node = new QueueNode(config);

        queueRepo.save(node);
        queue.put(index, node);

        counter.shiftEnqueueIndex();
        counterRepo.saveCounter(counter);
    }

    @Override
    public synchronized QueueNode dequeue() {
        Long index = counter.getDequeueIndex();
        QueueNode node = queue.get(index);

        if (node == null) {
            throw new IndexOutOfBoundsException("Queue is empty. Requested index: " + index);
        }

        queueRepo.delete(node);
        queue.remove(node.getIndex());

        counter.shiftDequeueIndex();
        counterRepo.saveCounter(counter);

        return node;
    }

    public boolean contains(Long userId) {
        for (Long key : queue.keySet()) {
            if (queue.get(key).getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public QueueType getQueueType(){
        return queueType;
    }

    @Override
    public void reload() {
        counter = counterRepo.getCounter(getQueueType());
        queue = new LinkedHashMap<>(QUEUE_SIZE);
        Set<QueueNode> allNodes = queueRepo.getNodesForSpecifiedQueue(getQueueType());
        allNodes.forEach(queueNode -> queue.put(queueNode.getIndex(), queueNode));
    }
}
