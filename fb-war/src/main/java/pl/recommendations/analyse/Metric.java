package pl.recommendations.analyse;

import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.person.PersonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public interface Metric {
    Neo4jTemplate getNeo4jTemplate();
    String getQuery(Long count);

    default Map<PersonNode, List<PersonNode>> getSuggestions(Long count){
        Map<PersonNode, List<PersonNode>> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();

        Result<Map<String, Object>> mapResult = getNeo4jTemplate().query(getQuery(count), map);

        for (Map<String, Object> r : mapResult) {
            PersonNode begin = getNeo4jTemplate().convert(r.get("begin"), PersonNode.class);
            PersonNode newFriend = getNeo4jTemplate().convert(r.get("new_friend"), PersonNode.class);

            if(!result.containsKey(begin)){
                result.put(begin, new ArrayList<>());
            }

            result.get(begin).add(newFriend);
        }

        return result;
    }
}
