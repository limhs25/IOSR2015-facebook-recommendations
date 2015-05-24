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
import pl.recommendations.db.queue.exceptions.EmptyQueueException;
import pl.recommendations.db.queue.exceptions.EntityAlreadyEnqueuedException;
import pl.recommendations.db.queue.exceptions.FullQueueException;

import static org.junit.Assert.assertEquals;

/**
 * Created by marekmagik on 2015-05-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@PropertySource("classpath:conf/neo4j.properties")
@Transactional
public class HighPriorityUsersQueueTest {

    @Autowired
    @Qualifier("hpUsersQueue")
    private PersistentQueue persistentQueue;

    @Before
    public void prepare() {
        persistentQueue.reload();
    }

    @Test
    public void saveInDB() throws EmptyQueueException, EntityAlreadyEnqueuedException, FullQueueException {
        Long userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            persistentQueue.enqueue(userId++, 0, 0);
        }

        userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = persistentQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        persistentQueue.reload();

        userId = 100L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            persistentQueue.enqueue(userId++, 0, 0);
        }

        persistentQueue.reload();

        userId = 100L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            QueueNode node = persistentQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

    }

    @Test(expected = FullQueueException.class)
    public void saveInDB_expectFullQueueException() throws EntityAlreadyEnqueuedException, FullQueueException {
        Long userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE; i++) {
            persistentQueue.enqueue(userId++, 0, 0);
        }

        persistentQueue.reload();

        persistentQueue.enqueue(userId, 0, 0);
    }

    @Test
    public void saveInDB_multipleReloads() throws EntityAlreadyEnqueuedException, EmptyQueueException, FullQueueException {
        Long userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            persistentQueue.enqueue(userId++, 0, 0);
        }

        persistentQueue.reload();

        userId = 2L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            persistentQueue.enqueue(userId++, 0, 0);
        }

        persistentQueue.reload();

        userId = 123L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = persistentQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }

        persistentQueue.reload();

        userId = 2L;

        for (int i = 0; i < PersistentQueue.QUEUE_SIZE / 2; i++) {
            QueueNode node = persistentQueue.dequeue();
            assertEquals(userId++, node.getUserId());
        }
    }

    @Test(expected = EntityAlreadyEnqueuedException.class)
    public void saveInDB_sameIDs() throws EntityAlreadyEnqueuedException, FullQueueException {
        persistentQueue.enqueue(500L, 0, 0);
        persistentQueue.enqueue(500L, 0, 0);
    }


}
