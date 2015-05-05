package pl.recommendations.crawling;

public interface CrawlerEndpoint extends CrawlerService, CrawledDataListener {

    void init();
}
