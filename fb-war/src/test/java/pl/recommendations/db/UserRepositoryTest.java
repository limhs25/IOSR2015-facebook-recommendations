package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.user.Friendship;
import pl.recommendations.db.user.User;
import pl.recommendations.db.user.UserRepository;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class UserRepositoryTest extends EntityFactory {

    @Autowired
    private UserRepository userRepo;

    private static long uuid = 1l;

    @Test
    public void saveAndGet() {
        User expected = createUser(uuid);
        userRepo.save(expected);

        User actual = userRepo.findByUuid(uuid);
        assertEquals(expected, actual);
    }

    @Test
    public void addFriends() {
        long uuid1 = 1l;
        long uuid2 = 2l;
        long uuid3 = 3l;

        User user1 = createUser(uuid1);
        User user2 = createUser(uuid2);
        User user3 = createUser(uuid3);

        user1.addFriend(user2);
        user1.addFriend(user3);

        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user1);

        User user = userRepo.findByUuid(uuid1);
        Set<Friendship> friendships = user.getFriendships();

        assertEquals(friendships.size(), 2);

        Collection<User> firends = userRepo.getFriendsOf(user1.getId());
        assertEquals(firends.size(), 2);
        assertTrue(firends.contains(user2));
        assertTrue(firends.contains(user3));
    }

    @Test
    public void addInterests() {
        long uuid1 = 1l;
        long uuid2 = 2l;
        long uuid3 = 3l;

        User user1 = createUser(uuid1);
        Interest interest1 = createInterest(uuid2);
        Interest interest2 = createInterest(uuid3);

        user1.addInterest(interest1);
        user1.addInterest(interest2);

        userRepo.save(user1);

        User user = userRepo.findByUuid(uuid1);
        Set<Interest> interests = user.getInterests();

        assertEquals(interests.size(), 2);
        assertTrue(interests.contains(interest1));
        assertTrue(interests.contains(interest2));
    }

}