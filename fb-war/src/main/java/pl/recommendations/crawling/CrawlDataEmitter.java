package pl.recommendations.crawling;

public interface CrawlDataEmitter {
    public void register(CrawledDataListener listener);
    public void unregister(CrawledDataListener listener);
}
