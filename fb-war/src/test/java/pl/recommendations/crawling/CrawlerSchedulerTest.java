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

    @Test
    public void test() throws InterruptedException {
        long uuid = 17765013l;
        scheduler.scheduleCrawling(uuid);

        while (true) {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(1000, TimeUnit.SECONDS));
        }

//        System.out.println(cache.getPersonName(uuid));
    }
}
