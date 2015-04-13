package pl.recommendations.crawling;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.Set;

public class CrawlResult {
    private final Set<Long> uuids;
    private final long nextCursor;

    public CrawlResult(Set<Long> uuids, long nextCursor) {
        Preconditions.checkNotNull(uuids);
        this.uuids = uuids;
        this.nextCursor = nextCursor;
    }

    public Set<Long> getUuids() {
        return uuids;
    }

    public long getNextCursor() {
        return nextCursor;
    }

    public static CrawlResult empty() {
        return new CrawlResult(Collections.emptySet(), 0);
    }
}
