package pl.recommendations.crawling;

import com.google.common.base.Preconditions;

public class CrawlLimit {
    private final long count;
    private final long cursor;

    public CrawlLimit(long count) {
        this(count, -1);
    }

    public CrawlLimit(long count, long cursor) {
        Preconditions.checkArgument(count >= 0, "Count must at least 0.");
        this.count = count;
        this.cursor = cursor;
    }

    public long getCount() {
        return count;
    }

    public long getCursor() {
        return cursor;
    }
}
