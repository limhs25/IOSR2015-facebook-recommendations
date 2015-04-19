package pl.recommendations.crawling;

public interface CrawlerService {
    void scheduleCrawling(Long uuid);

    void scheduleCrawling(Long uuid, int depthLimit, int friendsPerUserLimit);
}
