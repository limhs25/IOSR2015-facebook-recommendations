package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.interest.InterestNode;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.person.FriendshipEdge;
import pl.recommendations.db.person.InterestEdge;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.io.*;
import java.util.HashMap;

@Transactional
@Service("fileCrawlerService")
public class FileRepositoryCrawlerService implements FileRepositoryCrawler {
    private static final Logger logger = LogManager.getLogger(FileRepositoryCrawlerService.class.getName());

    @Autowired
    protected InterestNodeRepository interestsRepo;
    @Autowired
    protected PersonNodeRepository peopleRepo;

    private final HashMap<Long, PersonNode> people = new HashMap<>();
    private final HashMap<String, InterestNode> interests = new HashMap<>();


    @Override
    public void persist() {
        long start = System.currentTimeMillis();

        peopleRepo.save(people.values());
        logger.info("saved database in {}s", (System.currentTimeMillis() - start) / 1000.0);
    }


    public void readPeopleNodes(InputStream in, String separator) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));
        stream.lines().forEach((String line) -> {
            String[] split = line.split(separator);
            long userId = Long.parseLong(split[0]);
            String userName = split[1];

            PersonNode person = new PersonNode();
            person.setUuid(userId);
            person.setName(userName);

            people.put(userId, person);
        });

        peopleRepo.save(people.values());
    }

    public void readInterestNodes(InputStream in, String separator) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        stream.lines().forEach(interestName -> {
            InterestNode interestEntity = new InterestNode();
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

            PersonNode person = resolvePerson(id1);
            PersonNode friend = resolvePerson(id2);

            FriendshipEdge friendship = new FriendshipEdge();
            friendship.setPersonNode(person);
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

                    PersonNode person = resolvePerson(userId);
                    InterestNode interestEntity = resolveInterest(interestName);

                    InterestEdge interest = new InterestEdge();
                    interest.setPersonNode(person);
                    interest.setInterestNode(interestEntity);
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

    private InterestNode resolveInterest(String interestName) {
        InterestNode interest = interests.get(interestName);

        if (interest == null) {
            interest = new InterestNode();
            interest.setName(interestName);
            interests.put(interestName, interest);
        }

        return interest;
    }

    private PersonNode resolvePerson(Long userId) {
        PersonNode person = people.get(userId);

        if (person == null) {
            person = new PersonNode();
            person.setUuid(userId);
            peopleRepo.save(person);
            people.put(userId, person);
        }
        return person;
    }
}
