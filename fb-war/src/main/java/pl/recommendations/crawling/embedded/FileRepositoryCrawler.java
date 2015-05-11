package pl.recommendations.crawling.embedded;

import java.io.InputStream;

public interface FileRepositoryCrawler {
    void readPeopleNodes(InputStream in, String separator);
    void readInterestNodes(InputStream in, String separator);
    void readPeopleEdges(InputStream in, String separator);
    void readInterestEdges(InputStream in, String separator);

    void persist();
}
