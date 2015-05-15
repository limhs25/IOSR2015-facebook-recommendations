package pl.recommendations.db.queue.core;

import pl.recommendations.db.queue.types.QueueType;

/**
 * Created by marekmagik on 2015-05-14.
 */
public class QueueNodeConfig {

    private final Long index;

    private final Long userId;

    private final int recursiveLimit;

    private final int friendsLimit;

    private final QueueType queueType;

    private QueueNodeConfig(Builder builder) {
        this.index = builder.index;
        this.userId = builder.userId;
        this.recursiveLimit = builder.recursiveLimit;
        this.friendsLimit = builder.friendsLimit;
        this.queueType = builder.queueType;
    }

    public Long getIndex() {
        return index;
    }

    public Long getUserId() {
        return userId;
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

    public static class Builder {
        private Long index;
        private Long userId;
        private int recursiveLimit;
        private int friendsLimit;
        private QueueType queueType;

        public Builder setIndex(Long index) {
            this.index = index;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setRecursiveLimit(int recursiveLimit) {
            this.recursiveLimit = recursiveLimit;
            return this;
        }

        public Builder setFriendsLimit(int friendsLimit) {
            this.friendsLimit = friendsLimit;
            return this;
        }

        public Builder setQueueType(QueueType queueType) {
            this.queueType = queueType;
            return this;
        }

        public QueueNodeConfig build() {
            return new QueueNodeConfig(this);
        }
    }
}