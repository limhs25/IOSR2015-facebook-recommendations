package pl.recommendations.crawling.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawlerEndpoint;
import pl.recommendations.crawling.remote.messages.notice.*;
import pl.recommendations.crawling.remote.messages.request.RequestCrawling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

@Component
public abstract class CrawlerClient implements CrawlerEndpoint, Runnable {
    private static final Logger logger = LogManager.getLogger(CrawlerClient.class.getName());

    private String address;
    private int port;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    public void connect() throws IOException {
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
        logger.info("Connected to {}:{}", address, port);
    }

    @Override
    public void run() {
        try {
            while (socket != null) {
                Object msg = in.readObject();
                if (msg instanceof NoticeMessage) {
                    dispatchNotice((NoticeMessage) msg);
                } else {
                    logger.warn("Invalid message: " + msg.toString());
                }
            }
            logger.info("SockeT {} closed", socket);
        } catch (IOException e) {
            logger.error("Error during connection: {}", e.getMessage());
            e.printStackTrace();
            socket = null;
        } catch (ClassNotFoundException e) {
            logger.error("Invalid object sent: {}", e.getMessage());
        }
    }

    @Override
    public void scheduleCrawling(Long uuid) {
        try {
            out.writeObject(new RequestCrawling(uuid));
        } catch (IOException e) {
            logger.warn("Could not schedule crawling for {} due to {}", uuid, e.getMessage());
        }
    }

    private void dispatchNotice(NoticeMessage msg) {
        logger.debug("Got " + msg);
        if (msg instanceof NewInterest) {
            onNewInterest(((NewInterest) msg).getName());
        } else if (msg instanceof NewPerson) {
            onNewPerson(((NewPerson) msg).getUserID(), ((NewPerson) msg).getName());
        } else if (msg instanceof AddFriends) {
            Set<Long> friends = ((AddFriends) msg).getFriendsIds();
            onAddFriends(((AddFriends) msg).getUserId(), friends);
        } else if (msg instanceof AddInterests) {
            Map<String, Long> interests = ((AddInterests) msg).getInterests();
            onAddInterests(((AddInterests) msg).getUserId(), interests);
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
