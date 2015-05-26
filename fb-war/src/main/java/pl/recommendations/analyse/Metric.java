package pl.recommendations.analyse;

import java.util.List;

public interface Metric {
    List<Long> getSuggestionList(Long UUID);
}
