package pl.recommendations.db.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import pl.recommendations.db.RelationshipType;

import java.util.Collection;

public interface UserRepository extends GraphRepository<User> {
    User findByName(String name);

    @Query("start u = node({0}) " +
            "match u-[" + RelationshipType.FRIENDSHIP + "]->u2 " +
            "return u2")
    Collection<User> getFriendsOf(Long id);
}
