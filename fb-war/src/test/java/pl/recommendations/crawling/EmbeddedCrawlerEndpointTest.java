package pl.recommendations.crawling;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestNode;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class EmbeddedCrawlerEndpointTest {
    public static final long UUID = 1l;
    private static final String NAME = "name";
    @Autowired
    @Qualifier("embeddedCrawlerService")
    private CrawlerEndpoint endpoint;

    @Autowired
    InterestNodeRepository interestsRepo;
    @Autowired
    PersonNodeRepository peopleRepo;

    @Test
    public void addNewPersonNode() {
        endpoint.onNewPersonNode(UUID, "");

        assertNotNull(peopleRepo.findByUuid(UUID));
    }

    @Test
    public void addExistingPersonNode() {
        peopleRepo.save(createPersonNode(UUID));
        endpoint.onNewPersonNode(UUID, "");

        assertNotNull(peopleRepo.findByUuid(UUID));
    }

    @Test
    public void addNewInterestNode() {
        endpoint.onNewInterest(NAME);

        assertNotNull(interestsRepo.findByName(NAME));
    }

    @Test
    public void addExistingInterestNode() {
        interestsRepo.save(createInterestNode(NAME));
        endpoint.onNewInterest(NAME);

        assertNotNull(interestsRepo.findByName(NAME));
    }

    @Test
    public void addSelfAsFriend() {
        peopleRepo.save(createPersonNode(UUID));
        endpoint.onAddFriends(UUID, ImmutableSet.of(UUID));

        Collection<PersonNode> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
    public void addNewFriendsNotInDb() {
        peopleRepo.save(createPersonNode(UUID));
        endpoint.onAddFriends(UUID, ImmutableSet.of(2l, 3l, 4l));

        Collection<PersonNode> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
    public void addNewFriends() {
        ImmutableSet<Long> friendsUuids = ImmutableSet.of(2l, 3l, 4l);
        peopleRepo.save(createPersonNode(UUID));
        friendsUuids.stream()
                .map(this::createPersonNode)
                .forEach(peopleRepo::save);
        endpoint.onAddFriends(UUID, friendsUuids);

        Collection<PersonNode> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(friendsUuids.size(), friends.size());
    }

    @Test
    public void addNewInterestNodesNotInDb() {
        peopleRepo.save(createPersonNode(UUID));
        endpoint.onAddInterests(UUID, ImmutableMap.of("name1", 1l, "name2", 2l, "name3", 3l));

        PersonNode person = peopleRepo.findByUuid(UUID);
        assertNotNull(person);
        assertEquals(0, person.getInterestEdges().size());
    }

    @Test
    public void addNewInterestNodes() {
        peopleRepo.save(createPersonNode(UUID));
        Map<String, Long> interestsUuids = ImmutableMap.of("name1", 1l, "name2", 2l, "name3", 3l);
        interestsUuids.keySet().stream()
                .map(this::createInterestNode)
                .forEach(interestsRepo::save);

        endpoint.onAddInterests(UUID, interestsUuids);

        PersonNode person = peopleRepo.findByUuid(UUID);
        assertNotNull(person);
        assertEquals(interestsUuids.size(), person.getInterestEdges().size());
    }

    private InterestNode createInterestNode(String name) {
        InterestNode interest = new InterestNode();
        interest.setName(name);
        return interest;
    }

    private PersonNode createPersonNode(long l) {
        PersonNode person = new PersonNode();
        person.setUuid(l);
        return person;
    }
}
