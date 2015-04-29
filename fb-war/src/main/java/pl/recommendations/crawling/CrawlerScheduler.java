package pl.recommendations.crawling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class CrawlerScheduler implements CrawlerService, Runnable {
    private static final Logger logger = LogManager.getLogger(CrawlerScheduler.class.getName());

    private static final int TASK_QUEUE_SIZE = 100000;
    private static final int RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT = 5;
    private static final int PROCESSED_FRIENDS_PER_USER_LIMIT = 1000;
    private static final int PROCESSED_INTERESTS_PER_USER_LIMIT = 30;

    @Autowired
    private CrawledDataCache cache;

    @Autowired
    private Crawler crawler;

    private final BlockingQueue<CrawlTask> tasks = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE);
    private final Thread schedulerThread;

    public CrawlerScheduler() {
        schedulerThread = new Thread(this, "Crawler Scheduler");
        schedulerThread.start();
    }

    public void onShutdown() throws InterruptedException {
        schedulerThread.join();
        while (!tasks.isEmpty()) {
            consumeTask();
        }
    }

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduleCrawling(uuid, RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT, PROCESSED_FRIENDS_PER_USER_LIMIT);
    }

    public void scheduleCrawling(Long uuid, int depthLimit, int friendsPerUserLimit) {
        if (cache.hasPerson(uuid)) {
            logger.debug("Person {} alreaday crawled", uuid);
        } else {
            CrawlTask task = new CrawlTask(uuid, depthLimit, friendsPerUserLimit);
            tasks.add(task);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (tasks.isEmpty()) {
                    Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
                } else {
                    consumeTask();
                }
            } catch (InterruptedException e) {
                logger.info("Interrupted Crawler Scheduler.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void consumeTask() throws InterruptedException {
        CrawlTask task = tasks.poll();

        logger.debug("Consuming new task. Tasks in queue: {}", tasks.size());
        Long uuid = task.getUuid();

        crawlPersonName(uuid);
        crawlInterests(uuid);
        crawlFriends(uuid, task);

        logger.debug("Consumed task. Tasks in queue: {}", tasks.size());

    }

    private void crawlInterests(Long uuid) throws InterruptedException {
        Map<String, Long> interests = crawler.getPersonInterests(uuid, PROCESSED_INTERESTS_PER_USER_LIMIT);

        interests.keySet().stream().forEach(cache::onNewInterest);

        cache.onAddInterests(uuid, interests);
    }

    private void crawlFriends(Long uuid, CrawlTask task) throws InterruptedException {
        int recursiveLimit = task.getRecursiveLimit();
        Set<Long> friendsUuids = crawler.getPersonFriends(uuid, recursiveLimit);

        cache.onAddFriends(uuid, friendsUuids);

        if (recursiveLimit > 0) {
            friendsUuids.forEach(f -> scheduleCrawling(f, recursiveLimit - 1, task.getFriendsLimit()));
        }
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
