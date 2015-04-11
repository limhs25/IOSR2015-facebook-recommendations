package pl.recommendations.db.user;

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
    User user;

    @EndNode
    private User friend;

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
