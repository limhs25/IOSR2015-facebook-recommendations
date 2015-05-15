package pl.recommendations.crawling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recommendations.crawling.tasks.CrawlFriendsTask;
import pl.recommendations.crawling.tasks.SimpleCrawlTask;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


@Service
public class CrawlerScheduler implements CrawlerService {
    private static final Logger logger = LogManager.getLogger(CrawlerScheduler.class.getName());

    private static final int TASK_QUEUE_SIZE = 100000;
    private static final int RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT = 5;
    private static final int PROCESSED_FRIENDS_PER_USER_LIMIT = 1000;
    private static final int PROCESSED_INTERESTS_PER_USER_LIMIT = 45;

    @Autowired
    private CrawledDataCache cache;

    @Autowired
    private Crawler crawler;

    private final BlockingQueue<SimpleCrawlTask> crawlUserQueue = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE);
    private final BlockingQueue<CrawlFriendsTask> crawlFriendsQueue = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE);
    private final BlockingQueue<SimpleCrawlTask> crawlInterestsQueue = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE);

    Runnable userTaskConsumer = () -> run(this::consumeUserTask);
    Runnable friendsTaskConsumer = () -> run(this::consumeFriendsTask);
    Runnable interestTaskConsumer = () -> run(this::consumeInterestTask);

    public CrawlerScheduler() {
        Thread t1 = new Thread(userTaskConsumer, "user task consumer");
        Thread t2 = new Thread(friendsTaskConsumer, "friends task consumer");
        Thread t3 = new Thread(interestTaskConsumer, "interest task consumer");

        t1.start();
        t2.start();
        t3.start();
    }

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduleCrawling(uuid, RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT, PROCESSED_FRIENDS_PER_USER_LIMIT);
    }

    public void scheduleCrawling(Long uuid, int depthLimit, int friendsPerUserLimit) {
        if (cache.hasPerson(uuid) || crawlFriendsQueue.size() == TASK_QUEUE_SIZE) {
            logger.debug("Person {} already crawled", uuid);
        } else {
            CrawlFriendsTask task1 = new CrawlFriendsTask(uuid, depthLimit, friendsPerUserLimit);
            SimpleCrawlTask task2 = new SimpleCrawlTask(uuid);

            crawlUserQueue.add(task2);
            crawlInterestsQueue.add(task2);
            crawlFriendsQueue.add(task1);

//            logger.info("Scheduled");
//            logger.info("crawlUserQueue size: {}", crawlUserQueue.size());
//            logger.info("crawlInterestsQueue size: {}", crawlInterestsQueue.size());
//            logger.info("crawlFriendsQueue size: {}", crawlFriendsQueue.size());
        }
    }

    private void crawlInterests(Long uuid) throws InterruptedException {
        Map<String, Long> interests = crawler.getPersonInterests(uuid, PROCESSED_INTERESTS_PER_USER_LIMIT);

        interests.keySet().stream().forEach(cache::onNewInterest);

        cache.onAddInterests(uuid, interests);
    }

    private void crawlFriends(Long uuid, CrawlFriendsTask task) throws InterruptedException {
        int recursiveLimit = task.getRecursiveLimit();
        int friendsLimit = task.getFriendsLimit();

        Set<Long> friendsUuids = crawler.getPersonFriends(uuid, friendsLimit);

        cache.onAddFriends(uuid, friendsUuids);

        if (recursiveLimit > 0) {
            logger.info("Scheduling new friends: {}", friendsUuids.size());
            friendsUuids.forEach(f -> scheduleCrawling(f, recursiveLimit - 1, friendsLimit));
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

    private void run(Procedure consumeTask) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                consumeTask.apply();
            } catch (InterruptedException e) {
                logger.info("Interrupted Crawler Scheduler.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void consumeUserTask() throws InterruptedException {
        if (crawlUserQueue.isEmpty()) {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
        } else {
            SimpleCrawlTask task = crawlUserQueue.poll();
            logger.info("crawlUserQueue size: {}", crawlUserQueue.size());
            Long uuid = task.getUserId();
            crawlPersonName(uuid);
        }
    }

    private void consumeFriendsTask() throws InterruptedException {
        if (crawlUserQueue.isEmpty()) {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
        } else {
            CrawlFriendsTask task = crawlFriendsQueue.poll();
            logger.info("crawlFriendsQueue size: {}", crawlFriendsQueue.size());
            Long uuid = task.getUserId();
            crawlFriends(uuid, task);
        }
    }

    private void consumeInterestTask() throws InterruptedException {
        if (crawlInterestsQueue.isEmpty()) {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
        } else {
            SimpleCrawlTask task = crawlInterestsQueue.poll();
            logger.info("crawlInterestsQueue size: {}", crawlInterestsQueue.size());
            Long uuid = task.getUserId();
            crawlInterests(uuid);
        }
    }

}
