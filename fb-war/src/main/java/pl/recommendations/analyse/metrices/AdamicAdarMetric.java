package pl.recommendations.analyse.metrices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.analyse.AnalyseServiceImpl;
import pl.recommendations.analyse.Metric;
import pl.recommendations.db.person.PersonNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Transactional
@Component("AdamicAdarMetric")
public class AdamicAdarMetric implements Metric {
    private final Logger logger = LogManager.getLogger(AnalyseServiceImpl.class.getName());

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    private static final String query =
            "match (begin)\n" +
            "where begin.uuid = {uuid}\n" + //
            "with begin\n" + //
            "match (begin)-[:FRIENDSHIP]-(middle)-[:FRIENDSHIP]-(new_friend)\n" + //
            "where not (begin)-[:FRIENDSHIP]-(new_friend)\n" + //
            "with begin,middle,new_friend\n" + //
            "match (middle)-[s:FRIENDSHIP]-(n)\n" + //
            "with begin,new_friend,middle, count(s) as friendliness\n" + //
            "match (middle)-[:FRIENDSHIP]-(new_friend)\n" + //
            "where not (begin)=(new_friend)\n" + //
            "with new_friend,sum(1.0/log(friendliness)) as score\n" + //
            "return new_friend,score \n" + //
            "order by score DESC";

    @Override
    public Map<Long, List<Long>> getSuggestionList(Long UUID) {
        LinkedList<Long> result = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", UUID);

        Result<Map<String, Object>> mapResult = neo4jTemplate.query(query, map);
        logger.info("UUID:" + UUID);
        for (Map<String, Object> r : mapResult) {
            result.addLast(neo4jTemplate.convert(r.get("new_friend"), PersonNode.class).getUuid());
        }

        return result;
    }
}
