package spymemcached.reactive;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.BulkFuture;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.Transcoder;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ReactiveMemcachedTemplate implements ReactiveMemcachedOperations {

    private final MemcachedClient memcachedClient;

    public ReactiveMemcachedTemplate(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    @Override
    public Mono<Boolean> append(long cas, String key, Object val) {
        return fromOperationFuture(() -> memcachedClient.append(cas, key, val));
    }

    @Override
    public Mono<Boolean> append(String key, Object val) {
        return fromOperationFuture(() -> memcachedClient.append(key, val));
    }

    @Override
    public <T> Mono<Boolean> append(long cas, String key, T val, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.append(cas, key, val, tc));
    }

    @Override
    public <T> Mono<Boolean> append(String key, T val, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.append(key, val, tc));
    }

    @Override
    public Mono<Boolean> prepend(long cas, String key, Object val) {
        return fromOperationFuture(() -> memcachedClient.prepend(cas, key, val));
    }

    @Override
    public Mono<Boolean> prepend(String key, Object val) {
        return fromOperationFuture(() -> memcachedClient.prepend(key, val));
    }

    @Override
    public <T> Mono<Boolean> prepend(long cas, String key, T val, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.prepend(cas, key, val, tc));
    }

    @Override
    public <T> Mono<Boolean> prepend(String key, T val, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.prepend(key, val, tc));
    }

    @Override
    public <T> Mono<CASResponse> cas(String key, long casId, T value, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.asyncCAS(key, casId, value, tc));
    }

    @Override
    public Mono<CASResponse> cas(String key, long casId, Object value) {
        return fromOperationFuture(() -> memcachedClient.asyncCAS(key, casId, value));
    }

    @Override
    public Mono<CASResponse> cas(String key, long casId, int exp, Object value) {
        return fromOperationFuture(() -> memcachedClient.asyncCAS(key, casId, exp, value));
    }

    @Override
    public <T> Mono<CASResponse> cas(String key, long casId, int exp, T value, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.asyncCAS(key, casId, exp, value, tc));
    }

    @Override
    public <T> Mono<Boolean> add(String key, int exp, T o, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.add(key, exp, o, tc));
    }

    @Override
    public Mono<Boolean> add(String key, int exp, Object o) {
        return fromOperationFuture(() -> memcachedClient.add(key, exp, o));
    }

    @Override
    public <T> Mono<Boolean> set(String key, int exp, T o, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.set(key, exp, o, tc));
    }

    @Override
    public Mono<Boolean> set(String key, int exp, Object o) {
        return fromOperationFuture(() -> memcachedClient.set(key, exp, o));
    }

    @Override
    public <T> Mono<Boolean> replace(String key, int exp, T o, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.replace(key, exp, o, tc));
    }

    @Override
    public Mono<Boolean> replace(String key, int exp, Object o) {
        return fromOperationFuture(() -> memcachedClient.replace(key, exp, o));
    }

    @Override
    public <T> Mono<T> get(String key, Transcoder<T> tc) {
        return fromGetFuture(() -> memcachedClient.asyncGet(key, tc));
    }

    @Override
    public Mono<Object> get(String key) {
        return fromGetFuture(() -> memcachedClient.asyncGet(key));
    }

    @Override
    public Mono<CASValue<Object>> getAndTouch(String key, int exp) {
        return fromOperationFuture(() -> memcachedClient.asyncGetAndTouch(key, exp));
    }

    @Override
    public <T> Mono<CASValue<T>> getAndTouch(String key, int exp, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.asyncGetAndTouch(key, exp, tc));
    }

    @Override
    public <T> Mono<CASValue<T>> gets(String key, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.asyncGets(key, tc));
    }

    @Override
    public Mono<CASValue<Object>> gets(String key) {
        return fromOperationFuture(() -> memcachedClient.asyncGets(key));
    }

    @Override
    public <T> Mono<Map<String, T>> getBulk(Iterator<String> keys, Iterator<Transcoder<T>> tcs) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys, tcs));
    }

    @Override
    public <T> Mono<Map<String, T>> getBulk(Collection<String> keys, Iterator<Transcoder<T>> tcs) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys, tcs));
    }

    @Override
    public <T> Mono<Map<String, T>> getBulk(Iterator<String> keys, Transcoder<T> tc) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys, tc));
    }

    @Override
    public <T> Mono<Map<String, T>> getBulk(Collection<String> keys, Transcoder<T> tc) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys, tc));
    }

    @Override
    public Mono<Map<String, Object>> getBulk(Iterator<String> keys) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys));
    }

    @Override
    public Mono<Map<String, Object>> getBulk(Collection<String> keys) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys));
    }

    @Override
    public <T> Mono<Map<String, T>> getBulk(Transcoder<T> tc, String... keys) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(tc, keys));
    }

    @Override
    public Mono<Map<String, Object>> getBulk(String... keys) {
        return fromBulkFuture(() -> memcachedClient.asyncGetBulk(keys));
    }

    @Override
    public <T> Mono<Boolean> touch(String key, int exp, Transcoder<T> tc) {
        return fromOperationFuture(() -> memcachedClient.touch(key, exp, tc));
    }

    @Override
    public <T> Mono<Boolean> touch(String key, int exp) {
        return fromOperationFuture(() -> memcachedClient.touch(key, exp));
    }

    @Override
    public Mono<Long> incr(String key, long by) {
        return fromOperationFuture(() -> memcachedClient.asyncIncr(key, by));
    }

    @Override
    public Mono<Long> incr(String key, int by) {
        return fromOperationFuture(() -> memcachedClient.asyncIncr(key, by));
    }

    @Override
    public Mono<Long> decr(String key, long by) {
        return fromOperationFuture(() -> memcachedClient.asyncDecr(key, by));
    }

    @Override
    public Mono<Long> decr(String key, int by) {
        return fromOperationFuture(() -> memcachedClient.asyncDecr(key, by));
    }

    @Override
    public Mono<Long> incr(String key, long by, long def, int exp) {
        return fromOperationFuture(() -> memcachedClient.asyncIncr(key, by, def, exp));
    }

    @Override
    public Mono<Long> incr(String key, int by, long def, int exp) {
        return fromOperationFuture(() -> memcachedClient.asyncIncr(key, by, def, exp));
    }

    @Override
    public Mono<Long> decr(String key, long by, long def, int exp) {
        return fromOperationFuture(() -> memcachedClient.asyncDecr(key, by, def, exp));
    }

    @Override
    public Mono<Long> decr(String key, int by, long def, int exp) {
        return fromOperationFuture(() -> memcachedClient.asyncDecr(key, by, def, exp));
    }

    @Override
    public Mono<Long> incr(String key, long by, long def) {
        return fromOperationFuture(() -> memcachedClient.asyncIncr(key, by, def));
    }

    @Override
    public Mono<Long> incr(String key, int by, long def) {
        return fromOperationFuture(() -> memcachedClient.asyncIncr(key, by, def));
    }

    @Override
    public Mono<Long> decr(String key, long by, long def) {
        return fromOperationFuture(() -> memcachedClient.asyncDecr(key, by, def));
    }

    @Override
    public Mono<Long> decr(String key, int by, long def) {
        return fromOperationFuture(() -> memcachedClient.asyncDecr(key, by, def));
    }

    @Override
    public Mono<Boolean> delete(String key) {
        return fromOperationFuture(() -> memcachedClient.delete(key));
    }

    @Override
    public Mono<Boolean> delete(String key, long cas) {
        return fromOperationFuture(() -> memcachedClient.delete(key, cas));
    }

    @Override
    public Mono<Boolean> flush(int delay) {
        return fromOperationFuture(() -> memcachedClient.flush(delay));
    }

    @Override
    public Mono<Boolean> flush() {
        return fromOperationFuture(memcachedClient::flush);
    }

    private <T> Mono<T> fromOperationFuture(Supplier<OperationFuture<T>> futureCreator) {
        return toMono(futureCreator, (sink, future) -> {
            try {
                future.addListener(ignored -> pipeToSync(future, sink));
            } catch (RuntimeException e) {
                sink.error(e);
            }
        });
    }

    private <T> Mono<T> fromGetFuture(Supplier<GetFuture<T>> futureCreator) {
        return toMono(futureCreator, (sink, future) -> {
            try {
                future.addListener(ignored -> pipeToSync(future, sink));
            } catch (RuntimeException e) {
                sink.error(e);
            }
        });
    }

    private <T> Mono<T> fromBulkFuture(Supplier<BulkFuture<T>> futureCreator) {
        return toMono(futureCreator, (sink, future) -> {
            try {
                future.addListener(ignored -> pipeToSync(future, sink));
            } catch (RuntimeException e) {
                sink.error(e);
            }
        });
    }

    private <T, F extends Future<T>> Mono<T> toMono(Supplier<F> futureCreator, BiConsumer<MonoSink<T>, F> callback) {
        return Mono.create(sink -> {
            F future = futureCreator.get();
            sink = sink.onCancel(() -> future.cancel(true));
            callback.accept(sink, future);
        });
    }

    private <T> void pipeToSync(Future<T> future, MonoSink<T> sink) {
        assert future.isDone();
        if (future.isCancelled()) {
            return;
        }
        try {
            System.out.println("pipeToSync");
            sink.success(future.get());
        } catch (ExecutionException | InterruptedException e) {
            sink.error(e.getCause() == null ? e : e.getCause());
        }
    }

}
