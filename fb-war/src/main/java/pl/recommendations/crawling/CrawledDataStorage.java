package pl.recommendations.crawling;

public interface CrawledDataStorage {
    String getPersonName(Long uuid);


    boolean hasPerson(Long uuid);

    boolean hasInterest(Long uuid);
}
