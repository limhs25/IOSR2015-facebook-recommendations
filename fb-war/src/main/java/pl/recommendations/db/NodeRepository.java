package pl.recommendations.db;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.relationships.Contrast;
import pl.recommendations.db.relationships.Similarity;

@Transactional
public interface NodeRepository extends GraphRepository<Node> {

    @Query("match i1-[s:" + RelationshipType.SIMILARITY + "]->i2 \n" +
            "where i1.name = {0} and i2.name = {1} \n" +
            "return s")
    Similarity getSimilarityOf(String name1, String name2);

    @Query("match i1-[s:" + RelationshipType.CONTRAST + "]->i2 \n" +
            "where i1.name = {0} and i2.name = {1} \n" +
            "return s")
    Contrast getContrastOf(String name1, String name2);

}
