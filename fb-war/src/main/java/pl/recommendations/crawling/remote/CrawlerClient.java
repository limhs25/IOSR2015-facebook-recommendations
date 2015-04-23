package pl.recommendations.crawling.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.recommendations.crawling.CrawlerEndpoint;
import pl.recommendations.crawling.remote.messages.CrawlerDataType;
import pl.recommendations.crawling.remote.messages.notice.AddRelations;
import pl.recommendations.crawling.remote.messages.notice.NewEntity;
import pl.recommendations.crawling.remote.messages.notice.NoticeMessage;
import pl.recommendations.crawling.remote.messages.request.RequestCrawling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        logger.debug("Connected to {}:{}", address, port);
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
            logger.debug("SockeT {} closed", socket);
        } catch (IOException e) {
            logger.error("Error during connection: {}", e.getMessage());
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
        CrawlerDataType dataType = msg.getDataType();
        Long uuid = msg.getUuid();
        if (msg instanceof NewEntity) {
            String name = ((NewEntity) msg).getName();

            if (dataType == CrawlerDataType.INTEREST) {
                onNewInterest(uuid, name);
            } else if (dataType == CrawlerDataType.PERSON) {
                onNewPerson(uuid, name);
            }
        } else if (msg instanceof AddRelations) {
            Set<Long> relations = ((AddRelations) msg).getRelations();

            if (dataType == CrawlerDataType.INTEREST) {
                onAddInterests(uuid, relations);
            } else if (dataType == CrawlerDataType.PERSON) {
                onAddFriends(uuid, relations);
            }
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
