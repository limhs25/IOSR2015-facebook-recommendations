package pl.recommendations.db.interest;

import com.google.common.collect.ImmutableSet;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.StartNode;

import java.util.Set;


public class InterestRelationship {
    @GraphId
    protected Long graphId;

    @StartNode
    private Interest firstInterest;

    @EndNode
    private Interest secondInterest;

    private double value;

    public Set<Interest> getInterests() {
        return ImmutableSet.of(firstInterest, secondInterest);
    }

    public double getValue() {
        return value;
    }

    public void setFirstInterest(Interest firstInterest) {
        this.firstInterest = firstInterest;
    }

    public void setSecondInterest(Interest secondInterest) {
        this.secondInterest = secondInterest;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
