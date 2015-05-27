package pl.recommendations.crawling.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawledDataCache;
import pl.recommendations.crawling.CrawledDataListener;
import pl.recommendations.crawling.CrawlerService;
import pl.recommendations.crawling.remote.messages.notice.AddFriends;
import pl.recommendations.crawling.remote.messages.notice.AddInterests;
import pl.recommendations.crawling.remote.messages.notice.NewInterest;
import pl.recommendations.crawling.remote.messages.notice.NewPerson;
import pl.recommendations.crawling.remote.messages.notice.NoticeMessage;
import pl.recommendations.crawling.remote.messages.request.RequestCrawling;
import pl.recommendations.crawling.remote.messages.request.RequestMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class CrawlerConnectionHandler implements CrawlerService, CrawledDataListener, Runnable {
    public static final Logger logger = LogManager.getLogger(CrawlerConnectionHandler.class.getName());

    @Autowired
    @Qualifier("crawlerScheduler")
    private CrawlerService scheduler;

    @Autowired
    protected CrawledDataCache cache;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Socket socket;

    public synchronized void setStreams(Socket s) throws IOException {
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
    public synchronized void onNewPerson(Long uuid, String name) {
        NoticeMessage msg = new NewPerson(uuid, name);
        send(msg);
    }

    @Override
    public synchronized void onNewInterest(String interestName) {
        NoticeMessage msg = new NewInterest(interestName);
        send(msg);
    }

    @Override
    public synchronized void onAddFriends(Long userId, Set<Long> friends) {
        NoticeMessage msg = new AddFriends(userId, new HashSet<>(friends));
        send(msg);
    }


    @Override
    public synchronized void onAddInterests(Long userId, Map<String, Long> interests) {
        NoticeMessage msg = new AddInterests(userId, new HashMap<>(interests));
        send(msg);
    }

    @Override
    public synchronized void scheduleCrawling(Long uuid, boolean highPriority) {
        scheduler.scheduleCrawling(uuid, highPriority);
    }

    private void dispatch(RequestMessage msg) {
        long uuid = msg.getUuid();
        if (msg instanceof RequestCrawling) {
            scheduleCrawling(uuid, false);
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
