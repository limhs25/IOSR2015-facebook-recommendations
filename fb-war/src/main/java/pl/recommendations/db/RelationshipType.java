package pl.recommendations.db;

import org.springframework.data.neo4j.annotation.RelationshipEntity;

@RelationshipEntity
public class RelationshipType {
    public static final String SIMILARITY = "SIMILARITY";
    public static final String CONTRAST = "CONTRAST";
    public static final String FRIENDSHIP = "FRIENDSHIP";
    public static final String INTEREST = "INTEREST";
}
