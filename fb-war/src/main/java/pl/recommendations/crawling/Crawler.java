package pl.recommendations.crawling;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public interface Crawler {
    Optional<String> getPersonName(Long uuid) throws InterruptedException;

    Set<Long> getPersonFriends(Long uuid, int friendsLimit) throws InterruptedException;

    //TODO Long -> (String, Int) ?
    Map<String, Long> getPersonInterests(Long uuid, int interestLimit) throws InterruptedException;
}
