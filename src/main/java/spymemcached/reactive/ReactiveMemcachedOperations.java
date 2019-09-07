package spymemcached.reactive;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClientIF;
import net.spy.memcached.transcoders.Transcoder;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @see MemcachedClientIF
 */
public interface ReactiveMemcachedOperations {
    /**
     * @see MemcachedClientIF#append(long, String, Object)
     */
    Mono<Boolean> append(long cas, String key, Object val);

    /**
     * @see MemcachedClientIF#append(String, Object)
     */
    Mono<Boolean> append(String key, Object val);

    /**
     * @see MemcachedClientIF#append(long, String, Object, Transcoder)
     */
    <T> Mono<Boolean> append(long cas, String key, T val, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#append(String, Object, Transcoder)
     */
    <T> Mono<Boolean> append(String key, T val, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#prepend(long, String, Object)
     */
    Mono<Boolean> prepend(long cas, String key, Object val);

    /**
     * @see MemcachedClientIF#prepend(String, Object)
     */
    Mono<Boolean> prepend(String key, Object val);

    /**
     * @see MemcachedClientIF#prepend(long, String, Object, Transcoder)
     */
    <T> Mono<Boolean> prepend(long cas, String key, T val, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#prepend(String, Object, Transcoder)
     */
    <T> Mono<Boolean> prepend(String key, T val, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncCAS(String, long, int, Object, Transcoder)
     */
    <T> Mono<CASResponse> cas(String key, long casId, T value, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncCAS(String, long, Object)
     */
    Mono<CASResponse> cas(String key, long casId, Object value);

    /**
     * @see MemcachedClientIF#asyncCAS(String, long, int, Object)
     */
    Mono<CASResponse> cas(String key, long casId, int exp, Object value);

    /**
     * @see MemcachedClientIF#asyncCAS(String, long, int, Object, Transcoder)
     */
    <T> Mono<CASResponse> cas(String key, long casId, int exp, T value, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#add(String, int, Object, Transcoder)
     */
    <T> Mono<Boolean> add(String key, int exp, T o, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#add(String, int, Object)
     */
    Mono<Boolean> add(String key, int exp, Object o);

    /**
     * @see MemcachedClientIF#set(String, int, Object, Transcoder)
     */
    <T> Mono<Boolean> set(String key, int exp, T o, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#set(String, int, Object)
     */
    Mono<Boolean> set(String key, int exp, Object o);

    /**
     * @see MemcachedClientIF#replace(String, int, Object, Transcoder)
     */
    <T> Mono<Boolean> replace(String key, int exp, T o, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#replace(String, int, Object)
     */
    Mono<Boolean> replace(String key, int exp, Object o);

    /**
     * @see MemcachedClientIF#asyncGet(String, Transcoder)
     */
    <T> Mono<T> get(String key, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncGet(String)
     */
    Mono<Object> get(String key);

    /**
     * @see MemcachedClientIF#asyncGetAndTouch(String, int)
     */
    Mono<CASValue<Object>> getAndTouch(String key, int exp);

    /**
     * @see MemcachedClientIF#asyncGetAndTouch(String, int, Transcoder)
     */
    <T> Mono<CASValue<T>> getAndTouch(String key, int exp, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncGets(String, Transcoder)
     */
    <T> Mono<CASValue<T>> gets(String key, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncGets(String)
     */
    Mono<CASValue<Object>> gets(String key);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Iterator, Iterator)
     */
    <T> Mono<Map<String, T>> getBulk(Iterator<String> keys, Iterator<Transcoder<T>> tcs);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Collection, Iterator)
     */
    <T> Mono<Map<String, T>> getBulk(Collection<String> keys, Iterator<Transcoder<T>> tcs);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Iterator, Transcoder)
     */
    <T> Mono<Map<String, T>> getBulk(Iterator<String> keys, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Collection, Transcoder)
     */
    <T> Mono<Map<String, T>> getBulk(Collection<String> keys, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Iterator)
     */
    Mono<Map<String, Object>> getBulk(Iterator<String> keys);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Collection)
     */
    Mono<Map<String, Object>> getBulk(Collection<String> keys);

    /**
     * @see MemcachedClientIF#asyncGetBulk(Transcoder, String...)
     */
    <T> Mono<Map<String, T>> getBulk(Transcoder<T> tc, String... keys);

    /**
     * @see MemcachedClientIF#asyncGetBulk(String...)
     */
    Mono<Map<String, Object>> getBulk(String... keys);

    /**
     * @see MemcachedClientIF#touch(String, int, Transcoder)
     */
    <T> Mono<Boolean> touch(String key, int exp, Transcoder<T> tc);

    /**
     * @see MemcachedClientIF#touch(String, int)
     */
    <T> Mono<Boolean> touch(String key, int exp);

    /**
     * @see MemcachedClientIF#asyncIncr(String, long)
     */
    Mono<Long> incr(String key, long by);

    /**
     * @see MemcachedClientIF#asyncIncr(String, int)
     */
    Mono<Long> incr(String key, int by);

    /**
     * @see MemcachedClientIF#asyncDecr(String, long)
     */
    Mono<Long> decr(String key, long by);

    /**
     * @see MemcachedClientIF#asyncDecr(String, int)
     */
    Mono<Long> decr(String key, int by);

    /**
     * @see MemcachedClientIF#asyncIncr(String, long, long, int)
     */
    Mono<Long> incr(String key, long by, long def, int exp);

    /**
     * @see MemcachedClientIF#asyncIncr(String, int, long, int)
     */
    Mono<Long> incr(String key, int by, long def, int exp);

    /**
     * @see MemcachedClientIF#asyncDecr(String, long, long, int)
     */
    Mono<Long> decr(String key, long by, long def, int exp);

    /**
     * @see MemcachedClientIF#asyncDecr(String, int, long, int)
     */
    Mono<Long> decr(String key, int by, long def, int exp);

    /**
     * @see MemcachedClientIF#asyncIncr(String, long, long)
     */
    Mono<Long> incr(String key, long by, long def);

    /**
     * @see MemcachedClientIF#asyncIncr(String, int, long)
     */
    Mono<Long> incr(String key, int by, long def);

    /**
     * @see MemcachedClientIF#asyncDecr(String, long, long)
     */
    Mono<Long> decr(String key, long by, long def);

    /**
     * @see MemcachedClientIF#asyncDecr(String, int, long)
     */
    Mono<Long> decr(String key, int by, long def);

    /**
     * @see MemcachedClientIF#delete(String)
     */
    Mono<Boolean> delete(String key);

    /**
     * @see MemcachedClientIF#delete(String, long)
     */
    Mono<Boolean> delete(String key, long cas);

    /**
     * @see MemcachedClientIF#flush(int)
     */
    Mono<Boolean> flush(int delay);

    /**
     * @see MemcachedClientIF#flush()
     */
    Mono<Boolean> flush();
}
