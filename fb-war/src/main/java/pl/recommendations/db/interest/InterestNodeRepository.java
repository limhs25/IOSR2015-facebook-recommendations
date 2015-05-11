package pl.recommendations.db.interest;

import pl.recommendations.db.NodeRepository;
import pl.recommendations.db.relationships.Contrast;
import pl.recommendations.db.relationships.Similarity;


public interface InterestNodeRepository extends NodeRepository {
    InterestNode findByUuid(Long uuid);
    InterestNode findByName(String interestName);
}
