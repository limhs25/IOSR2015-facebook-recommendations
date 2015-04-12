package pl.recommendations.crawling;

import java.util.Set;

public interface CrawledDataListener {
    void onNewPerson(Long uuid, String name);

    void onNewInterest(Long uuid, String name);

    void onAddFriends(Long uuid, Set<Long> friends);

    void onAddInterests(Long uuid, Set<Long> interests);
}
