package pl.recommendations.db.queue.core.counter;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import pl.recommendations.db.queue.core.PersistentQueue;
import pl.recommendations.db.queue.types.QueueType;

/**
 * Created by marekmagik on 2015-05-10.
 */
@NodeEntity
public class CursorNode {

    private static final int QUEUE_SIZE = PersistentQueue.QUEUE_SIZE;

    @GraphId
    private Long graphID;

    @Indexed(unique = true, failOnDuplicate = true)
    private String uuid;

    @Fetch
    private Long dequeueIndex;

    @Fetch
    private Long enqueueIndex;

    public CursorNode() {
    }

    public CursorNode(QueueType queueType) {
        uuid = queueType.getCounterId();
        dequeueIndex = 0L;
        enqueueIndex = 0L;
    }

    public Long getGraphID() {
        return graphID;
    }

    public void setGraphID(Long graphID) {
        this.graphID = graphID;
    }

    public Long getDequeueIndex() {
        return dequeueIndex;
    }

    public Long getEnqueueIndex() {
        return enqueueIndex;
    }

    public void shiftDequeueIndex() {
        dequeueIndex++;
        shiftCursorIfNecessary();
    }

    public void shiftEnqueueIndex() {
        enqueueIndex++;
        shiftCursorIfNecessary();
    }

    private void shiftCursorIfNecessary() {
        if (dequeueIndex == QUEUE_SIZE) {
            dequeueIndex = 0L;
        }
        if (enqueueIndex == QUEUE_SIZE) {
            enqueueIndex = 0L;
        }
    }

}
