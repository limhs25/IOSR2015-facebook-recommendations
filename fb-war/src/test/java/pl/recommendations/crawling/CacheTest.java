package pl.recommendations.crawling;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CacheTest {
    private CrawlerCache cache;
    private DummyCrawlDataListener listener;

    @Before
    public void before() {
        cache = new CrawlerCache();
        listener = new DummyCrawlDataListener();
        cache.register(listener);
    }

    @Test
    public void unregisterListener() {
        cache.unregister(listener);
        assertEquals(0, cache.getListeners().size());
    }

    @Test
    public void addSameListenerTwice() {
        cache.register(listener);
        assertEquals(1, cache.getListeners().size());
    }

    @Test
    public void addTwoDifferentListeners() {
        cache.register(new DummyCrawlDataListener());
        assertEquals(2, cache.getListeners().size());
    }

    @Test
    public void addUser() {
        long uuid = 1l;
        String name = "name";
        cache.onNewPerson(uuid, name);
        assertEquals(1, cache.getUsers().size());
        assertEquals(1, listener.getUsers().size());
        assertEquals(name, cache.getUsers().get(uuid));
        assertEquals(name, listener.getUsers().get(uuid));
    }

    @Test
    public void addSameUserTwice() {
        long uuid = 1l;
        String name = "name";
        cache.onNewPerson(uuid, name);
        cache.onNewPerson(uuid, name);
        assertEquals(1, cache.getUsers().size());
        assertEquals(1, listener.getUsers().size());
        assertEquals(name, cache.getUsers().get(uuid));
        assertEquals(name, listener.getUsers().get(uuid));
    }

    @Test
    public void addInterest() {
        long uuid = 1l;
        String name = "name";
        cache.onNewInterest(uuid, name);
        assertEquals(1, cache.getInterests().size());
        assertEquals(1, listener.getInterests().size());
        assertEquals(name, cache.getInterests().get(uuid));
        assertEquals(name, listener.getInterests().get(uuid));
    }

    @Test
    public void addSameInterestTwice() {
        long uuid = 1l;
        String name = "name";
        cache.onNewInterest(uuid, name);
        cache.onNewInterest(uuid, name);
        assertEquals(1, cache.getInterests().size());
        assertEquals(1, listener.getInterests().size());
        assertEquals(name, cache.getInterests().get(uuid));
        assertEquals(name, listener.getInterests().get(uuid));
    }

    @Test
    public void addExistingFriends() {
        long uuid = 1l;
        String name = "name";
        Set<Long> friends = ImmutableSet.of(2l, 3l, 4l, 5l);

        cache.onNewPerson(uuid, name);
        friends.forEach(id -> cache.onNewPerson(id, id.toString()));

        cache.onAddFriends(uuid, friends);

        assertNotNull(cache.getUserFriends().get(uuid));
        assertEquals(friends.size(), cache.getUserFriends().get(uuid).size());
        assertEquals(0, cache.getAwaitingFriends().size());

        assertNotNull(listener.getUserFriends().get(uuid));
        assertEquals(friends.size(), listener.getUserFriends().get(uuid).size());
    }

    @Test
    public void addUnexistingFriends() {
        long uuid = 1l;
        String name = "name";
        Set<Long> friends = ImmutableSet.of(2l, 3l, 4l, 5l);

        cache.onNewPerson(uuid, name);
        cache.onAddFriends(uuid, friends);

        assertNotNull(cache.getUserFriends().get(uuid));
        assertEquals(0, cache.getUserFriends().get(uuid).size());
        assertEquals(friends.size(), cache.getAwaitingFriends().size());

        assertNotNull(listener.getUserFriends().get(uuid));
        assertEquals(0, listener.getUserFriends().get(uuid).size());
    }

    @Test
    public void addBothExistingAndUnexistingFriends() {
        long uuid = 1l;
        String name = "name";
        Set<Long> crawledFriends = ImmutableSet.of(2l, 3l);
        Set<Long> notCrawledFriends = ImmutableSet.of(4l, 5l);

        cache.onNewPerson(uuid, name);
        crawledFriends.forEach(id -> cache.onNewPerson(id, id.toString()));

        cache.onAddFriends(uuid, Sets.union(crawledFriends, notCrawledFriends));

        assertNotNull(cache.getUserFriends().get(uuid));
        assertEquals(crawledFriends.size(), cache.getUserFriends().get(uuid).size());
        assertEquals(notCrawledFriends.size(), cache.getAwaitingFriends().size());

        assertNotNull(listener.getUserFriends().get(uuid));
        assertEquals(crawledFriends.size(), listener.getUserFriends().get(uuid).size());
    }

    @Test
    public void addExistingInterests() {
        long uuid = 1l;
        String name = "name";
        Set<Long> interests = ImmutableSet.of(2l, 3l, 4l, 5l);

        cache.onNewPerson(uuid, name);
        interests.forEach(id -> cache.onNewInterest(id, id.toString()));

        cache.onAddInterests(uuid, interests);

        assertNotNull(cache.getUserInterests().get(uuid));
        assertEquals(interests.size(), cache.getUserInterests().get(uuid).size());
        assertEquals(0, cache.getAwaitingInterests().size());

        assertNotNull(listener.getUserInterests().get(uuid));
        assertEquals(interests.size(), listener.getUserInterests().get(uuid).size());
    }

    @Test
    public void addUnexistingInterests() {
        long uuid = 1l;
        String name = "name";
        Set<Long> interests = ImmutableSet.of(2l, 3l, 4l, 5l);

        cache.onNewPerson(uuid, name);
        cache.onAddInterests(uuid, interests);

        assertNotNull(cache.getUserInterests().get(uuid));
        assertEquals(0, cache.getUserInterests().get(uuid).size());
        assertEquals(interests.size(), cache.getAwaitingInterests().size());

        assertNotNull(listener.getUserInterests().get(uuid));
        assertEquals(0, listener.getUserInterests().get(uuid).size());
    }

    @Test
    public void addBothExistingAndUnexistingInterests() {
        long uuid = 1l;
        String name = "name";
        Set<Long> crawledInterests = ImmutableSet.of(2l, 3l);
        Set<Long> notCrawledInterests = ImmutableSet.of(4l, 5l);

        cache.onNewPerson(uuid, name);
        crawledInterests.forEach(id -> cache.onNewInterest(id, id.toString()));

        cache.onAddInterests(uuid, Sets.union(crawledInterests, notCrawledInterests));

        assertNotNull(cache.getUserInterests().get(uuid));
        assertEquals(crawledInterests.size(), cache.getUserInterests().get(uuid).size());
        assertEquals(notCrawledInterests.size(), cache.getAwaitingInterests().size());

        assertNotNull(listener.getUserInterests().get(uuid));
        assertEquals(crawledInterests.size(), listener.getUserInterests().get(uuid).size());
    }

    @Test
    public void addAwaitingFriend() {
        long uuid = 1l;
        long awaitingFriendUuid = 2l;
        String name = "name";
        Set<Long> friends = ImmutableSet.of(awaitingFriendUuid, 3l, 4l, 5l);

        cache.onNewPerson(uuid, name);
        cache.onAddFriends(uuid, friends);

        cache.onNewPerson(awaitingFriendUuid, name);

        assertNotNull(cache.getUserFriends().get(uuid));
        assertEquals(1, cache.getUserFriends().get(uuid).size());
        assertEquals(friends.size() - 1, cache.getAwaitingFriends().size());

        assertNotNull(listener.getUserFriends().get(uuid));
        assertEquals(1, listener.getUserFriends().get(uuid).size());
    }

    @Test
    public void addAwaitingInterest() {
        long uuid = 1l;
        long awaitingInterestUuid = 2l;
        String name = "name";
        Set<Long> interests = ImmutableSet.of(awaitingInterestUuid, 3l, 4l, 5l);

        cache.onNewPerson(uuid, name);
        cache.onAddInterests(uuid, interests);

        cache.onNewInterest(awaitingInterestUuid, name);

        assertNotNull(cache.getUserInterests().get(uuid));
        assertEquals(1, cache.getUserInterests().get(uuid).size());
        assertEquals(interests.size() - 1, cache.getAwaitingInterests().size());

        assertNotNull(listener.getUserInterests().get(uuid));
        assertEquals(1, listener.getUserInterests().get(uuid).size());
    }


}
