package pl.recommendations.db.interest.relationships;

import org.springframework.data.neo4j.annotation.RelationshipEntity;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.InterestRelationship;


@RelationshipEntity(type = RelationshipType.CONTRAST)
public class Contrast extends InterestRelationship {

}
