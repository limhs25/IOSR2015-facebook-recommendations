package pl.recommendations.db.person;

import com.google.common.base.Preconditions;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.NodeRepository;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.interest.InterestNode;

import java.util.Collection;

@Transactional
public interface PersonNodeRepository extends NodeRepository {
    @Query("match u-[" + RelationshipType.FRIENDSHIP + "]->u2 " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<PersonNode> getFriendsOf(Long id);

    @Query("match u-[" + RelationshipType.INTEREST + "]->u2 " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<InterestNode> getInterestsOf(Long id);

    PersonNode findByUuid(Long uuid);

    @Override
    void deleteAll();

    default void addFriend(PersonNode personNode, PersonNode friend) {
        if (friend != null && !friend.equals(personNode)) {
            FriendshipEdge relationship = new FriendshipEdge();
            relationship.setPersonNode(personNode);
            relationship.setFriend(friend);
            personNode.addFriendship(relationship);
        }
    }

    default void addInterest(PersonNode personNode, InterestNode interestNode, Long weight) {
        Preconditions.checkArgument(weight > 0);
        Preconditions.checkArgument(interestNode != null);

        InterestEdge interestEdge = new InterestEdge();
        interestEdge.setPersonNode(personNode);
        interestEdge.setInterestNode(interestNode);
        interestEdge.setWeight(weight);

        personNode.addInterest(interestEdge);

    }
}
