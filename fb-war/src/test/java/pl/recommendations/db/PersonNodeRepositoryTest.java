package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestNode;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.person.InterestEdge;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class PersonNodeRepositoryTest extends EntityFactory {

    @Autowired
    private PersonNodeRepository personRepo;
    @Autowired
    private InterestNodeRepository interestRepo;

    @Test
    public void saveAndGet() {
        long uuid = 1l;
        PersonNode expected = createUser(uuid);
        personRepo.save(expected);

        PersonNode actual = personRepo.findByUuid(uuid);
        assertEquals(expected, actual);
    }

    @Test
    public void addFriends() {
        long uuid1 = 1l;
        long uuid2 = 2l;
        long uuid3 = 3l;

        PersonNode person1 = createUser(uuid1);
        PersonNode person2 = createUser(uuid2);
        PersonNode person3 = createUser(uuid3);

        personRepo.addFriend(person1, person2);
        personRepo.addFriend(person1, person3);

        personRepo.save(person2);
        personRepo.save(person3);
        personRepo.save(person1);

        Set<PersonNode> friendships = new HashSet<>(personRepo.getFriendsOf(uuid1));
        assertEquals(friendships.size(), 2);

        Collection<PersonNode> firends = personRepo.getFriendsOf(person1.getUuid());
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

        PersonNode person1 = createUser(uuid1);
        InterestNode interestEntity1 = createInterestNode(name1);
        InterestNode interestEntity2 = createInterestNode(name2);
        personRepo.addInterest(person1, interestEntity1, weights.get(name1));
        personRepo.addInterest(person1, interestEntity2, weights.get(name2));

        interestRepo.save(interestEntity1);
        interestRepo.save(interestEntity2);
        personRepo.save(person1);

        PersonNode person = personRepo.findByUuid(uuid1);
        Set<InterestEdge> interestEntities = person.getInterestEdges();

        assertEquals(interestEntities.size(), 2);
    }

}
