package pl.recommendations.db.queue.types;

/**
 * Created by marekmagik on 2015-05-13.
 */
public enum QueueType {
    HIGH_PRIORITY_USERS_QUEUE("HP_USER_QUEUE_", 1),
    LOW_PRIORITY_USERS_QUEUE("LP_USER_QUEUE_", 2),
    HIGH_PRIORITY_INTERESTS_QUEUE("HP_INTERESTS_QUEUE_", 3),
    LOW_PRIORITY_INTERESTS_QUEUE("LP_INTERESTS_QUEUE_", 4),
    HIGH_PRIORITY_FRIENDS_QUEUE("HP_FRIENDS_QUEUE_", 5),
    LOW_PRIORITY_FRIENDS_QUEUE("LP_FRIENDS_QUEUE_", 6);

    private static final String POSTFIX = "COUNTER";

    private final int typeId;

    private final String prefix;

    QueueType(String prefix, int typeId) {
        this.prefix = prefix;
        this.typeId = typeId;
    }

    public String getIndexPrefix() {
        return prefix;
    }

    public String getCounterId() {
        return prefix + POSTFIX;
    }

    public int getTypeId() {
        return typeId;
    }
}
