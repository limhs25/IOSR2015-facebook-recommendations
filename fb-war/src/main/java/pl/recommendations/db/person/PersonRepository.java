package pl.recommendations.db.person;

import com.google.common.base.Preconditions;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.InterestEntity;

import java.util.Collection;

@Transactional
public interface PersonRepository extends GraphRepository<Person> {
    Person findByUuid(Long uuid);

    void deleteAll();

    @Query("match u-[" + RelationshipType.FRIENDSHIP + "]->u2 " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<Person> getFriendsOf(Long id);

    @Query("match u-[" + RelationshipType.INTEREST + "]->u2 " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<InterestEntity> getInterestsOf(Long id);

    default void addFriend(Person person, Person friend) {
        if (friend != null && !friend.equals(person)) {
            Friendship relationship = new Friendship();
            relationship.setPerson(person);
            relationship.setFriend(friend);
            person.addFriendship(relationship);
        }
    }

    default void addInterest(Person person, InterestEntity interestEntity, Long weight) {
        Preconditions.checkArgument(weight > 0);
        Preconditions.checkArgument(interestEntity != null);

        Interest interest = new Interest();
        interest.setPerson(person);
        interest.setInterest(interestEntity);
        interest.setWeight(weight);

        person.addInterest(interest);

    }
}
