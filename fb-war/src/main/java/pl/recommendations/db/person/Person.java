package pl.recommendations.db.person;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import pl.recommendations.db.interest.Interest;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Person {
    @GraphId
    private Long graphID;

    @Indexed(unique = true)
    private Long uuid;
    private String name;

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<Friendship> friendships = new HashSet<>();

    @Fetch
    @RelatedTo(direction = Direction.OUTGOING)
    private Set<Interest> interests = new HashSet<>();

    public void addFriend(Person friend) {
        if (friend != null && !this.equals(friend)) {
            Friendship relationship = new Friendship();
            relationship.setPerson(this);
            relationship.setFriend(friend);
            friendships.add(relationship);
        }
    }

    public void addInterest(Interest interest) {
        if (interest != null) {
            interests.add(interest);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (uuid != null ? !uuid.equals(person.uuid) : person.uuid != null) return false;

        return true;
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
    public Set<Interest> getInterests() {
        return ImmutableSet.copyOf(interests);
    }

    public Set<Friendship> getFriendships() {
        return ImmutableSet.copyOf(friendships);
    }

    public Long getId() {
        return graphID;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }
}
