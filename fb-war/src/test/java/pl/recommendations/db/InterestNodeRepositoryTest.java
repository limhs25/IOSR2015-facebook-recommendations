package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestNode;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.relationships.Contrast;
import pl.recommendations.db.relationships.Similarity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class InterestNodeRepositoryTest extends EntityFactory {
    public static final String NAME = "name1";
    public static final String NAME1 = "name1";
    public static final String NAME2 = "name2";

    @Autowired
    private InterestNodeRepository interestRepo;
    @Autowired
    Neo4jTemplate template;

    @Test
    public void saveAndGet() {
        InterestNode expected = createInterestNode(NAME);
        interestRepo.save(expected);

        InterestNode actual = interestRepo.findByName(NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void saveContrast() {
        double val = 0.5;

        InterestNode i1 = createInterestNode(NAME1);
        InterestNode i2 = createInterestNode(NAME2);
        i1.addContrast(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        InterestNode i1FromDb = interestRepo.findByName(NAME1);
        InterestNode i2FromDb = interestRepo.findByName(NAME2);

        Contrast contrast = interestRepo.getContrastOf(i1.getName(), i2.getName());

        assertEquals(i1FromDb.getContrasts().size(), 1);
        assertEquals(i2FromDb.getContrasts().size(), 1);
        assertEquals(contrast.getValue(), val, Double.MIN_VALUE);
    }

    @Test
    public void saveSimilarity() {
        double val = 0.5;

        InterestNode i1 = createInterestNode(NAME1);
        InterestNode i2 = createInterestNode(NAME2);
        i1.addSimilarity(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        InterestNode i1FromDb = interestRepo.findByName(NAME1);
        InterestNode i2FromDb = interestRepo.findByName(NAME2);

        Similarity similarity = interestRepo.getSimilarityOf(i1.getName(), i2.getName());

        assertEquals(i1FromDb.getSimilarities().size(), 1);
        assertEquals(i2FromDb.getSimilarities().size(), 1);
        assertEquals(similarity.getValue(), val, Double.MIN_VALUE);
    }

    @Test
    public void supportMoreThanOneConnectionBetweenNodes() {
        double val = 0.5;

        InterestNode i1 = createInterestNode(NAME1);
        InterestNode i2 = createInterestNode(NAME2);
        i1.addSimilarity(i2, val);
        i1.addContrast(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        Similarity similarity = interestRepo.getSimilarityOf(i1.getName(), i2.getName());
        Contrast contrast = interestRepo.getContrastOf(i1.getName(), i2.getName());

        assertNotNull(similarity);
        assertEquals(similarity.getValue(), val, Double.MIN_VALUE);
        assertNotNull(contrast);
        assertEquals(contrast.getValue(), val, Double.MIN_VALUE);
    }
}