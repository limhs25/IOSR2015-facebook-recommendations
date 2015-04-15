package pl.recommendations.crawling;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public interface Crawler {
    static final int DEPTH = 1;

    Optional<String> getPersonName(Long uuid) throws InterruptedException;

    Set<Long> getPersonFriends(Long uuid, int maximumNumberOfFriends) throws InterruptedException;

    Map<Long, String> getPersonInterests(Long uuid, int maximumNumberOfInterests) throws InterruptedException;
}
