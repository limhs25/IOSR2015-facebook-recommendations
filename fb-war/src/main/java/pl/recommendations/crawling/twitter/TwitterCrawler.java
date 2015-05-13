package pl.recommendations.crawling.twitter;

import com.google.common.primitives.Longs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.Crawler;
import twitter4j.HashtagEntity;
import twitter4j.HttpResponseCode;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component(value = "TwitterCrawler")
public class TwitterCrawler implements Crawler {

    private static final Logger logger = LogManager.getLogger(TwitterCrawler.class.getName());

    private static final int FRIENDS_PER_PAGE = 5000;
    private static final int INTERESTS_PER_PAGE = 30;
    public static final int PAGE_LIMIT = 5;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition freeWindow = lock.newCondition();

    private final Twitter twitter = TwitterConfiguration.getTwitterInstance();

    @Override
    public Optional<String> getPersonName(Long uuid) throws InterruptedException {
        lock.lock();
        try {
            User user = twitter.showUser(uuid);
            String name = user.getScreenName();
            logger.info("user[{}] name is {}", uuid, name);
            return Optional.of(name);
        } catch (TwitterException e) {
            if (handledTwitterException(uuid, e)) return getPersonName(uuid);
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<Long> getPersonFriends(Long uuid, int friendsLimit) throws InterruptedException {
        Set<Long> uuids = new HashSet<>();
        long cursor = -1;
        IDs friendsIDs = null;

        do {
            lock.lock();
            try {
                friendsIDs = twitter.getFriendsIDs(uuid, cursor);
                uuids.addAll(Longs.asList(friendsIDs.getIDs()));
                cursor = friendsIDs.getNextCursor();
            } catch (TwitterException e) {
                if (!handledTwitterException(uuid, e)) {
                    return uuids;
                }
            } finally {
                lock.unlock();
            }
        } while (friendsIDs != null && friendsIDs.hasNext() &&
                friendsLimit < (friendsIDs.getNextCursor() + 1) * FRIENDS_PER_PAGE);

        logger.info("Crawled {} friends for user[{}]", uuids.size(), uuid);

        return uuids.stream().limit(friendsLimit).collect(Collectors.toSet());
    }

    @Override
    public Map<String, Long> getPersonInterests(Long uuid, int interestLimit) throws InterruptedException {
        Map<String, Long> interests = new HashMap<>();

        int page = 1;
        do {
            lock.lock();

            try {
                ResponseList<Status> statuses = twitter.getUserTimeline(uuid, new Paging(page, INTERESTS_PER_PAGE));
                Map<String, Long> newInterests = statuses.stream()
                        .flatMap(s -> Stream.of(s.getHashtagEntities()))
                        .map(HashtagEntity::getText)
                        .map(String::toLowerCase)
                        .collect(Collectors.groupingBy(h -> h, Collectors.counting()));

                newInterests.keySet().stream()
                        .filter(interests::containsKey)
                        .forEach(key -> newInterests.put(key, newInterests.get(key) + interests.get(key)));

                interests.putAll(newInterests);
            } catch (TwitterException e) {
                if (!handledTwitterException(uuid, e)) {
                    return interests;
                }
            } finally {
                lock.unlock();
                ++page;
            }
        } while (page < PAGE_LIMIT && interests.size() < interestLimit);

        logger.info("New interests: {}", interests.size());

        return interests.entrySet().stream()
                .limit(interestLimit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean exceededRateLimit(TwitterException e) {
        return e.getErrorCode() == 88;
    }

    private boolean handledTwitterException(Long uuid, TwitterException e) throws InterruptedException {
        if (exceededRateLimit(e)) {
            logger.info(Thread.currentThread().getName() + " waiting for rate limit to be reset");
            freeWindow.await();
            logger.info("Resumed crawling");
            return true;
        } else if (e.getStatusCode() == HttpResponseCode.UNAUTHORIZED ||
                e.getStatusCode() == HttpResponseCode.NOT_FOUND) {
            logger.info("Omitting Twitter crawling for {} due to {}", uuid, e.getMessage());
            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 */15LP * * * *")
    public void resetRateLimit() {
        logger.info("Resetting rate limit");
        lock.lock();
        freeWindow.signalAll();
        lock.unlock();
    }
}
