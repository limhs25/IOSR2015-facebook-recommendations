package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.db.person.FriendshipEdge;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StanfordRepositoryReader {
    public static final Logger logger = LogManager.getLogger(StanfordRepositoryReader.class.getName());

    private static final String SEPARATOR = " ";
    @Autowired
    private PersonNodeRepository peopleRepo;

    private Map<Long, PersonNode> people = new HashMap<>();
    private Map<PersonNode, List<FriendshipEdge>> peopleRelations = new HashMap<>();

    public void read(InputStream in){
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));
        stream.lines().forEach(line -> {
            String[] split = line.split(SEPARATOR);
            long id1 = Long.parseLong(split[0]);
            long id2 = Long.parseLong(split[1]);

            PersonNode person = resolvePerson(id1);
            PersonNode friend = resolvePerson(id2);

            FriendshipEdge friendship = RepositoryReader.createFriendship(person, friend);

            peopleRelations.get(person).add(friendship);
            });

        peopleRepo.save(people.values());
        logger.info("Saved {} peoples", people.values().size());

        peopleRelations.entrySet().forEach(e -> e.getValue().forEach(e.getKey()::addFriendship));
        peopleRepo.save(people.values());

        logger.info("read stanford repository");
    }

    private PersonNode resolvePerson(Long userId) {
        PersonNode person = people.get(userId);

        if (person == null) {
            person = new PersonNode();
            person.setUuid(userId);
            people.put(userId, person);
            peopleRelations.put(person, new ArrayList<>());
        }
        return person;
    }
}
