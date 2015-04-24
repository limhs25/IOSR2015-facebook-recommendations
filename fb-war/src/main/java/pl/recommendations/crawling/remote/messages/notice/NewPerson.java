package pl.recommendations.crawling.remote.messages.notice;

public class NewPerson extends NoticeMessage{
    private final Long userID;
    private final String name;

    public NewPerson(Long userID, String name) {
                this.userID = userID;
        this.name = name;
    }

    public Long getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
}
