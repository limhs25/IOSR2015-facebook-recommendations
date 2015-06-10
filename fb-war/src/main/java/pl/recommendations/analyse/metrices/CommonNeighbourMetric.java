package pl.recommendations.analyse.metrices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.analyse.Metric;
import pl.recommendations.db.SuggestionType;

@Transactional
@Component("CommonNeighbourMetric")
public class CommonNeighbourMetric implements Metric {
    @Autowired
    private Neo4jTemplate template;

    @Override
    public Neo4jTemplate getNeo4jTemplate() {
        return template;
    }

    private static final String query =
            "match (begin)-[ret:FRIENDSHIP{type:'RETAINED'}]-()\n" + //
                    "with begin\n" + //
                    "match (begin)-[:FRIENDSHIP{type:'COMMON'}]-(middle)\n" + //
                    "with begin, middle\n" + //
                    "match (middle)-[:FRIENDSHIP{type:'COMMON'}]-(new_friend) \n" + //
                    "where not (begin)-[:FRIENDSHIP{type:'COMMON'}]-(new_friend) and begin <> new_friend\n" + //
                    "with begin, middle,new_friend, count(middle) as friendliness\n" + //
                    "return begin, new_friend,friendliness\n" + //
                    "order by friendliness DESC\n" + //
                    "limit ";

    @Override
    public String getQuery(Long count) {
        return query + count.toString() + ";";
    }

    @Override
    public SuggestionType getType() {
        return SuggestionType.NEIGHBOUR;
    }

    @Override
    public String getName() {
        return "Common Neighbour";
    }
}
