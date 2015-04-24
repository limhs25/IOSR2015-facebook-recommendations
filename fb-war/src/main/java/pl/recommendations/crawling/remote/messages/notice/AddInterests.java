package pl.recommendations.crawling.remote.messages.notice;

import java.util.Map;

public class AddInterests extends NoticeMessage {
    private final Long userId;
    private final Map<String, Long> interests;

    public AddInterests(Long userId, Map<String, Long> interests) {
        this.userId = userId;
        this.interests = interests;
    }

    public Map<String, Long> getInterests() {
        return interests;
    }

    public Long getUserId() {
        return userId;
    }
}
