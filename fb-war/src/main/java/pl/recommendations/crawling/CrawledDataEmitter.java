package pl.recommendations.crawling;

public interface CrawledDataEmitter {
    public void register(CrawledDataListener listener);

    public void unregister(CrawledDataListener listener);
}
