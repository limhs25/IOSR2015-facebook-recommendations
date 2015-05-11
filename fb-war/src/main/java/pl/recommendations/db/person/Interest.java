package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.*;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.InterestEntity;

@RelationshipEntity(type = RelationshipType.INTEREST)
public class Interest {
    @GraphId
    private Long graphId;

    @Fetch
    @StartNode
    private Person person;

    @Fetch
    @EndNode
    private InterestEntity interest;

    private Long weight;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public InterestEntity getInterest() {
        return interest;
    }

    public void setInterest(InterestEntity interest) {
        this.interest = interest;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }
}
