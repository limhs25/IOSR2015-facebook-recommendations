package pl.recommendations.crawling;

public interface CrawledDataStorage {
    Object getPersonName(Long uuid);

    Object getInterestName(Long uuid);

    boolean hasPerson(Long uuid);

    boolean hasInterest(Long uuid);
}
