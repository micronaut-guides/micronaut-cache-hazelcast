package micronaut.cache.hazelcast.service

import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.CachePut
import io.micronaut.cache.annotation.Cacheable

import javax.inject.Singleton

@Singleton
class MessageService {

    int invocationCounter = 0

    @Cacheable("my-cache") // <1>
    String returnMessage(String message) {
        ++invocationCounter
        message+"_FromInsideMethodReturnMessage"
    }

    @CacheInvalidate(value="my-cache", all=true) // <2>
    String invalidateAndReturnMessage(String message) {
        ++invocationCounter
        message+"_FromInsideMethodInvalidateAndReturnMessage"
    }

    @CachePut("my-cache") // <3>
    String putReturnMessage(String message) {
        ++invocationCounter
        message+"_FromInsideMethodPutAndReturnMessage"
    }
}