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
import pl.recommendations.db.queue.PersistentQueueFacade;
import pl.recommendations.db.queue.core.PersistentQueue;
import pl.recommendations.db.queue.core.QueueNode;
import pl.recommendations.db.queue.exceptions.EmptyQueueException;
import pl.recommendations.db.queue.exceptions.EntityAlreadyEnqueuedException;
import pl.recommendations.db.queue.exceptions.FullQueueException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marekmagik on 2015-05-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class PersistentQueueFacadeTest {

    @Autowired
    @Qualifier("hpUsersQueue")
    private PersistentQueue highPriorityUserQueue;

    @Autowired
    @Qualifier("lpUsersQueue")
    private PersistentQueue lowPriorityUserQueue;

    @Autowired
    private PersistentQueueFacade queue;


    @Before
    public void reload() {
        highPriorityUserQueue.reload();
        lowPriorityUserQueue.reload();
    }

    @Test
    public void saveInHighLowPriorityQueue() throws EmptyQueueException, EntityAlreadyEnqueuedException, FullQueueException {

        Long userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            highPriorityUserQueue.enqueue(userId++, 0, 0);
        }

        queue.enqueueUser(300L, false);

        reload();

        userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = queue.dequeueUser();
            assertEquals(userId++, node.getUserId());
        }

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = highPriorityUserQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        reload();

        assertTrue(highPriorityUserQueue.isEmpty());

        userId = 300L;

        QueueNode node = queue.dequeueUser();
        assertEquals(userId, node.getUserId());
    }
}
