package pl.recommendations.db.queue.core.counter;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.queue.types.QueueType;

/**
 * Created by marekmagik on 2015-05-10.
 */
@Transactional
public interface CursorRepository extends GraphRepository<CursorNode> {
    CursorNode findByUuid(String id);

    default CursorNode getCounter(QueueType queueType) {
        CursorNode counter = findByUuid(queueType.getCounterId());
        if (counter == null) {
            counter = new CursorNode(queueType);
            saveCounter(counter);
        }
        return findByUuid(queueType.getCounterId());
    }

    default void saveCounter(CursorNode counter) {
        save(counter);
    }

}
