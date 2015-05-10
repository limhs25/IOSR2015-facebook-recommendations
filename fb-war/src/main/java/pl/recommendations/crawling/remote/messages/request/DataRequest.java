package pl.recommendations.crawling.remote.messages.request;

import pl.recommendations.crawling.remote.messages.CrawlerDataType;

public class DataRequest extends RequestMessage {

    private final CrawlerDataType dataType;

    public DataRequest(long uuid, CrawlerDataType type) {
        super(uuid);
        this.dataType = type;
    }

    public CrawlerDataType getDataType() {
        return dataType;
    }
}
