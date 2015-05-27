package pl.recommendations.crawling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.queue.PersistentQueueFacade;
import pl.recommendations.db.queue.core.QueueNode;
import pl.recommendations.db.queue.exceptions.EmptyQueueException;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("crawlerScheduler")
public class CrawlerScheduler implements CrawlerService {
    private static final Logger logger = LogManager.getLogger(CrawlerScheduler.class);

    private static final int TASK_QUEUE_SIZE = 100000;
    private static final int RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT = 5;
    private static final int PROCESSED_FRIENDS_PER_USER_LIMIT = 1000;
    private static final int PROCESSED_INTERESTS_PER_USER_LIMIT = 45;

    @Autowired
    private CrawledDataCache cache;

    @Autowired
    private Crawler crawler;

    @Autowired
    private PersistentQueueFacade queue;

    @Autowired(required = false)
    private TaskExecutor taskExecutor;

    private final Runnable userTaskConsumer = () -> run(this::consumeUserTask);
    private final Runnable friendsTaskConsumer = () -> run(this::consumeFriendsTask);
    private final Runnable interestTaskConsumer = () -> run(this::consumeInterestTask);

    @PostConstruct
    private void init() {
        if (taskExecutor != null) {
            taskExecutor.execute(userTaskConsumer);
            taskExecutor.execute(friendsTaskConsumer);
            taskExecutor.execute(interestTaskConsumer);
        }
    }

    @Override
    public void scheduleCrawling(Long uuid, boolean highPriority) {
        scheduleCrawling(uuid, RECURSIVE_FRIENDS_CRAWL_DEPTH_LIMIT, PROCESSED_FRIENDS_PER_USER_LIMIT, highPriority);
    }

    public void scheduleCrawling(Long uuid, int depthLimit, int friendsPerUserLimit, boolean highPriority) {
        if (cache.hasPerson(uuid) || queue.getFriendsQueueSize() == TASK_QUEUE_SIZE) {
            logger.debug("Person {} already crawled", uuid);
        } else {
            queue.enqueueUser(uuid, highPriority);
            queue.enqueueInterest(uuid, highPriority);
            queue.enqueueFriend(uuid, depthLimit, friendsPerUserLimit, highPriority);

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

    private void crawlFriends(Long uuid, QueueNode node) throws InterruptedException {
        int recursiveLimit = node.getRecursiveLimit();
        int friendsLimit = node.getFriendsLimit();

        Set<Long> friendsUuids = crawler.getPersonFriends(uuid, friendsLimit);

        cache.onAddFriends(uuid, friendsUuids);

        if (recursiveLimit > 0) {
            logger.info("Scheduling new friends: {}", friendsUuids.size());
            friendsUuids.forEach(f -> scheduleCrawling(f, recursiveLimit - 1, friendsLimit, false));
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
        QueueNode user = null;
        do {
            try {
                user = queue.dequeueUser();
            } catch (EmptyQueueException e) {
                Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
            }
        } while (user == null);

        logger.info("crawlUserQueue size: {}", queue.getUserQueueSize());
        Long uuid = user.getUserId();
        crawlPersonName(uuid);
    }

    private void consumeFriendsTask() throws InterruptedException {
        QueueNode friend = null;
        do {
            try {
                friend = queue.dequeueFriend();
            } catch (EmptyQueueException e) {
                Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
            }
        } while (friend == null);

        logger.info("crawlFriendsQueue size: {}", queue.getFriendsQueueSize());
        Long uuid = friend.getUserId();
        crawlFriends(uuid, friend);
    }

    private void consumeInterestTask() throws InterruptedException {
        QueueNode interest = null;
        do {
            try {
                interest = queue.dequeueInterest();
            } catch (EmptyQueueException e) {
                Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
            }
        } while (interest == null);

        logger.info("crawlInterestsQueue size: {}", queue.getInterestsQueueSize());
        Long uuid = interest.getUserId();
        crawlInterests(uuid);
    }

}
