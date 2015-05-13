package pl.recommendations.analyse.metrices;


import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.stereotype.Component;
import pl.recommendations.analyse.Metric;
import pl.recommendations.db.Node;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommonNeighbourMetric implements Metric {

    @Autowired
    protected PersonNodeRepository personRepo;

    @Override
    public double getDistance(Long firstUUID, Long secondUUID) {
        double score = 0.;

        Map<String,Object> map = new HashMap<>();
        map.put("firstUUID",firstUUID);
        map.put("secondUUID",secondUUID);

//        Result<Node> result = personRepo.query("match (user)-[:FRIENDSHIP]->(middle_friend)<-[:FRIENDSHIP]-(end_friend) " +
//                "where user.uuid = {firstUUID} and " +
//                "end_friend.uuid = {secondUUID}" +
//                "return middle_friend", map);

        return personRepo.countCommonFriend(firstUUID,secondUUID);
    }
}
