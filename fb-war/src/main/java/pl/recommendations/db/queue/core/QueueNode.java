package pl.recommendations.db.queue.core;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import pl.recommendations.db.queue.types.QueueType;

/**
 * Created by marekmagik on 2015-05-10.
 */
@NodeEntity
public class QueueNode {

    @GraphId
    private Long graphID;

    @Indexed(unique = true, failOnDuplicate = true)
    private String uuid;

    @Fetch
    private Long index;

    @Fetch
    private Long userId;

    @Fetch
    private int recursiveLimit;

    @Fetch
    private int friendsLimit;

    @Fetch
    private QueueType queueType;


    public QueueNode() {
    }

    public QueueNode(QueueNodeConfig config) {
        this.recursiveLimit = config.getRecursiveLimit();
        this.friendsLimit = config.getFriendsLimit();
        this.index = config.getIndex();
        this.queueType = config.getQueueType();
        this.uuid = config.getQueueType().getIndexPrefix() + config.getIndex();
        this.userId = config.getUserId();
    }

    public Long getUserId() {
        return userId;
    }

    public Long getIndex() {
        return index;
    }

    public int getRecursiveLimit() {
        return recursiveLimit;
    }

    public int getFriendsLimit() {
        return friendsLimit;
    }

    public QueueType getQueueType() {
        return queueType;
    }

}
