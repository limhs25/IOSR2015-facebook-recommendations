package pl.recommendations.analyse.metrices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.analyse.Metric;

@Transactional
@Component("ResourceAllocationMetric")
public class ResourceAllocationMetric implements Metric {
    @Autowired
    private Neo4jTemplate template;

    @Override
    public Neo4jTemplate getNeo4jTemplate(){
        return template;
    }
    private static final String query =
            "match (begin)-[ret:FRIENDSHIP{type:'RETAINED'}]-()\n" +//
                    "with begin\n" +//
                    "match (begin)-[:FRIENDSHIP{type:'COMMON'}]-(middle)-[:FRIENDSHIP{type:'COMMON'}]-(new_friend)\n" +//
                    "where not (begin)-[:FRIENDSHIP{type:'COMMON'}]-(new_friend)\n" +//
                    "with begin,middle,new_friend\n" +//
                    "match (middle)-[s:FRIENDSHIP{type:'COMMON'}]-(n)\n" +//
                    "with begin,new_friend,middle, count(s) as friendliness\n" +//
                    "match (middle)-[:FRIENDSHIP{type:'COMMON'}]-(new_friend)\n" +//
                    "where not (begin)=(new_friend)\n" +//
                    "with begin,new_friend,sum(1.0/friendliness) as score\n" +//
                    "return begin,new_friend,score \n" +//
                    "order by score DESC\n" +//
                    "limit ";

    @Override
    public String getQuery(Long count) {
        return query+ count.toString() + ";";
    }
}
