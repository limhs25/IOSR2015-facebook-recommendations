package pl.recommendations.crawling;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
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

        assertNotNull(peopleRepo.findByUuid(UUID));
    }

    @Test
    public void addExistingPerson() {
        peopleRepo.save(createPerson(UUID));
        endpoint.onNewPerson(UUID, "");

        assertNotNull(peopleRepo.findByUuid(UUID));
    }

    @Test
    public void addNewInterest() {
        endpoint.onNewInterest(UUID, "");

        assertNotNull(interestsRepo.findByUuid(UUID));
    }

    @Test
    public void addExistingInterest() {
        interestsRepo.save(createInterest(UUID));
        endpoint.onNewInterest(UUID, "");

        assertNotNull(interestsRepo.findByUuid(UUID));
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
    public void addNewFriendsNotInDb() {
        peopleRepo.save(createPerson(UUID));
        endpoint.onAddFriends(UUID, ImmutableSet.of(2l, 3l, 4l));

        Collection<Person> friends = peopleRepo.getFriendsOf(UUID);
        assertNotNull(friends);
        assertEquals(0, friends.size());
    }

    @Test
    public void addNewFriends() {
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
    public void addNewInterestsNotInDb() {
        peopleRepo.save(createPerson(UUID));
        endpoint.onAddInterests(UUID, ImmutableSet.of(2l, 3l, 4l));

        Person person = peopleRepo.findByUuid(UUID);
        assertNotNull(person);
        assertEquals(0, person.getInterests().size());
    }

    @Test
    public void addNewInterests() {
        peopleRepo.save(createPerson(UUID));
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
    }

    private Person createPerson(long l) {
        Person person = new Person();
        person.setUuid(l);
        return person;
    }
}
