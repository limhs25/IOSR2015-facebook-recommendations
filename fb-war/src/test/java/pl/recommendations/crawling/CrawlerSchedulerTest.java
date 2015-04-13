package pl.recommendations.crawling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CrawlerSpringConfiguration.class)
public class CrawlerSchedulerTest {
    @Autowired
    private CrawlerScheduler scheduler;
    @Autowired
    private CrawlerCache cache;

    public void test() throws InterruptedException {
        long uuid = 17765013l;
        scheduler.scheduleCrawling(uuid);

        while (!cache.hasPerson(uuid)) {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));
        }


    }
}
