package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import pl.recommendations.db.RelationshipType;

@RelationshipEntity(type = RelationshipType.FRIENDSHIP)
public class Friendship {
    @GraphId
    private Long graphId;

    @StartNode
    private
    Person person;

    @EndNode
    private Person friend;

    public Person getFriend() {
        return friend;
    }

    public void setFriend(Person friend) {
        this.friend = friend;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
