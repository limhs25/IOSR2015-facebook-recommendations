package pl.recommendations.crawling;

public class CrawlTask {
    private final Long uuid;
    private final int recursiveLimit;
    private final int friendsLimit;

    public CrawlTask(Long uuid, int recursiveLimit, int friendsLimit) {
        this.uuid = uuid;
        this.recursiveLimit = recursiveLimit;
        this.friendsLimit = friendsLimit;
    }

    public Long getUuid() {
        return uuid;
    }

    public int getRecursiveLimit() {
        return recursiveLimit;
    }

    public int getFriendsLimit() {
        return friendsLimit;
    }
}
