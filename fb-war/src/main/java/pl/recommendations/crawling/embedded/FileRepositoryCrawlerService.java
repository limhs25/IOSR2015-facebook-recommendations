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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Transactional
@Service("fileCrawlerService")
public class FileRepositoryCrawlerService extends EmbeddedCrawlerEndpoint implements FileRepositoryCrawler {
    private static final Logger logger = LogManager.getLogger(FileRepositoryCrawlerService.class.getName());
    public static final String SEPARATOR = ",";

    private File dbDir;

    private Path peopleFile;
    private Path interestsFile;
    private Path peopleRelationsFile;
    private Path interestRelationsFile;

    public void setDbDir(File file) {
        this.dbDir = file;
        Preconditions.checkArgument(dbDir != null && dbDir.isDirectory(), "invalid directory " + dbDir.getAbsolutePath());
    }

    @Override
    public void init() {
        Preconditions.checkState(dbDir != null);

        peopleFile = FileSystems.getDefault().getPath(dbDir.getAbsolutePath(), "people.csv");
        interestsFile = FileSystems.getDefault().getPath(dbDir.getAbsolutePath(), "interests.csv");
        peopleRelationsFile = FileSystems.getDefault().getPath(dbDir.getAbsolutePath(), "peopleRelations.csv");
        interestRelationsFile = FileSystems.getDefault().getPath(dbDir.getAbsolutePath(), "interestRelations.csv");
    }

    @Override
    public void run() {
        try {
            readPeople();
            readInterestEntities();
            readFriendships();
            readInterest();
        } catch (IOException e) {
            logger.error("Error while reading crawler repository in {} due to {}", dbDir.getAbsolutePath(), e
                    .getMessage(), e);
        }
    }

    private void readPeople() throws IOException {
        Files.lines(peopleFile).forEach(line -> {
            String[] split = line.split(",");
            long userId = Long.parseLong(split[0]);
            String userName = split[1];
            onNewPerson(userId, userName);
            logger.info("read people");
        });
    }

    private void readInterestEntities() throws IOException {
        logger.info("reading interest entities");
        Files.lines(interestsFile).forEach(this::onNewInterest);
        logger.info("read interest entities");
    }

    private void readFriendships() throws IOException {
        logger.info("reading friendships");
        Files.lines(peopleRelationsFile).forEach(line -> {
            String[] split = line.split(",");
            long id1 = Long.parseLong(split[0]);
            long id2 = Long.parseLong(split[1]);
            onAddFriends(id1, ImmutableSet.of(id2));
        });
        logger.info("read friendships");
    }

    private void readInterest() throws IOException {
        logger.info("reading interests");
        Files.lines(interestRelationsFile).forEach(line -> {
            String[] split = line.split(SEPARATOR);
            long userId = Long.parseLong(split[0]);
            String interestName = split[1];
            long weight = Long.parseLong(split[2]);
            onAddInterests(userId, ImmutableMap.of(interestName, weight));
        });
        logger.info("read interests");
    }

    public static void main(String[] args) throws InterruptedException {
        File db = new File("csvDb");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        FileRepositoryCrawler crawler = (FileRepositoryCrawler) context.getBean("fileCrawlerService");
        crawler.setDbDir(db);

        crawler.init();

        Thread thread = new Thread(crawler);

        thread.start();
        thread.join();
    }
}
