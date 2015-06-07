package pl.recommendations.analyse.metrices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.analyse.Metric;

@Transactional
@Component("JaccardMetric")
public class JaccardMetric implements Metric {
    @Autowired
    private Neo4jTemplate template;

    @Override
    public Neo4jTemplate getNeo4jTemplate() {
        return template;
    }

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
    public String getQuery() {
        return query;
    }
}
