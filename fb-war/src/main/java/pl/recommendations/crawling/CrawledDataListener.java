package pl.recommendations.crawling;

import java.util.Map;
import java.util.Set;

public interface CrawledDataListener {
    void onNewPerson(Long userId, String name);

    void onNewInterest(String interestName);

    void onAddFriends(Long userId, Set<Long> friends);

    void onAddInterests(Long userId, Map<String, Long> interests);
}
