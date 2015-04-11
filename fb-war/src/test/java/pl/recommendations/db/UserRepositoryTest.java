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
public class UserRepositoryTest extends FacebookentityFactory {

    @Autowired
    private UserRepository userRepo;

    private static String name = "user";

    @Test
    public void saveAndGet() {
        User expected = createUser(name);
        userRepo.save(expected);

        User actual = userRepo.findByName(name);
        assertEquals(expected, actual);
    }

    @Test
    public void addFriends() {
        String name1 = name + "1";
        String name2 = name + "2";
        String name3 = name + "3";

        User user1 = createUser(name1);
        User user2 = createUser(name2);
        User user3 = createUser(name3);

        user1.addFriend(user2);
        user1.addFriend(user3);

        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user1);

        User user = userRepo.findByName(name1);
        Set<Friendship> friendships = user.getFriendships();

        assertEquals(friendships.size(), 2);

        Collection<User> firends = userRepo.getFriendsOf(user1.getId());
        assertEquals(firends.size(), 2);
        assertTrue(firends.contains(user2));
        assertTrue(firends.contains(user3));
    }

    @Test
    public void addInterests() {
        String name1 = name + "1";
        String name2 = name + "2";
        String name3 = name + "3";

        User user1 = createUser(name1);
        Interest interest1 = createInterest(name2);
        Interest interest2 = createInterest(name3);

        user1.addInterest(interest1);
        user1.addInterest(interest2);

        userRepo.save(user1);

        User user = userRepo.findByName(name1);
        Set<Interest> interests = user.getInterests();

        assertEquals(interests.size(), 2);
        assertTrue(interests.contains(interest1));
        assertTrue(interests.contains(interest2));
    }

}