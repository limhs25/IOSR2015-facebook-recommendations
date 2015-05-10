package pl.recommendations.crawling.tasks;

public class SimpleCrawlTask {
    private final Long userId;

    public SimpleCrawlTask(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
