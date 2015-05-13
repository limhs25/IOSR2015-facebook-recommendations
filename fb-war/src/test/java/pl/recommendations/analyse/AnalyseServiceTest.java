package pl.recommendations.analyse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.db.person.SuggestionEdge;

import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class AnalyseServiceTest {

//    @Autowired
//    private PersonNodeRepository personRepo;
    @Autowired
//    @Qualifier("analyseServiceImpl")
    protected AnalyseService analyseService;

    @Autowired
    protected PersonNodeRepository peopleRepo;
//    @Before
//    public void before(){
//        analyseService = new AnalyseServiceImpl();
//
//    }


    @Test
    public void analyseNode(){

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");
//        AnalyseService analyseService = new AnalyseServiceImpl();
//                context.getBean(AnalyseService.class);
//        analyseScheduler.scheduleForAnalyse(1l);
        peopleRepo.getFriendsOf(1l);


        Long uuid =17765013l;
        analyseService.analyse(uuid);
        PersonNode tmp = peopleRepo.findByUuid(uuid);
        Set<SuggestionEdge> tmp1 = tmp.getSuggestionEdges();
        assertEquals(5,tmp1.size());

    }
}
