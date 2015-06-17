package pl.recommendations.db.interest;

import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.NodeRepository;

@Transactional
public interface InterestNodeRepository extends NodeRepository {
    InterestNode findByName(String interestName);

    default void clear(){
        delete(findAll());
    }
}
