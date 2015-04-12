package pl.recommendations.crawling;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CrawlerCache implements CrawledDataListener, CrawlDataEmitter , CrawledDataStorage{
    private final Set<CrawledDataListener> listeners = Collections.newSetFromMap(new IdentityHashMap<>());

    private final Map<Long, String> users = new HashMap<>();
    private final Map<Long, String> interests = new HashMap<>();
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();
    private final Map<Long, Set<Long>> userInterests = new HashMap<>();

    /**
     * friend -> userIds
     * Invariant: userIds are always contained by <code>users</code>
     */
    private final Map<Long, Set<Long>> awaitingFriends = new HashMap<>();
    /**
     * interest -> userIds
     * Invariant: userIds are always contained by <code>users</code>
     */
    private final Map<Long, Set<Long>> awaitingInterests = new HashMap<>();

    @Override
    public void onNewPerson(Long uuid, String name) {
        users.put(uuid, name);
        userFriends.put(uuid, new HashSet<>());
        userInterests.put(uuid, new HashSet<>());

        listeners.forEach(l -> l.onNewPerson(uuid, name));

        awaitingFriends
                .getOrDefault(uuid, Collections.emptySet())
                .forEach(user -> addFriendAndNotify(user, uuid));
        awaitingFriends.remove(uuid);
    }

    @Override
    public void onNewInterest(Long uuid, String name) {
        interests.put(uuid, name);

        listeners.forEach(l -> l.onNewInterest(uuid, name));

        awaitingInterests
                .getOrDefault(uuid, Collections.emptySet())
                .forEach(user -> addInterestAndNotify(user, uuid));
        awaitingInterests.remove(uuid);
    }

    @Override
    public void onAddFriends(Long uuid, Set<Long> friends) {
        Set<Long> crawledFriends = Sets.intersection(friends, users.keySet());
        Set<Long> notCrawled = Sets.difference(friends, crawledFriends);

        getOrUpdate(userFriends, uuid).addAll(crawledFriends);
        listeners.forEach(l -> l.onAddFriends(uuid, crawledFriends));

        notCrawled.forEach(friendUuid -> getOrUpdate(awaitingFriends, friendUuid).add(uuid));
    }

    @Override
    public void onAddInterests(Long uuid, Set<Long> newInterests) {
        Set<Long> crawledInterests = Sets.intersection(newInterests, interests.keySet());
        Set<Long> notCrawled = Sets.difference(newInterests, crawledInterests);

        getOrUpdate(userInterests, uuid).addAll(crawledInterests);
        listeners.forEach(l -> l.onAddInterests(uuid, crawledInterests));

        notCrawled.forEach(friendUuid -> getOrUpdate(awaitingInterests, friendUuid).add(uuid));
    }

    private void addFriendAndNotify(Long user, Long friendUuid) {
        userFriends.get(user).add(friendUuid);
        listeners.forEach(l -> l.onAddFriends(user, ImmutableSet.of(friendUuid)));
    }

    private void addInterestAndNotify(Long user, Long interestUuid) {
        userInterests.get(user).add(interestUuid);
        listeners.forEach(l -> l.onAddInterests(user, ImmutableSet.of(interestUuid)));
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
    public Object getPersonName(Long uuid) {
        return users.get(uuid);
    }

    @Override
    public Object getInterestName(Long uuid) {
        return interests.get(uuid);
    }

    @Override
    public boolean hasPerson(Long uuid) {
        return users.containsKey(uuid);
    }

    @Override
    public boolean hasInterest(Long uuid) {
        return interests.containsKey(uuid);
    }

    private Set<Long> getOrUpdate(Map<Long, Set<Long>> map, Long uuid) {
        if (!map.containsKey(uuid))
            map.put(uuid, new HashSet<>());
        return map.get(uuid);
    }

    Set<CrawledDataListener> getListeners() {
        return listeners;
    }

    Map<Long, Object> getUsers() {
        return ImmutableMap.copyOf(users);
    }

    Map<Long, Object> getInterests() {
        return ImmutableMap.copyOf(interests);
    }

    Map<Long, Set<Long>> getUserFriends() {
        return userFriends;
    }

    Map<Long, Set<Long>> getUserInterests() {
        return userInterests;
    }

    Map<Long, Set<Long>> getAwaitingFriends() {
        return awaitingFriends;
    }

    Map<Long, Set<Long>> getAwaitingInterests() {
        return awaitingInterests;
    }
}
