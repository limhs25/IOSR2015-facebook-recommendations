package pl.recommendations.analyse.metrices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.analyse.Metric;
import pl.recommendations.db.person.PersonNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Transactional
@Component("CommonNeighbourMetric")
public class CommonNeighbourMetric implements Metric {

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    private static final String query = "match (begin)\n" + //
            "where begin.uuid = {uuid}\n" + //
            "with begin\n" + //
            "match (begin)-[:FRIENDSHIP]-(middle)-[:FRIENDSHIP]-(new_friend)\n" + //
            "where not (begin)-[:FRIENDSHIP]-(new_friend)\n" + //
            "with new_friend, count(middle) as friendliness\n" + //
            "return new_friend,friendliness\n" + //
            "order by friendliness DESC";

    @Override
    public List<Long> getSuggestionList(Long UUID) {
        LinkedList<Long> result = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", UUID);

        Result<Map<String, Object>> mapResult = neo4jTemplate.query(query, map);

        for (Map<String, Object> r : mapResult) {
            result.addLast(neo4jTemplate.convert(r.get("new_friend"), PersonNode.class).getUuid());
        }

        return result;
    }
}
