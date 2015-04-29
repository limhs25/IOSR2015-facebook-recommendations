package pl.recommendations.crawling.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FileStoreClient extends CrawlerClient {
    private static final Logger logger = LogManager.getLogger(FileStoreClient.class.getName());

    private final FileWriter people;
    private final FileWriter interests;
    private final FileWriter interestRelations;
    private final FileWriter peopleRelations;

    public FileStoreClient(File dbDir) throws IOException {
        File peopleFile = new File(dbDir, "people.csv");
        File interestsFile = new File(dbDir, "interests.csv");
        File peopleRelationsFile = new File(dbDir, "peopleRelations.csv");
        File interestRelationsFile = new File(dbDir, "interestRelations.csv");

        dbDir.mkdirs();
        peopleFile.createNewFile();
        interestsFile.createNewFile();
        peopleRelationsFile.createNewFile();
        interestRelationsFile.createNewFile();

        people = new FileWriter(peopleFile);
        interests = new FileWriter(interestsFile);
        peopleRelations = new FileWriter(peopleRelationsFile);
        interestRelations = new FileWriter(interestRelationsFile);
    }

    @Override
    public void onNewPerson(Long userId, String name) {
        write(people, userId, name);
    }


    @Override
    public void onNewInterest(String interestName) {
        write(interests, interestName);
    }

    @Override
    public void onAddFriends(Long userId, Set<Long> friends) {
        friends.forEach(f -> write(peopleRelations, userId, f));
        logger.info("added {} friends", friends.size());
    }

    @Override
    public void onAddInterests(Long userId, Map<String, Long> interests) {
        interests.entrySet().forEach(e -> write(interestRelations, userId, e.getKey(), e.getValue()));
        logger.info("added {} interests", interests.size());
    }

    private void write(FileWriter writer, Object... objects) {
        try {
            Optional<Object> line = Arrays.stream(objects).reduce((s1, s2) -> s1 + "," + s2);
            if (line.isPresent()) {
//                logger.info("Writing {}", line.get());
                writer.write(line.get() + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            logger.warn("could not write line to file due to {}", e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FileStoreClient client = new FileStoreClient(new File("csvDb"));
        client.setAddress("localhost");
        client.setPort(8808);
        client.connect();

        new Thread(client).start();

        client.scheduleCrawling(17765013l);
        while (true) {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS));
        }
    }
}
