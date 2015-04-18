package pl.recommendations.crawling.remote.messages.notice;

import pl.recommendations.crawling.remote.messages.CrawlerDataType;

import java.util.Set;

public class AddRelations extends NoticeMessage{
    private final Set<Long> relations;

    public AddRelations(CrawlerDataType dataType, Long uuid, Set<Long> relations) {
        super(dataType, uuid);
        this.relations = relations;
    }

    public Set<Long> getRelations() {
        return relations;
    }
}
