package pl.recommendations.db.person;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import pl.recommendations.db.interest.InterestEntity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Person {
    @GraphId
    private Long graphID;

    @Indexed(unique = true)
    private Long uuid;
    @Fetch
    private String name;

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<Friendship> friendships = new HashSet<>();

    @RelatedToVia(direction = Direction.OUTGOING)
    private Set<Interest> interests = new HashSet<>();

    public void addFriend(Person friend) {
        if (friend != null && !this.equals(friend)) {
            Friendship relationship = new Friendship();
            relationship.setPerson(this);
            relationship.setFriend(friend);
            friendships.add(relationship);
        }
    }

    public void addInterest(InterestEntity interestEntity, Long weight) {
        Preconditions.checkArgument(weight > 0);

        Interest interest = new Interest();
        interest.setPerson(this);
        interest.setInterest(interestEntity);

        if (interestEntity != null && !interests.contains(interest)) {
            interest.setWeight(weight);
            interests.add(interest);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return !(uuid != null ? !uuid.equals(person.uuid) : person.uuid != null);
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

    public Set<Friendship> getFriendships() {
        return ImmutableSet.copyOf(friendships);
    }

    public Long getId() {
        return graphID;
    }

    public Set<Interest> getInterests() {
        return interests;
    }
}
