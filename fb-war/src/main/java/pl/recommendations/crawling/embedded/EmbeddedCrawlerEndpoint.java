package pl.recommendations.crawling.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawlerEndpoint;
import pl.recommendations.crawling.CrawlerScheduler;

@Component
public abstract class EmbeddedCrawlerEndpoint implements CrawlerEndpoint {
    @Autowired
    private CrawlerScheduler scheduler;
    @Autowired
    protected CrawledDataCache cache;

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduler.scheduleCrawling(uuid);

    }
}
