package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.interest.InterestRepository;
import pl.recommendations.db.interest.relationships.Contrast;
import pl.recommendations.db.interest.relationships.Similarity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Transactional
public class InterestRepositoryTest extends FacebookentityFactory {

    @Autowired
    private InterestRepository interestRepo;

    @Autowired
    Neo4jTemplate template;

    @Test
    public void saveAndGet() {
        String name = "user";

        Interest expected = createInterest(name);
        interestRepo.save(expected);

        Interest actual = interestRepo.findByName(name);
        assertEquals(expected, actual);
    }

    @Test
    public void saveContrast() {
        double val = 0.5;
        Interest i1 = createInterest("name1");
        Interest i2 = createInterest("name2");
        i1.addContrast(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        Interest i1FromDb = interestRepo.findByName("name1");
        Interest i2FromDb = interestRepo.findByName("name2");

        Contrast contrast = interestRepo.getContrastOf(i1.getId(), i2.getId());

        assertEquals(i1FromDb.getContrasts().size(), 1);
        assertEquals(i2FromDb.getContrasts().size(), 1);
        assertEquals(contrast.getValue(), val, Double.MIN_VALUE);
    }

    @Test
    public void saveSimilarity() {
        double val = 0.5;
        Interest i1 = createInterest("name1");
        Interest i2 = createInterest("name2");
        i1.addSimilarity(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        Interest i1FromDb = interestRepo.findByName("name1");
        Interest i2FromDb = interestRepo.findByName("name2");

        Similarity similarity = interestRepo.getSimilarityOf(i1.getId(), i2.getId());

        assertEquals(i1FromDb.getSimilarities().size(), 1);
        assertEquals(i2FromDb.getSimilarities().size(), 1);
        assertEquals(similarity.getValue(), val, Double.MIN_VALUE);
    }

    @Test
    public void supportMoreThanOneConnectionBetweenNodes() {
        double val = 0.5;
        Interest i1 = createInterest("name1");
        Interest i2 = createInterest("name2");
        i1.addSimilarity(i2, val);
        i1.addContrast(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        Similarity similarity = interestRepo.getSimilarityOf(i1.getId(), i2.getId());
        Contrast contrast = interestRepo.getContrastOf(i1.getId(), i2.getId());

        assertNotNull(similarity);
        assertEquals(similarity.getValue(), val, Double.MIN_VALUE);
        assertNotNull(contrast);
        assertEquals(contrast.getValue(), val, Double.MIN_VALUE);
    }
}