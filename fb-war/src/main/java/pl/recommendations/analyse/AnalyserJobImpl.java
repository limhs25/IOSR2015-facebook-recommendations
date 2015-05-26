package pl.recommendations.analyse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.Node;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

/**
 * Created by marekmagik on 2015-05-26.
 */
@Component
@Transactional
public class AnalyserJobImpl implements Job {

    private static final Logger log = LogManager.getLogger(AnalyserJobImpl.class);

    @Autowired
    private AnalyseService analyseService;

    @Autowired
    private PersonNodeRepository personRepo;

    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void execute() {
        log.info("Starting Analyzer Job...");

        int processedNodes = 0;
        int failedProcessing = 0;
        for (Node node : personRepo.findAll()) {
            try {
                analyseService.analyse(((PersonNode) node).getUuid(), AnalyseService.PREFFERED_SUGGESTION_SIZE);
                processedNodes++;
            } catch (Exception e) {
                log.warn("Analyzer job error for node: " + node.getId());
                failedProcessing++;
            }
        }
        StringBuilder builder = new StringBuilder("Analyzer Job has finished, after processing ") //
                .append(processedNodes).append(" nodes sucessfully and ") //
                .append(failedProcessing).append(" with errors");
        log.info(builder.toString());
    }
}
