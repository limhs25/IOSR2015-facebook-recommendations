package pl.recommendations.db;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.db.queue.core.PersistentQueue;
import pl.recommendations.db.queue.core.QueueNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marekmagik on 2015-05-13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class MultipleQueuesTest {

    @Autowired
    @Qualifier("hpUsersQueue")
    private PersistentQueue highPriorityQueue;

    @Autowired
    @Qualifier("lpUsersQueue")
    private PersistentQueue lowPriorityQueue;


    @Before
    public void reload() {
        highPriorityQueue.reload();
        lowPriorityQueue.reload();
    }

    @Test
    public void saveAndGetSimultaneously() {

        Long userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            highPriorityQueue.enqueue(userId, 0, 0);
            lowPriorityQueue.enqueue(userId++, 0, 0);
        }

        reload();

        userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = lowPriorityQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        reload();

        userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = highPriorityQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        userId = 100L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            highPriorityQueue.enqueue(userId, 0, 0);
            lowPriorityQueue.enqueue(userId++, 0, 0);
        }

        reload();

        userId = 100L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            QueueNode node = highPriorityQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        userId = 100L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            QueueNode node = lowPriorityQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        assertTrue(highPriorityQueue.isEmpty());
        assertTrue(lowPriorityQueue.isEmpty());

        reload();

        assertTrue(highPriorityQueue.isEmpty());
        assertTrue(lowPriorityQueue.isEmpty());
    }

}
