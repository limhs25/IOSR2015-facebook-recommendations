package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.crawling.CrawlerEndpoint;
<<<<<<< HEAD
import pl.recommendations.crawling.CrawlerScheduler;
import pl.recommendations.db.interest.Interest;
import pl.recommendations.db.interest.InterestRepository;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.util.Set;

@Component
public class EmbeddedCrawlerEndpoint implements CrawlerEndpoint {
=======
import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.interest.InterestEntityRepository;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.util.Map;
import java.util.Set;

@Transactional
@Service
public abstract class EmbeddedCrawlerEndpoint implements CrawlerEndpoint {
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
    private final static Logger logger = LogManager.getLogger(EmbeddedCrawlerEndpoint.class.getName());

    @Autowired
    private InterestEntityRepository interestsRepo;
    @Autowired
<<<<<<< HEAD
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
=======
    private PersonRepository peopleRepo;

    @Override
    public void scheduleCrawling(Long uuid) {
    }

    @Override
    public void onNewPerson(Long userId, String name) {
        if (peopleRepo.findByUuid(userId) == null) {
            Person person = new Person();
            person.setUuid(userId);
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
            person.setName(name);

            peopleRepo.save(person);
        } else {
<<<<<<< HEAD
            logger.warn("User[{}] already exists", uuid);
=======
            logger.debug("User[{}] already exists", userId);
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
        }
    }

    @Override
<<<<<<< HEAD
    public void onNewInterest(Long uuid, String name) {
        if (interestsRepo.findByUuid(uuid) == null) {
            Interest interest = new Interest();
            interest.setUuid(uuid);
            interest.setName(name);

            interestsRepo.save(interest);
        } else {
            logger.warn("Interest[{}] already exists", uuid);
=======
    public void onNewInterest(String interestName) {
        if (interestsRepo.findByName(interestName) == null) {
            InterestEntity interestEntity = new InterestEntity();
            interestEntity.setName(interestName);

            interestsRepo.save(interestEntity);
        } else {
            logger.debug("Interest[{}] already exists", interestName);
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
        }
    }

    @Override
<<<<<<< HEAD
    public void onAddFriends(Long uuid, Set<Long> friends) {
        Person person = peopleRepo.findByUuid(uuid);
        if (person != null) {
            friends.stream()
                    .map(peopleRepo::findByUuid)
                    .forEach(person::addFriend);
=======
    public void onAddFriends(Long userId, Set<Long> friends) {
        Person person = peopleRepo.findByUuid(userId);
        if (person != null) {
            friends.stream()
                    .map(peopleRepo::findByUuid)
                    .forEach(friend -> peopleRepo.addFriend(person, friend));
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
            ;
        } else {
            logger.warn("Cannot add friends to non-existing user");
        }
    }

    @Override
<<<<<<< HEAD
    public void onAddInterests(Long uuid, Set<Long> interests) {
        Person person = peopleRepo.findByUuid(uuid);
        if (person != null) {
            interests.stream()
                    .map(interestsRepo::findByUuid)
                    .forEach(person::addInterest);
            peopleRepo.save(person);
=======
    public void onAddInterests(Long userId, Map<String, Long> interests) {
        Person person = peopleRepo.findByUuid(userId);
        if (person != null) {
            for (Map.Entry<String, Long> entry : interests.entrySet()) {
                InterestEntity interest = interestsRepo.findByName(entry.getKey());
                if (interest != null) {
                    peopleRepo.addInterest(person, interest, entry.getValue());
                } else {
                    logger.warn("Interest {} does not exist", entry.getKey());
                }
            }
>>>>>>> 2970d6393f3fc719ea5af4dd6a1e5192a2a92647
        } else {
            logger.warn("Cannot add interests to non-existing user");
        }
    }
}
