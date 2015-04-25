package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.interest.InterestRepository;
import pl.recommendations.db.person.Friendship;
import pl.recommendations.db.person.Interest;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class PersonRepositoryTest extends EntityFactory {

    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private InterestRepository interestRepo;

    @Test
    public void saveAndGet() {
        long uuid = 1l;
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

        String name1 = "name1";
        String name2 = "name2";

        Map<String, Long> weights = new HashMap<>();
        weights.put(name1, 5l);
        weights.put(name2, 3l);

        Person person1 = createUser(uuid1);
        InterestEntity interestEntity1 = createInterest(name1);
        InterestEntity interestEntity2 = createInterest(name2);
        person1.addInterest(interestEntity1, weights.get(name1));
        person1.addInterest(interestEntity2, weights.get(name2));

        interestRepo.save(interestEntity1);
        interestRepo.save(interestEntity2);
        personRepo.save(person1);

        Person person = personRepo.findByUuid(uuid1);
        Set<Interest> interestEntities = person.getInterests();

        assertEquals(interestEntities.size(), 2);
        interestEntities.forEach(i ->
                        assertEquals(weights.get(i.getInterest().getName()), i.getWeight())
        );
    }

}