package pl.recommendations.db.relationships;

import org.springframework.data.neo4j.annotation.RelationshipEntity;
import pl.recommendations.db.RelationshipType;


@RelationshipEntity(type = RelationshipType.CONTRAST)
public class Contrast extends NodeRelationship {

}
