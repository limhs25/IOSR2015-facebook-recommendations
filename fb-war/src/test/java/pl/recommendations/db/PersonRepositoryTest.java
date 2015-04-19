package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.person.Friendship;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class PersonRepositoryTest extends EntityFactory {

    @Autowired
    private PersonRepository personRepo;

    private static long uuid = 1l;

    @Test
    public void saveAndGet() {
        Person expected = createUser(uuid);
        personRepo.save(expected);

        Person actual = personRepo.findByUuid(uuid);
        assertEquals(expected, actual);
    }

    @Test
    public void addFriends() {
        long uuid1 = 1l;
        long uuid2 = 2l;
        long uuid3 = 3l;

        Person person1 = createUser(uuid1);
        Person person2 = createUser(uuid2);
        Person person3 = createUser(uuid3);

        person1.addFriend(person2);
        person1.addFriend(person3);

        personRepo.save(person2);
        personRepo.save(person3);
        personRepo.save(person1);

        Person person = personRepo.findByUuid(uuid1);
        Set<Friendship> friendships = person.getFriendships();

        assertEquals(friendships.size(), 2);

        Collection<Person> firends = personRepo.getFriendsOf(person1.getUuid());
        assertEquals(firends.size(), 2);
        assertTrue(firends.contains(person2));
        assertTrue(firends.contains(person3));
    }

    @Test
    public void addInterests() {
        long uuid1 = 1l;
        long uuid2 = 2l;
        long uuid3 = 3l;

        Person person1 = createUser(uuid1);
        Interest interest1 = createInterest(uuid2);
        Interest interest2 = createInterest(uuid3);

        person1.addInterest(interest1);
        person1.addInterest(interest2);

        personRepo.save(person1);

        Person person = personRepo.findByUuid(uuid1);
        Set<Interest> interests = person.getInterests();

        assertEquals(interests.size(), 2);
        assertTrue(interests.contains(interest1));
        assertTrue(interests.contains(interest2));
    }

}