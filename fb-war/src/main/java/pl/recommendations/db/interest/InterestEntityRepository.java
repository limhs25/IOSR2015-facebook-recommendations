package pl.recommendations.db.interest;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.relationships.Contrast;
import pl.recommendations.db.interest.relationships.Similarity;


public interface InterestEntityRepository extends GraphRepository<InterestEntity> {
    InterestEntity findByName(String interestName);

    @Query("match i1-[s:" + RelationshipType.SIMILARITY + "]->i2 \n" +
            "where i1.name = {0} and i2.name = {1} \n" +
            "return s")
    Similarity getSimilarityOf(String name1, String name2);

    @Query("match i1-[s:" + RelationshipType.CONTRAST + "]->i2 \n" +
            "where i1.name = {0} and i2.name = {1} \n" +
            "return s")
    Contrast getContrastOf(String name1, String name2);
}
