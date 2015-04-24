package pl.recommendations.crawling.remote.messages.notice;

public class NewInterest extends NoticeMessage{
    private final String name;

    public NewInterest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
