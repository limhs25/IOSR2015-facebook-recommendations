package pl.quatrofantastico.fb.db.model.nodes;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class FacebookUser {
    @GraphId
    private Long id;

    @Indexed(unique = true)
    private String name;

    @Fetch
    @RelatedTo(type = "knows", direction = Direction.BOTH)
    private Set<FacebookUser> friends = new HashSet<>();

    @Fetch
    @RelatedTo(type = "likes")
    private Set<FacebookContent> interests = new HashSet<>();

    public void addFriend(FacebookUser user) {
        if (!user.equals(this)) {
            friends.add(user);
        }
    }

    public void addInterest(FacebookContent interest) {
        interests.add(interest);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookUser)) return false;
        String thatName = ((FacebookUser) o).name;
        if (name == null) return ((FacebookUser) o).name == null;
        return name.equals(thatName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FacebookUser> getFriends() {
        return friends;
    }

    public void setFriends(Set<FacebookUser> friends) {
        this.friends = friends;
    }

    public Set<FacebookContent> getInterests() {
        return interests;
    }

    public void setInterests(Set<FacebookContent> interests) {
        this.interests = interests;
    }
}
