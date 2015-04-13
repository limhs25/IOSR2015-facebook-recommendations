package pl.recommendations.crawling.twitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawlResult;
import pl.recommendations.crawling.Crawler;
import twitter4j.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@Component(value = "TwitterCrawler")
public class TwitterCrawler implements Crawler {
    private static final Logger logger = LogManager.getLogger(TwitterCrawler.class.getName());
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition rateLimitExceeded = lock.newCondition();

    private final Twitter twitter = TwitterConfiguration.getTwitterInstance();

    @Override
    public Optional<String> getPersonName(Long uuid) throws InterruptedException {
        try {
            User user = twitter.showUser(uuid);
            String name = user.getScreenName();
//            logger.debug("user[{}] name is {}", uuid, name);
            return Optional.of(name);
        } catch (TwitterException e) {
            if (handledTwitterException(uuid, e)) return getPersonName(uuid);
            return Optional.empty();
        }
    }

    @Override
    public CrawlResult getPersonFriends(Long uuid, long cursor) throws InterruptedException {
        try {
            IDs friendsIDs = twitter.getFriendsIDs(uuid, cursor);
            Set<Long> uuids = Arrays.stream(friendsIDs.getIDs()).boxed().collect(Collectors.toSet());
//            logger.debug("Crawled {} friends for user[{}]", uuids.size(), uuids);
            return new CrawlResult(uuids, friendsIDs.getNextCursor());
        } catch (TwitterException e) {
            if (handledTwitterException(uuid, e)) return getPersonFriends(uuid, cursor);
            return CrawlResult.empty();
        }
    }

    @Override
    public CrawlResult getPersoninterests(Long uuid, long cursor) throws InterruptedException {
        try {
            //TODO crawl interests
            throw new TwitterException("Dummy exception");
        } catch (TwitterException e) {
            if (handledTwitterException(uuid, e)) return getPersonFriends(uuid, cursor);
            return CrawlResult.empty();
        }
    }

    private boolean exceededRateLimit(TwitterException e) {
        return e.getErrorCode() == 88;
    }

    private void waitForNewWindow() throws InterruptedException {
        logger.debug("Waiting for rate limit to be reset");
        rateLimitExceeded.await();
    }

    private boolean handledTwitterException(Long uuid, TwitterException e) throws InterruptedException {
        if (exceededRateLimit(e)) {
            logger.warn("Exceeded rate limit for crawling interests.");
            waitForNewWindow();
            return true;
        } else if (e.getStatusCode() == HttpResponseCode.UNAUTHORIZED ||
                e.getStatusCode() == HttpResponseCode.NOT_FOUND) {
            logger.debug("Omitting interests crawling for {} due to {}", uuid, e.getMessage());
        }
        return false;
    }

    @Scheduled(cron = "15 * * * * *")
    public void resetRateLimit(){
        logger.debug("Reseting rate limit");
        rateLimitExceeded.signalAll();
    }

   /* @Override
    public Set<Long> getNewFriends(Long uuid, Set<Long> longs) throws CrawlerException, InterruptedException {
        return addFriends(null,  DEPTH);
    }

    @Override
    public Set<Interest> getInterests(Long uuid) throws InterruptedException {
        try {
            ResponseList<Status> timeline = twitter.getUserTimeline(uuid);
            return ImmutableSet.of();
        } catch (TwitterException e) {
            if (exceededRateLimit(e)) {
                logger.warn("Exceeded rate limit for crawling interests.");
                waitForNewWindow();
                return getInterests(uuid);
            } else if (e.getStatusCode() == HttpResponseCode.UNAUTHORIZED ||
                    e.getStatusCode() == HttpResponseCode.NOT_FOUND) {
                logger.debug("Omitting interests crawling for {} due to {}", uuid, e.getMessage());
            }

            return ImmutableSet.of();
        }
    }


    private Set<Long> addFriends(User user, int depth) throws CrawlerException, InterruptedException {
        try {
            ArrayList<Long> uuids = new ArrayList<>();

            long cursor = -1;
            do {
                PagableResponseList<twitter4j.User> friendsList = twitter.getFriendsList(user.getUuid(), cursor);
                for (twitter4j.User friend : friendsList) {

                }
                cursor = friendsList.getNextCursor();
            } while (cursor != 0);

            logger.info("{} friend count: {}", user.getUuid(), uuids.size());

            for (long uuid : uuids) {
//                User friend = scheduleCrawling(uuid, depth - 1);
//                user.addFriend(friend);
            }
        } catch (TwitterException e) {
            if (exceededRateLimit(e)) {
                logger.warn("Exceeded rate limit for crawling friends.");
                waitForNewWindow();
                addFriends(user, depth);
            } else if (e.getStatusCode() == HttpResponseCode.UNAUTHORIZED ||
                    e.getStatusCode() == HttpResponseCode.NOT_FOUND) {
                logger.debug("Omitting friends crawling for due to {}", user.getUuid(), e.getMessage());
            }
        }
        return null;
    }*/
}
