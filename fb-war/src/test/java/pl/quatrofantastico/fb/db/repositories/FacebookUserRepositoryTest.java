package pl.quatrofantastico.fb.db.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.quatrofantastico.fb.db.FacebookentityFactory;
import pl.quatrofantastico.fb.db.model.nodes.FacebookContent;
import pl.quatrofantastico.fb.db.model.nodes.FacebookUser;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class FacebookUserRepositoryTest extends FacebookentityFactory {

    @Autowired
    private FacebookUserRepository userRepo;

    private static String name = "user";

    @Test
    public void saveAndGet() {
        FacebookUser expected = createFacebookUser(name);
        userRepo.save(expected);

        FacebookUser actual = userRepo.findByName(name);
        assertEquals(expected, actual);
    }

    @Test
    public void addFriends() {
        String name1 = name + "1";
        String name2 = name + "2";
        String name3 = name + "3";

        FacebookUser user1 = createFacebookUser(name1);
        FacebookUser user2 = createFacebookUser(name2);
        FacebookUser user3 = createFacebookUser(name3);

        user1.addFriend(user2);
        user1.addFriend(user3);

        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user1);

        FacebookUser user = userRepo.findByName(name1);
        Set<FacebookUser> friends = user.getFriends();

        assertEquals(friends.size(), 2);
        assertTrue(friends.contains(user2));
        assertTrue(friends.contains(user3));
    }

    @Test
    public void addInterests() {
        String name1 = name + "1";
        String name2 = name + "2";
        String name3 = name + "3";

        FacebookUser user1 = createFacebookUser(name1);
        FacebookContent interest1 = createFacebookContent(name2);
        FacebookContent interest2 = createFacebookContent(name3);

        user1.addInterest(interest1);
        user1.addInterest(interest2);

        userRepo.save(user1);

        FacebookUser user = userRepo.findByName(name1);
        Set<FacebookContent> interests = user.getInterests();

        assertEquals(interests.size(), 2);
        assertTrue(interests.contains(interest1));
        assertTrue(interests.contains(interest2));
    }

}