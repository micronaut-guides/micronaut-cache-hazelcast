package micronaut.cache.hazelcast.service;

import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;

import javax.inject.Singleton;

@Singleton
public class MessageService {

    private int invocationCounter = 0;

    @Cacheable("my-cache") // <1>
    public String returnMessage(String message) {
        ++invocationCounter;
        return message+"_FromInsideMethodReturnMessage";
    }

    @CacheInvalidate(value="my-cache", all=true) // <2>
    public String invalidateAndReturnMessage(String message) {
        ++invocationCounter;
        return message+"_FromInsideMethodInvalidateAndReturnMessage";
    }

    @CachePut("my-cache") // <3>
    public String putReturnMessage(String message) {
        ++invocationCounter;
        return message+"_FromInsideMethodPutAndReturnMessage";
    }

    public int getInvocationCounter() {
        return invocationCounter;
    }

    public void setInvocationCounter(int invocationCounter) {
        this.invocationCounter = invocationCounter;
    }
}