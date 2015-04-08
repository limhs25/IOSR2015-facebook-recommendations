package pl.quatrofantastico.fb.db.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.quatrofantastico.fb.db.model.FacebookUser;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class FacebookUserServiceTest {

    @Autowired
    private FacebookUserRepository userRepo;

    @Test

    public void saveAndGet()  {
        String name = "user";

        FacebookUser expected = createFacebookUser(name);
        userRepo.save(expected);

        FacebookUser actual = userRepo.findByName(name);
        assertEquals(expected, actual);
    }

    private FacebookUser createFacebookUser(String name){
        FacebookUser user = new FacebookUser();
        user.setName(name);

        return user;
    }

}