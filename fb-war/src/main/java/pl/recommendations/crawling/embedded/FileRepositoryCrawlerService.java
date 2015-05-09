package pl.recommendations.crawling.embedded;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;

@Transactional
@Service("fileCrawlerService")
public class FileRepositoryCrawlerService extends EmbeddedCrawlerEndpoint implements FileRepositoryCrawler {
    private static final Logger logger = LogManager.getLogger(FileRepositoryCrawlerService.class.getName());

    private File dbDir;
    private String separator = ",";

    private File peopleFile;
    private File interestsFile;
    private File peopleRelationsFile;
    private File interestRelationsFile;

    public void setDbDir(File file) {
        this.dbDir = file;
        Preconditions.checkArgument(dbDir != null && dbDir.isDirectory(), "invalid directory " + file.getAbsolutePath());
    }

    @Override
    public void init() {
        Preconditions.checkState(dbDir != null);

        peopleFile = new File(dbDir, "people.csv");
        interestsFile = new File(dbDir, "interests.csv");
        peopleRelationsFile = new File(dbDir, "peopleRelations.csv");
        interestRelationsFile = new File(dbDir, "interestRelations.csv");
    }

    @Override
    public void run() {
        try {
            readPeople();
            readInterestEntities();
            readFriendships();
            readInterest();
        } catch (IOException e) {
            logger.error("Error while interest nodes people due to {}", e.getMessage(), e);
        }
    }

    @Override
    public void onAddFriends(Long userId, Set<Long> friends) {
        super.onNewPerson(userId, Long.toString(userId));
        friends.forEach(id -> super.onNewPerson(userId, Long.toString(id)));

        super.onAddFriends(userId, friends);
    }

    @Override
    public void onAddInterests(Long userId, Map<String, Long> interests) {
        super.onNewPerson(userId, Long.toString(userId));
        interests.keySet().forEach(super::onNewInterest);

        super.onAddInterests(userId, interests);
    }

    private void readPeople() throws IOException {
        if (!peopleFile.exists()) {
            return;
        }

        Files.lines(peopleFile.toPath()).forEach(line -> {
            String[] split = line.split(separator);
            long userId = Long.parseLong(split[0]);
            String userName = split[1];
            onNewPerson(userId, userName);
            logger.info("read people");
        });
    }

    private void readInterestEntities() throws IOException {
        if (!interestsFile.exists()) {
            return;
        }

        logger.info("reading interest entities");
        Files.lines(interestsFile.toPath()).forEach(this::onNewInterest);
        logger.info("read interest entities");

    }

    private void readFriendships() throws IOException {
        if (!peopleRelationsFile.exists()) {
            return;
        }

        logger.info("reading friendships");
        Files.lines(peopleRelationsFile.toPath()).forEach(line -> {
            String[] split = line.split(separator);
            long id1 = Long.parseLong(split[0]);
            long id2 = Long.parseLong(split[1]);
            onAddFriends(id1, ImmutableSet.of(id2));
        });
        logger.info("read friendships");

    }

    private void readInterest() throws IOException {
        if (!interestRelationsFile.exists()) {
            return;
        }

        logger.info("reading interests");
        Files.lines(interestRelationsFile.toPath()).forEach(line -> {
            String[] split = line.split(separator);
            long userId = Long.parseLong(split[0]);
            String interestName = split[1];
            long weight = Long.parseLong(split[2]);
            onAddInterests(userId, ImmutableMap.of(interestName, weight));
        });
        logger.info("read interests");

    }

    public static void main(String[] args) throws InterruptedException {
        File db = new File("fbDb");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        FileRepositoryCrawler crawler = (FileRepositoryCrawler) context.getBean("fileCrawlerService");
        crawler.setSeparator(" ");
        crawler.setDbDir(db);

        crawler.init();

        Thread thread = new Thread(crawler);

        thread.start();
        thread.join();
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
