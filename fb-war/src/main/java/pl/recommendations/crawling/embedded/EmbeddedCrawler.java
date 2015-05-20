package pl.recommendations.crawling.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawlerScheduler;

import javax.annotation.PostConstruct;

@Transactional
@Service("embeddedCrawlerService")
public class EmbeddedCrawler extends EmbeddedCrawlerEndpoint {

    @Autowired
    private CrawlerScheduler scheduler;

    @Autowired
    protected CrawledDataCache cache;

    @Override
    @PostConstruct
    public void init() {
        cache.register(this);
    }

    @Override
    public void scheduleCrawling(Long uuid, boolean highPriority) {
        scheduler.scheduleCrawling(uuid, highPriority);
    }
}
