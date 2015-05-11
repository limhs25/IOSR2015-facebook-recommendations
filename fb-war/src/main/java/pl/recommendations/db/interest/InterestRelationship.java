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
    private InterestEntity firstInterestEntity;

    @EndNode
    private InterestEntity secondInterestEntity;

    private double value;

    public Set<InterestEntity> getInterests() {
        return ImmutableSet.of(firstInterestEntity, secondInterestEntity);
    }

    public double getValue() {
        return value;
    }

    public void setFirstInterest(InterestEntity firstInterestEntity) {
        this.firstInterestEntity = firstInterestEntity;
    }

    public void setSecondInterest(InterestEntity secondInterestEntity) {
        this.secondInterestEntity = secondInterestEntity;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
