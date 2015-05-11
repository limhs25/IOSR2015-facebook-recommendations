package pl.recommendations.db;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

@RelationshipEntity
public abstract class Edge {
    @GraphId
    private Long graphID;
    public Long getId() {
        return graphID;
    }
}
