package pl.recommendations.db;

import org.springframework.data.neo4j.annotation.GraphId;

public abstract class Edge {
    @GraphId
    private Long graphID;

    public Long getId() {
        return graphID;
    }
}
