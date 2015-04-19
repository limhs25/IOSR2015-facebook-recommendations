package pl.recommendations.crawling.remote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlerServer {
    private static final Logger logger = LogManager.getLogger(CrawlerServer.class.getName());

    public static final int PORT = 8808;
    private static final int THREADS = 4;

    private static final ExecutorService pool = Executors.newFixedThreadPool(THREADS);

    private final ServerSocket serverSocket;
    private final ApplicationContext context;

    public CrawlerServer(int port, ApplicationContext context) throws IOException {
        this.context = context;
        serverSocket = new ServerSocket(port);
    }

    public void acceptConnection() throws IOException {
        Socket socket = serverSocket.accept();
        CrawlerConnectionHandler connectionHandler = context.getBean(CrawlerConnectionHandler.class);
        connectionHandler.setStreams(socket);
        pool.submit(connectionHandler);
        logger.debug("accepted connection from: {} ", socket.getInetAddress());
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        CrawlerServer server = new CrawlerServer(PORT, context);
        logger.info("Awaiting connection");
        while (true) {
            server.acceptConnection();
        }
    }
}
