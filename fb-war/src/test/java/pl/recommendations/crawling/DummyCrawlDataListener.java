package pl.recommendations.crawling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyCrawlDataListener implements CrawledDataListener {
    private final Map<Long, Object> users = new HashMap<>();
    private final Set<String> interests = new HashSet<>();

    private final Map<Long, Set<Long>> userFriends = new HashMap<>();
    private final Map<Long, Map<String, Long>> userInterests = new HashMap<>();

    @Override
    public void onNewPerson(Long userId, String name) {
        users.put(userId, name);
        userFriends.put(userId, new HashSet<>());
        userInterests.put(userId, new HashMap<>());
    }

    @Override
    public void onNewInterest(String interestName) {
        interests.add(interestName);
    }

    @Override
    public void onAddFriends(Long userId, Set<Long> friends) {
        userFriends.get(userId).addAll(friends);
    }

    @Override
    public void onAddInterests(Long userId, Map<String, Long> interests) {
        userInterests.get(userId).putAll(interests);
    }

    public Map<Long, Object> getUsers() {
        return users;
    }

    public Set<String> getInterests() {
        return interests;
    }

    public Map<Long, Set<Long>> getUserFriends() {
        return userFriends;
    }

    public Map<Long, Map<String, Long>> getUserInterests() {
        return userInterests;
    }
}
