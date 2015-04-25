package pl.recommendations.crawling.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawlerScheduler;

@Component
public class EmbeddedCrawler extends EmbeddedCrawlerEndpoint {
    @Autowired
    private CrawlerScheduler scheduler;
    @Autowired
    protected CrawledDataCache cache;

    public void init() {
        cache.register(this);
    }

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduler.scheduleCrawling(uuid);
    }
}
