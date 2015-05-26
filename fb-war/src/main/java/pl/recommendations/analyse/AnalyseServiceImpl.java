package pl.recommendations.analyse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.List;

@Service
@Transactional
public class AnalyseServiceImpl implements AnalyseService {
    private final Logger logger = LogManager.getLogger(AnalyseServiceImpl.class.getName());

    @Autowired
    private PersonNodeRepository personRepo;

    @Autowired
    @Qualifier("JaccardMetric")
    private Metric metric;

    @Override
    public void analyse(Long uuid, int suggestionSize) {

        PersonNode personNode = personRepo.findByUuid(uuid);

        List<Long> suggestions = metric.getSuggestionList(uuid);

        for (int i = 0; i < suggestionSize && i < suggestions.size(); i++) {
            personRepo.addSuggestion(personNode, personRepo.findByUuid(suggestions.get(i)));
        }

    }
}
