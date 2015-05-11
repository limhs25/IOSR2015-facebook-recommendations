package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestEntity;
import pl.recommendations.db.interest.InterestEntityRepository;
import pl.recommendations.db.person.Friendship;
import pl.recommendations.db.person.Interest;
import pl.recommendations.db.person.Person;
import pl.recommendations.db.person.PersonRepository;

import java.io.*;
import java.util.HashMap;

@Transactional
@Service("fileCrawlerService")
public class FileRepositoryCrawlerService implements FileRepositoryCrawler {
    private static final Logger logger = LogManager.getLogger(FileRepositoryCrawlerService.class.getName());

    @Autowired
    protected InterestEntityRepository interestsRepo;
    @Autowired
    protected PersonRepository peopleRepo;

    private final HashMap<Long, Person> people = new HashMap<>();
    private final HashMap<String, InterestEntity> interests = new HashMap<>();


    @Override
    public void persist() {
        long start = System.currentTimeMillis();

        peopleRepo.save(people.values());
        logger.info("saved database in {}s", ( System.currentTimeMillis() - start) / 1000.0);
    }


    public void readPeopleNodes(InputStream in, String separator) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));
        stream.lines().forEach((String line) -> {
            String[] split = line.split(separator);
            long userId = Long.parseLong(split[0]);
            String userName = split[1];

            Person person = new Person();
            person.setUuid(userId);
            person.setName(userName);

            people.put(userId, person);
        });

        peopleRepo.save(people.values());
    }

    public void readInterestNodes(InputStream in, String separator) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        stream.lines().forEach(interestName -> {
            InterestEntity interestEntity = new InterestEntity();
            interestEntity.setName(interestName);

            interests.put(interestName, interestEntity);
        });

        interestsRepo.save(interests.values());
    }

    public void readPeopleEdges(InputStream in, String separator) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));
        stream.lines().forEach(line -> {
            String[] split = line.split(separator);
            long id1 = Long.parseLong(split[0]);
            long id2 = Long.parseLong(split[1]);

            Person person = resolvePerson(id1);
            Person friend = resolvePerson(id2);

            Friendship friendship = new Friendship();
            friendship.setPerson(person);
            friendship.setFriend(friend);

            person.addFriendship(friendship);
        });
        logger.info("read friendships");

    }

    public void readInterestEdges(InputStream in, String separator) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));
        stream.lines().forEach((String line) -> {
                    String[] split = line.split(separator);
                    long userId = Long.parseLong(split[0]);
                    String interestName = split[1];
                    long weight = Long.parseLong(split[2]);

                    Person person = resolvePerson(userId);
                    InterestEntity interestEntity = resolveInterest(interestName);

                    Interest interest = new Interest();
                    interest.setPerson(person);
                    interest.setInterest(interestEntity);
                    interest.setWeight(weight);

                    person.addInterest(interest);
                }
        );
        logger.info("read interests");
    }

    public static void main(String[] args) throws FileNotFoundException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");
        FileRepositoryCrawlerService fileRepositoryCrawler = context.getBean(FileRepositoryCrawlerService.class);

        String separator = ",";
        fileRepositoryCrawler.readPeopleNodes(new FileInputStream(new File("db/peopleNodes.csv")), separator);
        fileRepositoryCrawler.readInterestNodes(new FileInputStream(new File("db/interestNodes.csv")), separator);
        fileRepositoryCrawler.readPeopleEdges(new FileInputStream(new File("db/peopleRelations.csv")), separator);
        fileRepositoryCrawler.readInterestEdges(new FileInputStream(new File("db/interestRelations.csv")), separator);
    }

    private InterestEntity resolveInterest(String interestName) {
        InterestEntity interest = interests.get(interestName);

        if (interest == null) {
            interest = new InterestEntity();
            interest.setName(interestName);
            interests.put(interestName, interest);
        }

        return interest;
    }

    private Person resolvePerson(Long userId) {
        Person person = people.get(userId);

        if (person == null) {
            person = new Person();
            person.setUuid(userId);
            peopleRepo.save(person);
            people.put(userId, person);
        }
        return person;
    }
}
