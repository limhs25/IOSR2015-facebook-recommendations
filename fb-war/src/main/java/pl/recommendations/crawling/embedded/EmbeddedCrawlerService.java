package pl.recommendations.crawling.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.*;

@Component
public abstract class EmbeddedCrawlerService implements CrawlerService, CrawledDataListener, CrawledDataStorage {
    @Autowired
    private CrawlerScheduler scheduler;
    @Autowired
    protected CrawlerCache cache;

    @Override
    public Object getPersonName(Long uuid) {
        return cache.getPersonName(uuid);
    }

    @Override
    public Object getInterestName(Long uuid) {
        return cache.getInterestName(uuid);
    }

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduler.scheduleCrawling(uuid);

    }
}
