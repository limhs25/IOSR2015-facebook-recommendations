package pl.recommendations.crawling.remote.messages.notice;

import java.util.Set;

public class AddFriends extends NoticeMessage {
    private final Long userId;
    private final Set<Long> friendsIds;

    public AddFriends(Long userId, Set<Long> friendsIds) {
        this.friendsIds = friendsIds;
        this.userId = userId;
    }

    public Set<Long> getFriendsIds() {
        return friendsIds;
    }

    public Long getUserId() {
        return userId;
    }
}
