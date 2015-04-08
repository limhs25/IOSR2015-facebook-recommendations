package pl.quatrofantastico.fb.db.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.quatrofantastico.fb.db.FacebookentityFactory;
import pl.quatrofantastico.fb.db.model.FacebookContent;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class FacebookContentRepositoryTest extends FacebookentityFactory {

    @Autowired
    private FacebookContentRepository userRepo;

    @Test

    public void saveAndGet()  {
        String name = "user";

        FacebookContent expected = createFacebookContent(name);
        userRepo.save(expected);

        FacebookContent actual = userRepo.findByName(name);
        assertEquals(expected, actual);
    }



}