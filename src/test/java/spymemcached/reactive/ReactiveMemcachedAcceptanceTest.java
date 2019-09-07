package spymemcached.reactive;

import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@Testcontainers
public class ReactiveMemcachedAcceptanceTest {

    @Container
    private static GenericContainer<?> memcachedContainer = new GenericContainer("memcached:1.5")
            .withExposedPorts(11211);

    private MemcachedClient memcachedClient;
    private ReactiveMemcachedTemplate memcachedTemplate;

    @BeforeEach
    void setupClient() throws IOException {
        InetSocketAddress address = new InetSocketAddress(
                memcachedContainer.getContainerIpAddress(),
                memcachedContainer.getMappedPort(11211));
        memcachedClient = new MemcachedClient(address);
        memcachedTemplate = new ReactiveMemcachedTemplate(memcachedClient);
    }

    @AfterEach
    void tearDown() {
        memcachedClient.shutdown();
    }

    @Test
    void sanityTest() {
        Mono<Object> mono3 = memcachedTemplate.get("key");
        Mono<Boolean> mono2 = memcachedTemplate.set("key", 60, 1);
        Mono<Boolean> mono1 = memcachedTemplate.set("key", 60, 2);
        assertThat(memcachedClient.get("key"), nullValue());
        Object result = mono1.then(mono2).then(mono3).block();
        assertThat(result, equalTo(1));
    }

}
