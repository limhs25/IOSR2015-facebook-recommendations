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
@Component("JaccardMetric")
public class JaccardMetric implements Metric {

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    private static final String query = "match (source)\n" + //
            "where source.uuid = {uuid}\n" + //
            "with source\n" + //
            "match (source)-[:FRIENDSHIP]-(middle)-[:FRIENDSHIP]-(new_friend)\n" + //
            "where not (source)-[:FRIENDSHIP]-(new_friend)\n" + //
            "with source,middle,new_friend, count(middle) as middleFriendsAmount\n" + //
            "match (source)-[bf:FRIENDSHIP]-(n)\n" + //
            "with source,middle,new_friend,middleFriendsAmount, count(bf) as sourceFriendsAmount\n" + //
            "match (new_friend)-[nf:FRIENDSHIP]-(n)\n" + //
            "with new_friend, middleFriendsAmount, sourceFriendsAmount, count(nf) as newFriendFriendsAmount\n" + //
            "match (new_friend)\n" + //
            "with new_friend, toFloat(middleFriendsAmount)/(newFriendFriendsAmount" + //
            " + sourceFriendsAmount - middleFriendsAmount) as score \n" + //
            "return new_friend, score\n" + //
            "order by score DESC";


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
