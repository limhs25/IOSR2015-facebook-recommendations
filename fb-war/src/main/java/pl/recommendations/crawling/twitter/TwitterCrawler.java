package pl.recommendations.crawling.twitter;

import com.google.common.primitives.Longs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.Crawler;
import pl.recommendations.util.CollectionUtils;
import twitter4j.*;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


@Component(value = "TwitterCrawler")
public class TwitterCrawler implements Crawler {

    private static final Logger logger = LogManager.getLogger(TwitterCrawler.class.getName());

    private static final int FRIENDS_PER_PAGE = 5000;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition freeWindow = lock.newCondition();

    private final Twitter twitter = TwitterConfiguration.getTwitterInstance();

    @Override
    public Optional<String> getPersonName(Long uuid) throws InterruptedException {
        lock.lock();
        try {
            User user = twitter.showUser(uuid);
            String name = user.getScreenName();
            logger.debug("user[{}] name is {}", uuid, name);
            return Optional.of(name);
        } catch (TwitterException e) {
            if (handledTwitterException(uuid, e)) return getPersonName(uuid);
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<Long> getPersonFriends(Long uuid, int maximumNumberOfFriends) throws InterruptedException {
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
            logger.debug("Crawled {} friends for user[{}]", uuids.size(), uuids);
        }while (friendsIDs != null && friendsIDs.hasNext() &&
                maximumNumberOfFriends < (friendsIDs.getNextCursor() + 1) * FRIENDS_PER_PAGE);

        CollectionUtils.trimCollection(uuids, FRIENDS_PER_PAGE);

        return uuids;
    }

    @Override
    public Map<Long, String> getPersonInterests(Long uuid, int maximumNumberOfIterests) throws InterruptedException {
        Map<Long, String> interests = new HashMap<>();
        lock.lock();
        try {

            //TODO crawl interests, see getPersonFriends
            throw new TwitterException("Dummy exception");
        } catch (TwitterException e) {
            if (handledTwitterException(uuid, e)) {
            }
            CollectionUtils.trimMap(interests, maximumNumberOfIterests);
            return interests;
        } finally {
            lock.unlock();
        }
    }

    private boolean exceededRateLimit(TwitterException e) {
        return e.getErrorCode() == 88;
    }

    private boolean handledTwitterException(Long uuid, TwitterException e) throws InterruptedException {
        if (exceededRateLimit(e)) {
            logger.info("Waiting for rate limit to be reset");
            freeWindow.await();
            logger.info("Resumed crawling");
            return true;
        } else if (e.getStatusCode() == HttpResponseCode.UNAUTHORIZED ||
                e.getStatusCode() == HttpResponseCode.NOT_FOUND) {
            logger.debug("Omitting Twitter crawling for {} due to {}", uuid, e.getMessage());
            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void resetRateLimit() {
        logger.info("Resetting rate limit");
        lock.lock();
        freeWindow.signalAll();
        lock.unlock();
    }
}
