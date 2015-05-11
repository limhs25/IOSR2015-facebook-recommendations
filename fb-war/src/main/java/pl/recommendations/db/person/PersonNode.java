package pl.recommendations.db.person;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import pl.recommendations.db.Node;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class PersonNode extends Node {

    @Indexed(unique = true)
    private Long uuid;

    @Fetch
    private String name;

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<FriendshipEdge> friendshipEdges = new HashSet<>();

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<InterestEdge> interestEdges = new HashSet<>();
    
    public void addFriend(PersonNode friend) {
        if (friend != null && !this.equals(friend)) {
            FriendshipEdge relationship = new FriendshipEdge();
            relationship.setPersonNode(this);
            relationship.setFriend(friend);
            friendshipEdges.add(relationship);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonNode personNode = (PersonNode) o;

        return !(uuid != null ? !uuid.equals(personNode.uuid) : personNode.uuid != null);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    

    public Set<FriendshipEdge> getFriendshipEdges() {
        return ImmutableSet.copyOf(friendshipEdges);
    }

    public Set<InterestEdge> getInterestEdges() {
        return interestEdges;
    }

    public void addFriendship(FriendshipEdge relationship) {
        if (!friendshipEdges.contains(relationship)) {
            friendshipEdges.add(relationship);
        }
    }

    public void addInterest(InterestEdge interestEdge) {
        if(!interestEdges.contains(interestEdge)){
            interestEdges.add(interestEdge);
        }
    }
}
