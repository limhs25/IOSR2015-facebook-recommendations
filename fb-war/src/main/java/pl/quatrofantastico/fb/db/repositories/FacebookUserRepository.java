package pl.quatrofantastico.fb.db.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import pl.quatrofantastico.fb.db.model.nodes.FacebookUser;

public interface FacebookUserRepository extends GraphRepository<FacebookUser> {
    FacebookUser findByName(String name);

}
