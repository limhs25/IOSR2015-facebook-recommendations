package pl.recommendations.crawling;

<<<<<<< HEAD
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.crawling.embedded.EmbeddedCrawlerEndpoint;
import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.interest.InterestRepository;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.util.Collection;
=======
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.interest.InterestEntityRepository;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.util.Collection;
import java.util.Map;
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
<<<<<<< HEAD
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class EmbeddedCrawlerEndpointTest {
    public static final long UUID = 1l;
    @Autowired
    EmbeddedCrawlerEndpoint endpoint;
    @Autowired
    InterestRepository interestsRepo;
    @Autowired
    PersonRepository peopleRepo;

    @Test
    public void addNewPerson() {
        endpoint.onNewPerson(UUID, "");
=======
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class EmbeddedCrawlerEndpointTest {

    private static final long UUID = 1l;
    private static final String NAME = "name";

    @Autowired
    @Qualifier("embeddedCrawlerService")
    private CrawlerEndpoint endpoint;

    @Autowired
    private InterestEntityRepository interestsRepo;

    @Autowired
    private PersonRepository peopleRepo;

    @Before
    public void before() {
        endpoint.init();
    }

    @Test
    public void addNewPerson() {
        endpoint.onNewPerson(UUID, NAME);
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647

        assertNotNull(peopleRepo.findByUuid(UUID));
    }

    @Test
    public void addExistingPerson() {
        peopleRepo.save(createPerson(UUID));
<<<<<<< HEAD
        endpoint.onNewPerson(UUID, "");
=======
        endpoint.onNewPerson(UUID, NAME);
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647

        assertNotNull(peopleRepo.findByUuid(UUID));
    }

    @Test
    public void addNewInterest() {
<<<<<<< HEAD
        endpoint.onNewInterest(UUID, "");

        assertNotNull(interestsRepo.findByUuid(UUID));
=======
        endpoint.onNewInterest(NAME);

        assertNotNull(interestsRepo.findByName(NAME));
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
    }

    @Test
    public void addExistingInterest() {
<<<<<<< HEAD
        interestsRepo.save(createInterest(UUID));
        endpoint.onNewInterest(UUID, "");

        assertNotNull(interestsRepo.findByUuid(UUID));
=======
        interestsRepo.save(createInterest(NAME));
        endpoint.onNewInterest(NAME);

        assertNotNull(interestsRepo.findByName(NAME));
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
    }

    @Test
    public void addSelfAsFriend() {
        peopleRepo.save(createPerson(UUID));
        endpoint.onAddFriends(UUID, ImmutableSet.of(UUID));

        Collection<Person> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
<<<<<<< HEAD
    public void addNewFriendsNotInDb() {
=======
    public void addNewFriendsWhenNoneExistsInDb() {
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
        peopleRepo.save(createPerson(UUID));
        endpoint.onAddFriends(UUID, ImmutableSet.of(2l, 3l, 4l));

        Collection<Person> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
<<<<<<< HEAD
    public void addNewFriends() {
=======
    public void addNewFriends() throws SystemException, NotSupportedException {
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
        ImmutableSet<Long> friendsUuids = ImmutableSet.of(2l, 3l, 4l);
        peopleRepo.save(createPerson(UUID));
        friendsUuids.stream()
                .map(this::createPerson)
                .forEach(peopleRepo::save);
        endpoint.onAddFriends(UUID, friendsUuids);

        Collection<Person> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(friendsUuids.size(), friends.size());
    }

    @Test
<<<<<<< HEAD
    public void addNewInterestsNotInDb() {
        peopleRepo.save(createPerson(UUID));
        endpoint.onAddInterests(UUID, ImmutableSet.of(2l, 3l, 4l));
=======
    public void addNewInterestsWhenNoneExistsInDb() {
        peopleRepo.save(createPerson(UUID));
        endpoint.onAddInterests(UUID, ImmutableMap.of("name1", 1l, "name2", 2l, "name3", 3l));
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647

        Person person = peopleRepo.findByUuid(UUID);
        assertNotNull(person);
        assertEquals(0, person.getInterests().size());
    }

    @Test
    public void addNewInterests() {
        peopleRepo.save(createPerson(UUID));
<<<<<<< HEAD
        ImmutableSet<Long> interestsUuids = ImmutableSet.of(2l, 3l, 4l);
        interestsUuids.stream()
                .map(this::createInterest)
                .forEach(interestsRepo::save);
        endpoint.onAddInterests(UUID, interestsUuids);

        Person person = peopleRepo.findByUuid(UUID);
        assertNotNull(person);
        assertEquals(interestsUuids.size(), person.getInterests().size());
    }

    private Interest createInterest(long l) {
        Interest interest = new Interest();
        interest.setUuid(l);
        return interest;
=======
        Map<String, Long> interests = ImmutableMap.of("name1", 1l, "name2", 2l, "name3", 3l);

        interests.keySet().stream()
                .map(this::createInterest)
                .forEach(interestsRepo::save);

        endpoint.onAddInterests(UUID, interests);

        Person person = peopleRepo.findByUuid(UUID);
        assertNotNull(person);
        assertEquals(interests.size(), person.getInterests().size());
    }

    private InterestEntity createInterest(String name) {
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setName(name);
        return interestEntity;
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
    }

    private Person createPerson(long l) {
        Person person = new Person();
        person.setUuid(l);
        return person;
    }
}
