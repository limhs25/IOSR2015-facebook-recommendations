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
    @Query("match (u)-[:" + RelationshipType.FRIENDSHIP + "]->(u2) " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<PersonNode> getFriendsOf(Long id);

    @Query("match (u)-[:" + RelationshipType.INTEREST + "]->(u2) " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<InterestEdge> getInterestsOf(Long id);

    @Query("match (u)-[r:" + RelationshipType.SUGGESTION + "]->(u2) " +
            "where u.uuid = {0} " +
            "return r")
    Collection<SuggestionEdge> getSuggestionOf(Long id);

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

    @Query("match (begin)-[:" + RelationshipType.FRIENDSHIP + "]->(middle)<-[:" + RelationshipType.FRIENDSHIP + "]- (end) " +
            "where begin.uuid = {0} and end.uuid = {1} " +
            "return count(middle)")
    Long countCommonFriend(Long firstUUID, Long secondUUID);


    @Query("match (begin)-[:" + RelationshipType.FRIENDSHIP + "]->(middle)<-[:" + RelationshipType.FRIENDSHIP + "]-(end)\n" +
            "with end, count(middle) as cnt\n" +
            "where begin.uuid = {0} and end.uuid = {1} and cnt > 5\n" +
            "order by cnt\n" +
            "return end")
    Long getCommonFriend(Long firstUUID, Long secondUUID);

    @Query("match (b)-[r:FRIENDSHIP{type:'RETAINED'}]-(c)\n" +
            "\twith count(r) as totalRet\n" +
            "match (a)-[r:FRIENDSHIP{type:'RETAINED'}]-(c)-[s:SUGGESTION]-(a)\n" +
            "\treturn count(r) * 1.0 / totalRet")
    Double getSuggestionQuality();

    @Query("match ()-[r:FRIENDSHIP{type:'RETAINED'}]-() return count(r)")
    Long getRetainedAmount();

    default void addInterest(PersonNode personNode, InterestNode interestNode, Long weight) {
        Preconditions.checkArgument(weight > 0);
        Preconditions.checkArgument(interestNode != null);

        InterestEdge interestEdge = new InterestEdge();
        interestEdge.setPersonNode(personNode);
        interestEdge.setInterestNode(interestNode);
        interestEdge.setWeight(weight);

        personNode.addInterest(interestEdge);

    }

    default void addSuggestion(PersonNode personNode, PersonNode suggestion) {
        if (suggestion != null && !suggestion.equals(personNode)) {
            SuggestionEdge suggestionEdge = new SuggestionEdge();
            suggestionEdge.setPersonNode(personNode);
            suggestionEdge.setSuggestion(suggestion);
            personNode.addSuggestionEdge(suggestionEdge);
        }
    }


}
