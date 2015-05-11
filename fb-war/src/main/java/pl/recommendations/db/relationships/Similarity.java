package pl.recommendations.db.relationships;

import org.springframework.data.neo4j.annotation.RelationshipEntity;
import pl.recommendations.db.RelationshipType;

@RelationshipEntity(type = RelationshipType.SIMILARITY)
public class Similarity extends NodeRelationship {

}
