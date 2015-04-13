package pl.recommendations.db.interest;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.relationships.Contrast;
import pl.recommendations.db.interest.relationships.Similarity;


public interface InterestRepository extends GraphRepository<Interest> {
    Interest findByUuid(Long uuid);

    @Query("start i1 = node({0}), i2 = node({1}) \n" +
            "match i1-[s:" + RelationshipType.SIMILARITY + "]->i2 \n" +
            "return s")
    Similarity getSimilarityOf(Long i1, Long i2);

    @Query("start i1 = node({0}), i2 = node({1}) \n" +
            "match i1-[s:" + RelationshipType.CONTRAST + "]->i2 \n" +
            "return s")
    Contrast getContrastOf(Long id, Long id1);
}
