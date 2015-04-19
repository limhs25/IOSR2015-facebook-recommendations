package pl.recommendations.db.person;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import pl.recommendations.db.RelationshipType;

import java.util.Collection;

public interface PersonRepository extends GraphRepository<Person> {
    Person findByUuid(Long uuid);

    @Query("match u-[" + RelationshipType.FRIENDSHIP + "]->u2 " +
            "where u.uuid = {0} " +
            "return u2")
    Collection<Person> getFriendsOf(Long id);


}
