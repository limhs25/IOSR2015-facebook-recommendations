package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.*;
import pl.recommendations.db.Edge;
import pl.recommendations.db.RelationshipType;

@RelationshipEntity(type = RelationshipType.FRIENDSHIP)
public class FriendshipEdge extends Edge {

    @StartNode
    private PersonNode personNode;

    @EndNode
    @Indexed
    private PersonNode friend;

    @Fetch
    private FriendshipType type;

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

    public FriendshipType getType() {
        return type;
    }

    public void setType(FriendshipType type) {
        this.type = type;
    }
}
