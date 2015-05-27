package pl.recommendations.analyse;

import pl.recommendations.db.person.PersonNode;

import java.util.LinkedList;
import java.util.Map;

public interface Metric {
    Map<PersonNode, LinkedList<PersonNode>> getSuggestionList(Long UUID);
}
