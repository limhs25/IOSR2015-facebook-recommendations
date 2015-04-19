package pl.recommendations.crawling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CrawlerScheduler implements CrawlerService {
    private static final Logger logger = LogManager.getLogger(CrawlerScheduler.class.getName());
    public static final int TASK_QUEUE_SIZE = 100000;
    private static final int RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT = 3;
    private static final int PROCESSED_FRIENDS_PER_USER_LIMIT = 1000;
    private static final int PROCESSED_INTERESTS_PER_USER_LIMIT = 20;

    @Autowired
    private CrawledDataCache cache;

    @Autowired
    private Crawler crawler;

    private final BlockingQueue<CrawlTask> tasks = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE);

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduleCrawling(uuid, RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT, PROCESSED_FRIENDS_PER_USER_LIMIT);
    }

    @Override
    public void scheduleCrawling(Long uuid, int depthLimit, int friendsPerUserLimit) {
        if (cache.hasPerson(uuid)) {
            logger.info("Person {} alreaday crawled", uuid);
        } else {
            CrawlTask task = new CrawlTask(uuid, depthLimit, friendsPerUserLimit);
            tasks.add(task);
            logger.debug("Scheduled new task");
        }
    }

    @Scheduled(fixedDelay = 100l)
    public void consumeTask() {
        if (tasks.isEmpty()) {
            return;
        }

        try {
            CrawlTask task = tasks.poll();

            logger.debug("Consuming new task. Tasks in queue: {}");
            Long uuid = task.getUuid();

            crawlPersonName(uuid);

            crawlInterests(uuid);

            Set<Long> friendsUuids = crawlFriends(uuid, task);

            int recursiveDepthLimit = task.getRecursiveLimit();

            if (recursiveDepthLimit > 1) {
                friendsUuids.forEach(f -> scheduleCrawling(f, recursiveDepthLimit - 1, task.getFriendsLimit()));
            }
            logger.debug("Consumed task. Tasks in queue: {}", tasks.size());
        } catch (InterruptedException e) {
            logger.warn("Interrupted while consuming task due to: {}", e.getMessage(), e);
        }
    }

    private void crawlInterests(Long uuid) throws InterruptedException {
        Map<Long, String> interests = crawler.getPersonInterests(uuid, PROCESSED_INTERESTS_PER_USER_LIMIT);

        for (Map.Entry<Long, String> interest : interests.entrySet()) {
            cache.onNewInterest(interest.getKey(), interest.getValue());
        }
        cache.onAddInterests(uuid, interests.keySet());
    }

    private Set<Long> crawlFriends(Long uuid, CrawlTask task) throws InterruptedException {
        Set<Long> friendsUuids = crawler.getPersonFriends(uuid, task.getRecursiveLimit());

        cache.onAddFriends(uuid, friendsUuids);

        return friendsUuids;
    }

    private void crawlPersonName(Long uuid) throws InterruptedException {
        if (!cache.hasPerson(uuid)) {
            Optional<String> name = crawler.getPersonName(uuid);
            if (name.isPresent()) {
                cache.onNewPerson(uuid, name.get());
            }
        }
    }

}
