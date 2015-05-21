package pl.recommendations.crawling.embedded;

import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public interface FileRepositoryCrawler {
    void readPeopleNodes(InputStream in);
    void readInterestNodes(InputStream in);
    void readPeopleEdges(InputStream in);
    void readInterestEdges(InputStream in);

    void persist();
}
