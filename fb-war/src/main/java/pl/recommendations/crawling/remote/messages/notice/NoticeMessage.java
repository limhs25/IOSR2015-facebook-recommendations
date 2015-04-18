package pl.recommendations.crawling.remote.messages.notice;

import pl.recommendations.crawling.remote.messages.CrawlerConnectionMessage;
import pl.recommendations.crawling.remote.messages.CrawlerDataType;

public abstract class NoticeMessage extends CrawlerConnectionMessage{
    private final Long uuid;
    public final CrawlerDataType dataType;

    public NoticeMessage(CrawlerDataType dataType, Long uuid) {
        this.uuid = uuid;
        this.dataType = dataType;
    }

    public Long getUuid() {
        return uuid;
    }

    public CrawlerDataType getDataType() {
        return dataType;
    }
}
