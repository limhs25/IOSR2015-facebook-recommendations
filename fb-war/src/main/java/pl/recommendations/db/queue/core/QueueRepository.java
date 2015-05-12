package pl.recommendations.db.queue.core;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.queue.types.QueueType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by marekmagik on 2015-05-10.
 */
@Transactional
public interface QueueRepository extends GraphRepository<QueueNode> {
    QueueNode findByUuid(String id);

    default Set<QueueNode> getNodesForSpecifiedQueue(QueueType queueType) {
        Set<QueueNode> nodes = new HashSet<>();
        for (QueueNode node : findAll()) {
            if (queueType != null && node.getQueueType().equals(queueType)) {
                nodes.add(node);
            }
        }
        return nodes;
    }
}
