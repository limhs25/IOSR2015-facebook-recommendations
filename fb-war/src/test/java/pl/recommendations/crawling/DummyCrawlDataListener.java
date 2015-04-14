package pl.recommendations.crawling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyCrawlDataListener implements CrawledDataListener {
    private final Map<Long, Object> users = new HashMap<>();
    private final Map<Long, Object> interests = new HashMap<>();

    private final Map<Long, Set<Long>> userFriends = new HashMap<>();
    private final Map<Long, Set<Long>> userInterests = new HashMap<>();

    @Override
    public void onNewPerson(Long uuid, String name) {
        users.put(uuid, name);
        userFriends.put(uuid, new HashSet<>());
        userInterests.put(uuid, new HashSet<>());
    }

    @Override
    public void onNewInterest(Long uuid, String name) {
        interests.put(uuid, name);
    }

    @Override
    public void onAddFriends(Long uuid, Set<Long> friends) {
        userFriends.get(uuid).addAll(friends);
    }

    @Override
    public void onAddInterests(Long uuid, Set<Long> interests) {
        userInterests.get(uuid).addAll(interests);
    }

    public Map<Long, Object> getUsers() {
        return users;
    }

    public Map<Long, Object> getInterests() {
        return interests;
    }

    public Map<Long, Set<Long>> getUserFriends() {
        return userFriends;
    }

    public Map<Long, Set<Long>> getUserInterests() {
        return userInterests;
    }
}
