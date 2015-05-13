package pl.recommendations.analyse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.analyse.metrices.CommonNeighbourMetric;
import pl.recommendations.db.person.FriendshipEdge;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static pl.recommendations.util.MapUtil.sortByValue;

@Transactional
@Service("analyseServiceImpl")
public class AnalyseServiceImpl implements AnalyseService {
    private final Logger logger = LogManager.getLogger(AnalyseServiceImpl.class.getName());

    @Autowired
    protected PersonNodeRepository personRepo;

    private Metric metric = new CommonNeighbourMetric();

    private Map<Long,Map<Long,Double>> obtainedSuggestions= new HashMap<>();
    private int suggestionSize = 5;


    @Override
    public void scheduleForAnalyse(Long userId) {
    }

    @Override
    public void analyse(Long uuid){


        PersonNode personNode = personRepo.findByUuid(uuid);
//        System.out.println(personNode.getName());


        Map suggestions = new TreeMap<PersonNode,Double>();
//        logger.info(personNode.getFriendshipEdges().size());

        if (personNode != null) {

            for(FriendshipEdge friendEdge : personNode.getFriendshipEdges()){
//                logger.info("Person:" + friendEdge.getPersonNode());
                logger.info("Friend:" + friendEdge.getFriend().getName());
                logger.info("FriendEdge:            " + friendEdge);
//                logger.info(friend.getFriend().getFriendshipEdges().size());

                    for (FriendshipEdge farFriend : friendEdge.getFriend().getFriendshipEdges()) {
//                        logger.info(personNode);
                        logger.info(farFriend.getFriend());
                        if (!personNode.equals(farFriend.getFriend())) {
                            PersonNode suggestedPerson = farFriend.getFriend();
                            Long suggestion = suggestedPerson.getUuid();
                            Double tmp =metric.getDistance(personNode, suggestedPerson);

                            logger.info(personNode != null);
                            suggestions.put(suggestion,tmp);

                        }
                    }
                }
        }
        obtainedSuggestions.put(uuid,suggestions);
        this.updateDatabase();
    }

    @Override
    public void updateDatabase(){
        for(Long uuid:obtainedSuggestions.keySet()){
            Map suggestions = obtainedSuggestions.get(uuid);
            Map<Long,Double> sortedSuggestions = sortByValue(suggestions);
            int amount = suggestionSize;
            Iterator entries = sortedSuggestions.entrySet().iterator();
            PersonNode personNode = personRepo.findByUuid(uuid);
            while(entries.hasNext() && amount > 0){
                Map.Entry<Long, Double> suggestionEntry = (Map.Entry<Long, Double>) entries.next();
                PersonNode suggestion = personRepo.findByUuid(suggestionEntry.getKey());
                personRepo.addSuggestion(personNode,suggestion);

                --amount;
            }
        }
    }



}
