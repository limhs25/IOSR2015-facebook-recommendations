package pl.recommendations.crawling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CrawlerScheduler implements CrawlerService {
    private static final Logger logger = LogManager.getLogger(CrawlerScheduler.class.getName());
    public static final int TASK_QUEUE_SIZE = 1000;
    private static long friendLimit = 20;
    private static long interestLimit = 20;

    @Autowired
    private CrawledDataCache cache;

    @Autowired
    Crawler crawler;

    private final BlockingQueue<CrawlTask> tasks = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE);

    @Override
    public void scheduleCrawling(Long uuid) {
        if (cache.hasPerson(uuid)) {
            logger.info("Person {} alreaday crawled", uuid);
        } else {
            CrawlTask task = createNewTask(uuid);
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

            CrawlLimit friendsLimit = crawlFriends(uuid, task);
            CrawlLimit interestsLimit = crawlInterests(uuid, task);

            if (friendsLimit.getCount() > 0 || interestsLimit.getCount() > 0) {
                task = new CrawlTask(uuid, friendsLimit, interestsLimit);
                tasks.put(task);
            }
            logger.debug("Consumed task. Tasks in queue: {}", tasks.size());
        } catch (InterruptedException e) {
            logger.warn("Interrupted while consuming task due to: {}", e.getMessage(), e);
        }
    }

    private CrawlLimit crawlInterests(Long uuid, CrawlTask task) throws InterruptedException {
        CrawlLimit limit = task.getInterestLimit();
        long count = limit.getCount();
        long cursor = limit.getCursor();

        if (count > 0 && cursor != 0) {
            CrawlResult result = crawler.getPersoninterests(uuid, cursor);

            Set<Long> interests = result.getUuids();
            cache.onAddFriends(uuid, interests);

            limit = new CrawlLimit(--count, result.getNextCursor());
        }
        return limit;
    }

    private CrawlLimit crawlFriends(Long uuid, CrawlTask task) throws InterruptedException {
        CrawlLimit limit = task.getFriendLimit();
        long count = limit.getCount();
        long cursor = limit.getCursor();

        if (count > 0 && cursor != 0) {
            CrawlResult result = crawler.getPersonFriends(uuid, cursor);
            Set<Long> friends = result.getUuids();
            cache.onAddInterests(uuid, friends);

            friends.forEach(this::scheduleCrawling);

            limit = new CrawlLimit(--count, result.getNextCursor());
        }

        return limit;
    }

    private CrawlTask createNewTask(Long id) {
        return new CrawlTask(id, new CrawlLimit(friendLimit), new CrawlLimit(interestLimit));
    }

    private void crawlPersonName(Long uuid) throws InterruptedException {
        if (!cache.hasPerson(uuid)) {
            Optional<String> name = crawler.getPersonName(uuid);
            if (name.isPresent()) {
                cache.onNewPerson(uuid, name.get());
            }
        }
    }

    public static void setInterestLimit(long interestLimit) {
        CrawlerScheduler.interestLimit = interestLimit;
    }

    public static void setFriendLimit(long friendLimit) {
        CrawlerScheduler.friendLimit = friendLimit;
    }
}
