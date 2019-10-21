package micronaut.cache.hazelcast.service;

import io.micronaut.cache.hazelcast.HazelcastClientConfiguration;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;

import javax.inject.Singleton;

@Singleton
public class HazelcastCacheAdditionalSettings implements BeanCreatedEventListener<HazelcastClientConfiguration> { // <1>

    @Override
    public HazelcastClientConfiguration onCreated(BeanCreatedEvent<HazelcastClientConfiguration> event) {
        HazelcastClientConfiguration hazelcastClientConfiguration = event.getBean(); // <2>
        hazelcastClientConfiguration.getNetworkConfig().setConnectionTimeout(8888); // <3>
        return hazelcastClientConfiguration;
    }
}
