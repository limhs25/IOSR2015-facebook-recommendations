package pl.recommendations.crawling;

public class CrawlTask {
    private final Long uuid;
    private final CrawlLimit friendLimit;
    private final CrawlLimit interestLimit;

    public CrawlTask(Long uuid, CrawlLimit friendLimit, CrawlLimit interestLimit) {
        this.uuid = uuid;
        this.friendLimit = friendLimit;
        this.interestLimit = interestLimit;
    }

    public Long getUuid() {
        return uuid;
    }

    public CrawlLimit getFriendLimit() {
        return friendLimit;
    }

    public CrawlLimit getInterestLimit() {
        return interestLimit;
    }
}
