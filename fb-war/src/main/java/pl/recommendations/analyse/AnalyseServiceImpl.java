package pl.recommendations.analyse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Component
public class AnalyseServiceImpl implements AnalyseService {
    private final Logger logger = LogManager.getLogger(AnalyseServiceImpl.class.getName());

    @Autowired
    protected PersonNodeRepository personRepo;

    @Autowired
    private Metric metric;

    @Autowired
    Neo4jTemplate neo4jTemplate;

    private Map<Long, Map<Long, Double>> obtainedSuggestions = new HashMap<>();
    private int suggestionSize = 5;


    @Override
    public void scheduleForAnalyse(Long userId) {

    }

    @Override
    public void analyse(Long uuid) {

        PersonNode personNode = personRepo.findByUuid(uuid);
        Map<Long, Double> suggestions = new TreeMap<Long, Double>();

//        if (personRepo.getFriendsOf(uuid).size()>0) {
//
//            Map<String,Object> map = new HashMap<>();
//            map.put("uuid",uuid);
//
//            Result<Node> result = personRepo.query("match (user)-[:FRIENDSHIP]->(middle_friend)-[:FRIENDSHIP]->(end_friend) " +
//                    "where user.uuid = {uuid} and " +
//                    "not  (user)-[:FRIENDSHIP]->(end_friend) " +
//                    "return end_friend", map);
//
//            for (Node node :  result) {
//                logger.info("Friend: " + node);
//                PersonNode farFriend = (PersonNode)node;
//                if (!personNode.equals(farFriend)) {
//
//                    Long suggestion = farFriend.getUuid();
//                    Double tmp =metric.getDistance(uuid,farFriend.getUuid());
//
//                    suggestions.put(suggestion,tmp);
//
//                }
//            }
//        }
        String query = "match (begin)-[:FRIENDSHIP]-(middle)-[:FRIENDSHIP]-(end)\n" +
                "with begin, end, count(middle) as cnt\n" +
                "where begin.uuid = {uuid}\n" +
                "return end,cnt\n" +
                "order by cnt DESC\n" +
                "limit 5";

        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);

        Result<Map<String, Object>> mapResult = neo4jTemplate.query(query, map);

        for (Map<String, Object> r : mapResult) {

            for (Map.Entry<String, Object> entry : r.entrySet()) {
                logger.info(entry.getKey() + ": " + entry.getValue());

            }
//            PersonNode p = (PersonNode) r;
//            personRepo.addSuggestion(personNode,((PersonNode) r));
        }

//        obtainedSuggestions.put(uuid,suggestions);
//        this.updateDatabase();
    }

    @Override
    public void updateDatabase() {
        for (Long uuid : obtainedSuggestions.keySet()) {
            Map<Long, Double> suggestions = obtainedSuggestions.get(uuid);
//            Map<Long,Double> sortedSuggestions = sortByValue(suggestions);
            int amount = suggestionSize;
//            Iterator entries = sortedSuggestions.entrySet().iterator();
//            PersonNode personNode = personRepo.findByUuid(uuid);
//            while(entries.hasNext() && amount > 0){
//                Map.Entry<Long, Double> suggestionEntry = (Map.Entry<Long, Double>) entries.next();
//                PersonNode suggestion = personRepo.findByUuid(suggestionEntry.getKey());
//                personRepo.addSuggestion(personNode,suggestion);
//
//                --amount;
//            }
        }
    }


}
