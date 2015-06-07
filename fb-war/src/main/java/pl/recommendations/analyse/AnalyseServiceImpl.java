package pl.recommendations.analyse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.List;
import java.util.Map;

@Service
public class AnalyseServiceImpl implements AnalyseService {
    private final Logger logger = LogManager.getLogger(AnalyseServiceImpl.class.getName());

    @Autowired
    private PersonNodeRepository personRepo;

    private Metric metric;

    @Override
    public void analyse() {
        Map<PersonNode, List<PersonNode>> suggestions = metric.getSuggestions();
        logger.info("Suggestions: {}", suggestions.size());

        for (Map.Entry<PersonNode, List<PersonNode>> entry : suggestions.entrySet()) {
            PersonNode node1 = entry.getKey();
            for (PersonNode suggestion : entry.getValue()) {
                personRepo.addSuggestion(node1, suggestion);
                logger.info("Suggesting {} to {}", node1, suggestion);
            }
        }
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }
}
