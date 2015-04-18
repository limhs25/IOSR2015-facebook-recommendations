package pl.recommendations.crawling.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawledDataListener;
import pl.recommendations.crawling.CrawlerScheduler;
import pl.recommendations.crawling.CrawlerService;
import pl.recommendations.crawling.remote.messages.CrawlerConnectionMessage;
import pl.recommendations.crawling.remote.messages.CrawlerDataType;
import pl.recommendations.crawling.remote.messages.notice.AddRelations;
import pl.recommendations.crawling.remote.messages.notice.NewEntity;
import pl.recommendations.crawling.remote.messages.notice.NoticeMessage;
import pl.recommendations.crawling.remote.messages.request.RequestCrawling;
import pl.recommendations.crawling.remote.messages.request.RequestMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class CrawlerConnectionHandler implements CrawlerService, CrawledDataListener, Runnable {
    public static final Logger logger = LogManager.getLogger(CrawlerConnectionHandler.class.getName());

    @Autowired
    private CrawlerScheduler scheduler;
    @Autowired
    protected CrawledDataCache cache;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final ConcurrentLinkedQueue<CrawlerConnectionMessage> commands = new ConcurrentLinkedQueue<>();
    private Socket socket;

    public void setStreams(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        cache.register(this);
        while (socket != null) {
            try {
                Object msg = in.readObject();
                if (msg instanceof RequestMessage) {
                    dispatch((RequestMessage) msg);
                } else {
                    logger.warn("Invalid message: " + msg.toString());
                }
            } catch (IOException e) {
                logger.error("Error during input stream consuming due to: {}", e.getMessage());
                socket = null;
            } catch (ClassNotFoundException e) {
                logger.error("Invalid message format: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onNewPerson(Long uuid, String name) {
        NoticeMessage msg = new NewEntity(CrawlerDataType.PERSON, uuid, name);
        send(msg);
    }

    @Override
    public void onNewInterest(Long uuid, String name) {
        NoticeMessage msg = new NewEntity(CrawlerDataType.INTEREST, uuid, name);
        send(msg);
    }

    @Override
    public void onAddFriends(Long uuid, Set<Long> friends) {
        NoticeMessage msg = new AddRelations(CrawlerDataType.PERSON, uuid, new HashSet<>(friends));
        send(msg);
    }

    @Override
    public void onAddInterests(Long uuid, Set<Long> interests) {
        NoticeMessage msg = new AddRelations(CrawlerDataType.INTEREST, uuid, new HashSet<>(interests));
        send(msg);
    }

    @Override
    public void scheduleCrawling(Long uuid) {
        scheduler.scheduleCrawling(uuid);
    }

    private void dispatch(RequestMessage msg) {
        long uuid = msg.getUuid();
        if (msg instanceof RequestCrawling) {
            scheduleCrawling(uuid);
        }
    }

    private void send(NoticeMessage msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            logger.error("Could not send message: {} due to {}", msg, e.getMessage());
        }
    }


}
