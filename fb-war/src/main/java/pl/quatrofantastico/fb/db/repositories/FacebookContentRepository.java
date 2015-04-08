package pl.quatrofantastico.fb.db.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import pl.quatrofantastico.fb.db.model.FacebookContent;

public interface FacebookContentRepository extends GraphRepository<FacebookContent> {
    FacebookContent findByName(String name);}
