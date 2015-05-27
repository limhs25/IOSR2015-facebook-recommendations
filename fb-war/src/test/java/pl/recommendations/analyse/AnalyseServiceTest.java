package pl.recommendations.analyse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.db.person.SuggestionEdge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class AnalyseServiceTest {

    @Autowired
    private AnalyseService analyseService;

    @Autowired
    private PersonNodeRepository personRepo;

    @Autowired
    @Qualifier("JaccardMetric")
    private Metric metric;
    @Test
    public void analyseNode() {

        analyseService.setMetric(metric);
        int size = 5;
        Set<Long> uuid = new HashSet<>();
        PersonNode[] peoples = new PersonNode[size];
        for (int i = 0; i < size; ++i) {

            peoples[i] = new PersonNode();
            peoples[i].setUuid((long) i);
        }
        uuid.add(2l);
        uuid.add(3l);
        uuid.add(4l);

        personRepo.addFriend(peoples[0], peoples[1]);
        personRepo.addFriend(peoples[1], peoples[2]);
        personRepo.addFriend(peoples[1], peoples[3]);
        personRepo.addFriend(peoples[1], peoples[4]);

        personRepo.save(peoples[4]);
        personRepo.save(peoples[3]);
        personRepo.save(peoples[2]);
        personRepo.save(peoples[1]);
        personRepo.save(peoples[0]);

        int suggestionSize = 5;

        analyseService.analyse(0l, suggestionSize);

        Collection<SuggestionEdge> suggestionOf = personRepo.getSuggestionOf(0l);

        assertEquals(3, suggestionOf.size());

        suggestionOf.forEach(i ->
                        assertTrue(uuid.contains(i.getSuggestion().getUuid()))
        );

    }
}

