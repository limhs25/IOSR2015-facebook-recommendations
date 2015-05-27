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
import java.util.Map;

@Transactional
@Component("CommonNeighbourMetric")
public class CommonNeighbourMetric implements Metric {

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    private static final String query =
            "match (begin)-[ret:FRIENDSHIP]-(e)\n" + //
                "where ret.type = 'RETAINED'\n" + //
                "with begin, count(ret) as retainedEdges\n" + //
            "match (begin)-[:FRIENDSHIP]-(middle)-[:FRIENDSHIP]-(new_friend)\n" + //
                "where not (begin)-[common:FRIENDSHIP]-(new_friend)\n" +
                "and common.type = 'COMMON' \n" + //
                "with new_friend, count(middle) as friendliness\n" + //
                "return begin, new_friend,friendliness\n" + //
                "order by friendliness DESC\n" +
                "limit retainedEdges";

    @Override
    public Map<PersonNode, LinkedList<PersonNode>> getSuggestionList(Long UUID) {
        Map<PersonNode, LinkedList<PersonNode>> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", UUID);

        Result<Map<String, Object>> mapResult = neo4jTemplate.query(query, map);

        for (Map<String, Object> r : mapResult) {
            PersonNode person = neo4jTemplate.convert(r.get("begin"), PersonNode.class);

            if(!result.containsKey(person)){
                result.put(person, new LinkedList<>());
            }

            result.get(person).addLast(neo4jTemplate.convert(r.get("new_friend"), PersonNode.class));
        }

        return result;
    }
}
