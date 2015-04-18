package pl.recommendations.crawling.remote.messages.request;

import pl.recommendations.crawling.remote.messages.CrawlerConnectionMessage;

public abstract class RequestMessage extends CrawlerConnectionMessage {
    private final long uuid;

    public RequestMessage(long uuid) {
        this.uuid = uuid;
    }

    public long getUuid() {
        return uuid;
    }
}
