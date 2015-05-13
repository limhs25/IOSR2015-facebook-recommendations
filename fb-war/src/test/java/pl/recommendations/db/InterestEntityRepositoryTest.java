package pl.recommendations.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.interest.InterestEntityRepository;
import pl.recommendations.db.interest.relationships.Contrast;
import pl.recommendations.db.interest.relationships.Similarity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class InterestEntityRepositoryTest extends EntityFactory {
    public static final String NAME = "name1";
    public static final String NAME1 = "name1";
    public static final String NAME2 = "name2";

    @Autowired
    private InterestEntityRepository interestRepo;
    @Autowired
    Neo4jTemplate template;

    @Test
    public void saveAndGet() {
        InterestEntity expected = createInterest(NAME);
        interestRepo.save(expected);

        InterestEntity actual = interestRepo.findByName(NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void removeAll(){
        interestRepo.save(createInterest("1"));
        interestRepo.save(createInterest("2"));
        interestRepo.save(createInterest("3"));

        interestRepo.deleteAll();

        assertFalse(interestRepo.findAll().iterator().hasNext());
    }

    @Test
    public void saveContrast() {
        double val = 0.5;

        InterestEntity i1 = createInterest(NAME1);
        InterestEntity i2 = createInterest(NAME2);
        i1.addContrast(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        InterestEntity i1FromDb = interestRepo.findByName(NAME1);
        InterestEntity i2FromDb = interestRepo.findByName(NAME2);

        Contrast contrast = interestRepo.getContrastOf(i1.getName(), i2.getName());

        assertEquals(i1FromDb.getContrasts().size(), 1);
        assertEquals(i2FromDb.getContrasts().size(), 1);
        assertEquals(contrast.getValue(), val, Double.MIN_VALUE);
    }

    @Test
    public void saveSimilarity() {
        double val = 0.5;

        InterestEntity i1 = createInterest(NAME1);
        InterestEntity i2 = createInterest(NAME2);
        i1.addSimilarity(i2, val);

        interestRepo.save(i2);
        interestRepo.save(i1);

        InterestEntity i1FromDb = interestRepo.findByName(NAME1);
        InterestEntity i2FromDb = interestRepo.findByName(NAME2);

        Similarity similarity = interestRepo.getSimilarityOf(i1.getName(), i2.getName());

        assertEquals(i1FromDb.getSimilarities().size(), 1);
        assertEquals(i2FromDb.getSimilarities().size(), 1);
        assertEquals(similarity.getValue(), val, Double.MIN_VALUE);
    }

    @Test
    public void supportMoreThanOneConnectionBetweenNodes() {
        double val = 0.5;

        InterestEntity i1 = createInterest(NAME1);
        InterestEntity i2 = createInterest(NAME2);
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