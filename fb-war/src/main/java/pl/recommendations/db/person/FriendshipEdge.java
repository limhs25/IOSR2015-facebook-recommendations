package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import pl.recommendations.db.Edge;
import pl.recommendations.db.RelationshipType;

@RelationshipEntity(type = RelationshipType.FRIENDSHIP)
public class FriendshipEdge extends Edge {

    @StartNode
    private PersonNode personNode;

    @EndNode
    @Indexed
    private PersonNode friend;

    public PersonNode getFriend() {
        return friend;
    }

    public void setFriend(PersonNode friend) {
        this.friend = friend;
    }

    public PersonNode getPersonNode() {
        return personNode;
    }

    public void setPersonNode(PersonNode personNode) {
        this.personNode = personNode;
    }
}
