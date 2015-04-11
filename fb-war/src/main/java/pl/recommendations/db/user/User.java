package pl.recommendations.db.user;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import pl.recommendations.db.interest.Interest;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class User {
    @GraphId
    private Long graphID;

    private String name;

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<Friendship> friendships = new HashSet<>();

    @Fetch
    @RelatedTo(direction = Direction.OUTGOING)
    private Set<Interest> interests = new HashSet<>();

    public void addFriend(User friend) {
        if (!this.equals(friend)) {
            Friendship relationship = new Friendship();
            relationship.setUser(this);
            relationship.setFriend(friend);
            friendships.add(relationship);
        }
    }

    public void addInterest(Interest i) {
        interests.add(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Interest> getInterests() {
        return ImmutableSet.copyOf(interests);
    }

    public Set<Friendship> getFriendships() {
        return ImmutableSet.copyOf(friendships);
    }

    public Long getId() {
        return graphID;
    }
}
