package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawlerEndpoint;
import pl.recommendations.crawling.CrawlerScheduler;
import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.interest.InterestRepository;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.util.Map;
import java.util.Set;

@Component
public class EmbeddedCrawlerEndpoint implements CrawlerEndpoint {
    private final static Logger logger = LogManager.getLogger(EmbeddedCrawlerEndpoint.class.getName());

    @Autowired
    private CrawlerScheduler scheduler;
    @Autowired
    protected CrawledDataCache cache;
    @Autowired
    InterestRepository interestsRepo;
    @Autowired
    PersonRepository peopleRepo;

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduler.scheduleCrawling(uuid);
    }

    @Override
    public void onNewPerson(Long userId, String name) {
        if (peopleRepo.findByUuid(userId) == null) {
            Person person = new Person();
            person.setUuid(userId);
            person.setName(name);

            peopleRepo.save(person);
        } else {
            logger.debug("User[{}] already exists", userId);
        }
    }

    @Override
    public void onNewInterest(String interestName) {
        if (interestsRepo.findByName(interestName) == null) {
            InterestEntity interestEntity = new InterestEntity();
            interestEntity.setName(interestName);

            interestsRepo.save(interestEntity);
        } else {
            logger.debug("Interest[{}] already exists", interestName);
        }
    }

    @Override
    public void onAddFriends(Long userId, Set<Long> friends) {
        Person person = peopleRepo.findByUuid(userId);
        if (person != null) {
            friends.stream()
                    .map(peopleRepo::findByUuid)
                    .forEach(person::addFriend);
            ;
        } else {
            logger.warn("Cannot add friends to non-existing user");
        }
    }

    @Override
    public void onAddInterests(Long userId, Map<String, Long> interests) {
        Person person = peopleRepo.findByUuid(userId);
        if (person != null) {
            for (Map.Entry<String, Long> entry : interests.entrySet()) {
                InterestEntity interest = interestsRepo.findByName(entry.getKey());
                if(interest != null){
                    person.addInterest(interest, entry.getValue());
                }else{
                    logger.warn("Interest {} does not exist", entry.getKey());
                }
            }
        } else {
            logger.warn("Cannot add interests to non-existing user");
        }
    }
}
