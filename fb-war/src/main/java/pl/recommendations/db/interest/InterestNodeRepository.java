package pl.recommendations.db.interest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.NodeRepository;
import pl.recommendations.db.relationships.Contrast;
import pl.recommendations.db.relationships.Similarity;

@Transactional
@Component
public interface InterestNodeRepository extends NodeRepository {
    InterestNode findByUuid(Long uuid);
    InterestNode findByName(String interestName);
}
