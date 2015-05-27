package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import pl.recommendations.db.Edge;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.InterestNode;

@RelationshipEntity(type = RelationshipType.INTEREST)
public class InterestEdge extends Edge {

    @StartNode
    private PersonNode personNode;

    @EndNode
    private InterestNode interest;

    private Long weight;

    public PersonNode getPersonNode() {
        return personNode;
    }

    public void setPersonNode(PersonNode personNode) {
        this.personNode = personNode;
    }

    public InterestNode getInterest() {
        return interest;
    }

    public void setInterestNode(InterestNode interest) {
        this.interest = interest;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }
}
