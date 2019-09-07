# reactive-spymemcached

A proof of concept wrapper around spymemcached, the popular Memcached Java client, which adapts it to using reactive.
It uses project reactor as its reactive framework in conjunction with spymemcached's built-in listeners to bridge
between a `Future` and `Mono` without blocking on the `future.get()` invocation. It also propagates subscription
cancellation back to the original future, ensuring the client is able avoid unnecessary.


