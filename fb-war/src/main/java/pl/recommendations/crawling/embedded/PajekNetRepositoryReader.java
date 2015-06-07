package pl.recommendations.crawling.embedded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.db.person.FriendshipEdge;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.exceptions.FileReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PajekNetRepositoryReader {
    public static final Logger logger = LogManager.getLogger(StanfordRepositoryReader.class.getName());

    private static final String SEPARATOR = " ";
    public static final String NODES = "*Vertices ";
    public static final String EDGES = "*edges";
    public static final String ARCS = "*arcs";

    @Autowired
    private PersonNodeRepository peopleRepo;

    private Map<Long, PersonNode> people = new HashMap<>();
    private Map<PersonNode, List<FriendshipEdge>> peopleRelations = new HashMap<>();

    public void read(InputStream in) {
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        try {
            readNodes(stream);

            readEdges(stream);

            persist();
        } catch (IOException e) {
            throw new FileReadException(e);
        }
    }

    private void persist() {


        peopleRepo.save(people.values());
        logger.info("Saved {} peoples", people.values().size());

        peopleRelations.entrySet().forEach(e -> e.getValue().forEach(e.getKey()::addFriendship));
        peopleRepo.save(people.values());

        logger.info("read pajek repository");
    }

    private void readEdges(BufferedReader stream) throws IOException {
        String start = stream.readLine();
        if (!(EDGES.equals(start) || ARCS.equals(start)) )
            throw new FileReadException("Invalid file format");

        String line;
        while ((line = stream.readLine()) != null) {
            String[] split = line.split(SEPARATOR);
            Long uuid1 = Long.parseLong(split[0]);
            Long uuid2 = Long.parseLong(split[0]);

            PersonNode person = resolvePerson(uuid1);
            PersonNode friend = resolvePerson(uuid2);

            FriendshipEdge friendship = RepositoryReader.createFriendship(person, friend);

            peopleRelations.get(person).add(friendship);
        }
    }

    private void readNodes(BufferedReader stream) throws IOException {
        int nodes = Integer.parseInt(stream.readLine().split(SEPARATOR)[1]);

        for (int i = 0; i < nodes; i++) {
            String[] split = stream.readLine().split(SEPARATOR);

            Long uuid = Long.parseLong(split[0]);
            String name = split[1].substring(1, split[1].length() - 1);

            PersonNode person = new PersonNode();
            person.setUuid(uuid);
            person.setName(name);

            people.put(uuid, person);
        }
    }

    private PersonNode resolvePerson(Long userId) {
        PersonNode person = people.get(userId);

        if (person == null) {
            person = new PersonNode();
            person.setUuid(userId);
            people.put(userId, person);
            peopleRelations.put(person, new ArrayList<>());
        }

        if (peopleRelations.get(person) == null) {
            peopleRelations.put(person, new ArrayList<>());
        }

        return person;
    }


}
