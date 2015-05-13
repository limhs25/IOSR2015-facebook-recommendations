package pl.recommendations.db.interest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.NodeRepository;

@Transactional
@Component
public interface InterestNodeRepository extends NodeRepository {
    InterestNode findByName(String interestName);
}
