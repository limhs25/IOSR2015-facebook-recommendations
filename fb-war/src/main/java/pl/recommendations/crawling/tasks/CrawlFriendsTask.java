package pl.recommendations.crawling.tasks;

public class CrawlFriendsTask {
    private final Long userId;
    private final int recursiveLimit;
    private final int friendsLimit;

    public CrawlFriendsTask(Long userId, int recursiveLimit, int friendsLimit) {
        this.userId = userId;
        this.recursiveLimit = recursiveLimit;
        this.friendsLimit = friendsLimit;
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
}
