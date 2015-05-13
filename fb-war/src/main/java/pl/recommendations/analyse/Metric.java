package pl.recommendations.analyse;


import pl.recommendations.db.person.PersonNode;

public interface Metric {
    double getDistance(PersonNode first,PersonNode second);
}
