package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawlerEndpoint;
import pl.recommendations.crawling.CrawlerScheduler;
import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.interest.InterestRepository;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

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
    public void onNewPerson(Long uuid, String name) {
        if (peopleRepo.findByUuid(uuid) == null) {
            Person person = new Person();
            person.setUuid(uuid);
            person.setName(name);

            peopleRepo.save(person);
        } else {
            logger.warn("User[{}] already exists", uuid);
        }
    }

    @Override
    public void onNewInterest(Long uuid, String name) {
        if (interestsRepo.findByUuid(uuid) == null) {
            Interest interest = new Interest();
            interest.setUuid(uuid);
            interest.setName(name);

            interestsRepo.save(interest);
        } else {
            logger.warn("Interest[{}] already exists", uuid);
        }
    }

    @Override
    public void onAddFriends(Long uuid, Set<Long> friends) {
        Person person = peopleRepo.findByUuid(uuid);
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
    public void onAddInterests(Long uuid, Set<Long> interests) {
        Person person = peopleRepo.findByUuid(uuid);
        if (person != null) {
            interests.stream()
                    .map(interestsRepo::findByUuid)
                    .forEach(person::addInterest);
            peopleRepo.save(person);
        } else {
            logger.warn("Cannot add interests to non-existing user");
        }
    }
}
