package micronaut.cache.hazelcast.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.micronaut.cache.SyncCache;
import io.micronaut.cache.hazelcast.HazelcastCacheManager;
import io.micronaut.test.annotation.MicronautTest;
import micronaut.cache.hazelcast.Application;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

//tag::test[]
@MicronautTest(application = Application.class)
public class MessageServiceTests {

    static final String MESSAGE_TITLE = "myMessage";

    @Inject
    private MessageService messageService;
    @Inject
    private HazelcastCacheManager hazelcastManager;

    static HazelcastInstance hazelcastServerInstance;

    @BeforeAll
    public static void setupSpec() {
        hazelcastServerInstance = Hazelcast.newHazelcastInstance(); // <1>
    }

    @AfterAll
    public static void cleanupSpec() {
        hazelcastServerInstance.shutdown(); // <2>
    }

    @BeforeEach
    public void setup() {
        messageService.setInvocationCounter(0);
        SyncCache<IMap<Object, Object>> cache = hazelcastManager.getCache("my-cache");
        cache.invalidateAll();
    }

    @Test
    void testMethodCached() {
        // when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE);

        // then: 'method runs by getting the counter increasing'
        assertEquals(message,"myMessage_FromInsideMethodReturnMessage");
        assertEquals(1, messageService.getInvocationCounter());

        // when: 'called a second time'
        message = messageService.returnMessage(MESSAGE_TITLE);

        // then: 'the cache is accessed and the method doesnt run again'
        assertEquals(message, "myMessage_FromInsideMethodReturnMessage");
        assertEquals(1, messageService.getInvocationCounter());

        // when: 'called again with a different param'
        message = messageService.returnMessage(MESSAGE_TITLE + "Again");

        // then: 'method is invoked bc cache doesnt have the stored value'
        assertEquals(message, "myMessageAgain_FromInsideMethodReturnMessage");
        assertEquals(2, messageService.getInvocationCounter());
    }
    //end::test[]

    @Test
    void testMethodCacheInvalidate() {
        // when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE);

        // then: 'method runs by getting the counter increasing'
        assertEquals(message, "myMessage_FromInsideMethodReturnMessage");
        assertEquals(messageService.getInvocationCounter(), 1);

        // when: 'call a different method with @CacheInvalidate'
        message = messageService.invalidateAndReturnMessage(MESSAGE_TITLE);

        // then: 'method is invoked because cache was invalidated and evicted'
        assertEquals(message, "myMessage_FromInsideMethodInvalidateAndReturnMessage");
        assertEquals(messageService.getInvocationCounter(), 2);
    }

    @Test
    void testMethodCachePut() {
        // when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE);

        // then: 'method runs by getting the counter increasing'
        assertEquals(message, "myMessage_FromInsideMethodReturnMessage");
        assertEquals(messageService.getInvocationCounter(), 1);

        // when: 'call a different method with @CachePut'
        message = messageService.putReturnMessage(MESSAGE_TITLE);

        // then: 'method is invoked because cache it will put a new entry'
        assertEquals(message, "myMessage_FromInsideMethodPutAndReturnMessage");
        assertEquals(messageService.getInvocationCounter(), 2);
    }
}
