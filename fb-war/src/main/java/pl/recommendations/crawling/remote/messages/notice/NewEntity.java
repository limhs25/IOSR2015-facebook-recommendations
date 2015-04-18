package pl.recommendations.crawling.remote.messages.notice;

import pl.recommendations.crawling.remote.messages.CrawlerDataType;

public class NewEntity extends NoticeMessage {
    private final String name;

    public NewEntity(CrawlerDataType dataType, Long uuid, String name) {
        super(dataType, uuid);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
