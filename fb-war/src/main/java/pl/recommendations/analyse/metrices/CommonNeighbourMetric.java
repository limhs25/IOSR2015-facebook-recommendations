package pl.recommendations.analyse.metrices;


import pl.recommendations.analyse.Metric;
import pl.recommendations.db.person.FriendshipEdge;
import pl.recommendations.db.person.PersonNode;

import java.util.Set;

public class CommonNeighbourMetric implements Metric {
    @Override
    public double getDistance(PersonNode first, PersonNode second) {
        double score = 0.;

        Set<FriendshipEdge> firstFriendshipEdges  = first.getFriendshipEdges();
        Set<FriendshipEdge> secondFriendshipEdges = second.getFriendshipEdges();

        for (FriendshipEdge edge : firstFriendshipEdges) {
            if (secondFriendshipEdges.contains(edge)){
                score+=1;
            }
        }

        return score;
    }
}
