package pl.recommendations.crawling.remote.messages.response;

import pl.recommendations.crawling.remote.messages.CrawlerDataType;

public class EntityData extends ResponseMessage {
    private final CrawlerDataType dataType;
    private final Long uuid;
    private final String name;

    public EntityData(CrawlerDataType dataType, Long uuid, String name) {
        this.dataType = dataType;
        this.uuid = uuid;
        this.name = name;
    }

    public CrawlerDataType getDataType() {
        return dataType;
    }

    public Long getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
