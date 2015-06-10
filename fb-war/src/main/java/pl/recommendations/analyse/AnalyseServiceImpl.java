package pl.recommendations.analyse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.db.person.SuggestionEdge;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyseServiceImpl implements AnalyseService {
    private final Logger logger = LogManager.getLogger(AnalyseServiceImpl.class.getName());

    @Autowired
    private PersonNodeRepository personRepo;

    private Metric metric;

    @Override
    public void analyse() {
        Long count = personRepo.getRetainedAmount();
        logger.info("retained suggestions: " + count);
        Map<PersonNode, List<PersonNode>> suggestions = metric.getSuggestions(count);
        logger.info("Suggestions: {}", suggestions.size());

        for (Map.Entry<PersonNode, List<PersonNode>> entry : suggestions.entrySet()) {
            PersonNode node1 = entry.getKey();

            List<SuggestionEdge> ss = entry.getValue().stream().map(suggestion -> {
                SuggestionEdge edge = new SuggestionEdge();
                edge.setPersonNode(node1);
                edge.setSuggestion(suggestion);
                edge.setType(metric.getType());
                return edge;
            }).collect(Collectors.toList());

            personRepo.addSuggestions(node1, ss);
            logger.info("Suggesting {} friends to {}", ss.size(), node1);

        }
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }
}
