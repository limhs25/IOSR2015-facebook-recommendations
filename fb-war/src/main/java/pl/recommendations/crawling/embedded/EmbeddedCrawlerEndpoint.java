package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawlerEndpoint;
import pl.recommendations.crawling.CrawlerService;
import pl.recommendations.db.interest.InterestNode;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.util.Map;
import java.util.Set;

@Component
public abstract class EmbeddedCrawlerEndpoint implements CrawlerEndpoint {
    private final static Logger logger = LogManager.getLogger(EmbeddedCrawlerEndpoint.class.getName());

    @Autowired
    @Qualifier("crawlerScheduler")
    private CrawlerService scheduler;

    @Autowired
    protected CrawledDataCache cache;

    @Autowired
    protected InterestNodeRepository interestsRepo;

    @Autowired
    protected PersonNodeRepository peopleRepo;

    @Override
    public void scheduleCrawling(Long uuid, boolean highPriority) {
        scheduler.scheduleCrawling(uuid, highPriority);
    }

    @Override
    public void onNewPerson(Long uuid, String name) {
        if (peopleRepo.findByUuid(uuid) == null) {
            PersonNode personNode = new PersonNode();
            personNode.setUuid(uuid);
            personNode.setName(name);

            peopleRepo.save(personNode);
        } else {
            logger.debug("User[{}] already exists", uuid);
        }
    }


    @Override
    public void onNewInterest(String interestName) {
        if (interestsRepo.findByName(interestName) == null) {
            InterestNode interestNode = new InterestNode();
            interestNode.setName(interestName);

            interestsRepo.save(interestNode);
        } else {
            logger.debug("InterestNode[{}] already exists", interestName);
        }
    }

    @Override
    public void onAddFriends(Long uuid, Set<Long> friends) {
        PersonNode personNode = peopleRepo.findByUuid(uuid);
        if (personNode != null) {
            friends.stream()
                    .map(peopleRepo::findByUuid)
                    .forEach(friendNode ->  peopleRepo.addFriend(personNode, friendNode));
        } else {
            logger.debug("Cannot add friends to non-existing user");
        }
    }

    @Override
    public void onAddInterests(Long userId, Map<String, Long> interests) {
        PersonNode person = peopleRepo.findByUuid(userId);
        if (person != null) {
            for (Map.Entry<String, Long> entry : interests.entrySet()) {
                InterestNode interest = interestsRepo.findByName(entry.getKey());
                if (interest != null) {
                    peopleRepo.addInterest(person, interest, entry.getValue());
                } else {
                    logger.debug("Interest {} does not exist", entry.getKey());
                }
            }
        } else {
            logger.debug("Cannot add interests to non-existing user");
        }
    }
}
