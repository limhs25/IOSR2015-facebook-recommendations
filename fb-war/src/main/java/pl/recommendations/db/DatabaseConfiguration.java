package pl.recommendations.db;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@Configuration
public class DatabaseConfiguration extends Neo4jConfiguration {
    private static final String DB = "data/web-with-ind";
    public static final String[] basePackage = new String[]{"pl.recommendations"};

    @Override
    public String[] getBasePackage() {
        return basePackage;
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder(DB).
                setConfig(GraphDatabaseSettings.relationship_keys_indexable, "friend").
                setConfig(GraphDatabaseSettings.node_auto_indexing, "true").
                setConfig(GraphDatabaseSettings.relationship_auto_indexing, "true").
                newGraphDatabase();
    }
}
