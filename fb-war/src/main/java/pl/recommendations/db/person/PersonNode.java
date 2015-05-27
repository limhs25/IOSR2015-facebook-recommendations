package pl.recommendations.db.person;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import pl.recommendations.db.Node;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class PersonNode extends Node {

    @Indexed(unique = true)
    @Fetch
    protected Long uuid;

    @Fetch
    private String name;

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<FriendshipEdge> friendshipEdges = new HashSet<>();

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<InterestEdge> interestEdges = new HashSet<>();
    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<SuggestionEdge> suggestionEdges = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonNode personNode = (PersonNode) o;

        return !(uuid != null ? !uuid.equals(personNode.uuid) : personNode.uuid != null);
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

    public Set<SuggestionEdge> getSuggestionEdges() {
        return suggestionEdges;
    }

    public void addFriendship(FriendshipEdge relationship) {
        if (!friendshipEdges.contains(relationship)) {
            friendshipEdges.add(relationship);
        }
    }

    public void addInterest(InterestEdge interestEdge) {
        if (!interestEdges.contains(interestEdge)) {
            interestEdges.add(interestEdge);
        }
    }

    public void addSuggestionEdge(SuggestionEdge suggestionEdge) {
        if (!suggestionEdges.contains(suggestionEdge)) {
            suggestionEdges.add(suggestionEdge);
        }
    }
}
