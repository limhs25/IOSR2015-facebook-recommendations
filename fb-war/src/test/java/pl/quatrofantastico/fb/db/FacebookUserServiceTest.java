package pl.quatrofantastico.fb.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.quatrofantastico.fb.db.repositories.FacebookUserRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class FacebookUserServiceTest {

    @Autowired
    private FacebookUserRepository userRepo;

    @Test
    @Transactional
    public void getByName()  {
        String name = "user";
        FacebookUser expected = new FacebookUser();
        expected.setName(name);

        userRepo.save(expected);

        FacebookUser actual = userRepo.findByName(name);
        assertEquals(expected, actual);
    }

}