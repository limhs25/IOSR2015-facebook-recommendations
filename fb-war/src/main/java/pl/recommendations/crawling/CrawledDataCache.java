package pl.recommendations.crawling;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CrawledDataCache implements CrawledDataListener, CrawledDataEmitter, CrawledDataStorage {
    private final Logger logger = LogManager.getLogger(CrawledDataCache.class.getName());

    private final Set<CrawledDataListener> listeners = Collections.newSetFromMap(new IdentityHashMap<>());

    private final Map<Long, String> users = new HashMap<>();
    private final Set<String> interests = new HashSet<>();
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();
    private final Map<Long, Map<String, Long>> userInterests = new HashMap<>();

    /**
     * friend -> userIds
     * Invariant: userIds are always contained by <code>users</code>
     */
    private final Map<Long, Set<Long>> awaitingFriends = new HashMap<>();

    @Override
    public void onNewPerson(Long userId, String name) {
        users.put(userId, name);
        if (!userInterests.containsKey(userId)) userInterests.put(userId, new HashMap<>());
        if (!userFriends.containsKey(userId)) userFriends.put(userId, new HashSet<>());

        listeners.forEach(l -> l.onNewPerson(userId, name));

        awaitingFriends
                .getOrDefault(userId, Collections.emptySet())
                .forEach(user -> addFriendAndNotify(user, userId));

        Map<String, Long> interests = userInterests.get(userId);
        if (interests.size() > 0) {
            listeners.forEach(l -> l.onAddInterests(userId, interests));
        }

        awaitingFriends.remove(userId);
    }

    @Override
    public void onNewInterest(String interestName) {
        interests.add(interestName);

        listeners.forEach(l -> l.onNewInterest(interestName));
    }

    @Override
    public void onAddFriends(Long userId, Set<Long> friends) {
        Set<Long> crawledFriends = Sets.intersection(friends, users.keySet());
        Set<Long> notCrawled = Sets.difference(friends, crawledFriends);

        getOrUpdate(userFriends, userId).addAll(crawledFriends);
        listeners.forEach(l -> l.onAddFriends(userId, crawledFriends));

        notCrawled.forEach(friendUuid -> getOrUpdate(awaitingFriends, friendUuid).add(userId));
    }

    @Override
    public void onAddInterests(Long userId, Map<String, Long> newInterests) {
        if (!userInterests.containsKey(userId)) userInterests.put(userId, new HashMap<>());

        userInterests.get(userId).putAll(newInterests);

        if (users.containsKey(userId)) {
            listeners.forEach(l -> l.onAddInterests(userId, newInterests));
        }
    }

    private void addFriendAndNotify(Long userId, Long friendUuid) {
        if (!userFriends.containsKey(userId)) userFriends.put(userId, new HashSet<>());

        userFriends.get(userId).add(friendUuid);

        listeners.forEach(l -> l.onAddFriends(userId, ImmutableSet.of(friendUuid)));
    }

    @Override
    public void register(CrawledDataListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregister(CrawledDataListener listener) {
        listeners.remove(listener);
    }

    @Override
    public String getPersonName(Long uuid) {
        return users.get(uuid);
    }

    @Override
    public boolean hasPerson(Long uuid) {
        return users.containsKey(uuid);
    }

    @Override
    public boolean hasInterest(Long uuid) {
        return interests.contains(uuid);
    }

    private Set<Long> getOrUpdate(Map<Long, Set<Long>> map, Long uuid) {
        if (!map.containsKey(uuid))
            map.put(uuid, new HashSet<>());
        return map.get(uuid);
    }

    Set<CrawledDataListener> getListeners() {
        return ImmutableSet.copyOf(listeners);
    }

    Map<Long, String> getUsers() {
        return ImmutableMap.copyOf(users);
    }

    Set<String> getInterests() {
        return ImmutableSet.copyOf(interests);
    }

    Map<Long, Set<Long>> getUserFriends() {
        return ImmutableMap.copyOf(userFriends);
    }

    Map<Long, Map<String, Long>> getUserInterests() {
        return ImmutableMap.copyOf(userInterests);
    }

    Map<Long, Set<Long>> getAwaitingFriends() {
        return ImmutableMap.copyOf(awaitingFriends);
    }

}
