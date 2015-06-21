package pl.recommendations.db.person;

import com.google.common.base.Preconditions;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.NodeRepository;
import pl.recommendations.db.RelationshipType;
import pl.recommendations.db.SuggestionType;
import pl.recommendations.db.interest.InterestNode;

import java.util.Collection;
import java.util.List;

@Transactional
public interface PersonNodeRepository extends NodeRepository {
    @Query("match (u)-[:" + RelationshipType.FRIENDSHIP + "]->(u2) " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<PersonNode> getFriendsOf(Long id);

    @Query("match (u)-[r:" + RelationshipType.SUGGESTION + "]->(u2) " +
            "where u.uuid = {0} " +
            "return r")
    Collection<SuggestionEdge> getSuggestionOf(Long id);

    @Query("match (u)-[r:" + RelationshipType.SUGGESTION + "]->(u2) " +
            "where u.uuid = {0} and r.type = {1}" +
            "return u2 ")
    Collection<PersonNode> getSuggestionOf(Long id, String suggestionType);

    @Query("match (u)-[:FRIENDSHIP{type:'RETAINED'}]->(u2) " +
            "return u \n" +
            "limit 50")
    Collection<PersonNode> getPeopleWithRetainedEdges();

    @Query("match (u)-[:FRIENDSHIP{type:'RETAINED'}]->(u2) \n" +
            "where u.uuid = {0} \n" +
            "return u2\n")
    Collection<PersonNode> getRetainedEdges(Long id);

    PersonNode findByUuid(Long uuid);

  default void clear(){
      delete(findAll());
  }

    default void addFriend(PersonNode personNode, PersonNode friend) {
        if (friend != null && !friend.equals(personNode)) {
            FriendshipEdge relationship = new FriendshipEdge();
            relationship.setPersonNode(personNode);
            relationship.setFriend(friend);
            personNode.addFriendship(relationship);
        }
    }
    @Query("match (b)-[r:FRIENDSHIP{type:'RETAINED'}]-(c)\n" +
            "\twith count(r) as totalRet\n" +
            "match (a)-[r:FRIENDSHIP{type:'RETAINED'}]-(c)-[s:SUGGESTION{type:{0}}]-(a)\n" +
            "\treturn count(r) * 1.0 / totalRet")
    Double getSuggestionQuality(SuggestionType type);

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

    default void addSuggestions(PersonNode node1, List<SuggestionEdge> ss) {
        node1.addSuggestions(ss);
    }


}
