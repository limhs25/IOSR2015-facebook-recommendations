package pl.recommendations.crawling;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface Crawler {
    static final int DEPTH = 1;

    Optional<String> getPersonName(Long uuid) throws InterruptedException;

    CrawlResult getPersonFriends(Long uuid, long cursor) throws InterruptedException;

    CrawlResult getPersoninterests(Long uuid, long cursor) throws InterruptedException;
}
