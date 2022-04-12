package com.surajsbmn.shorturls;

import reactor.core.publisher.Mono;

public interface LinkRepository {
    Mono<Link> save(Link link);

    Mono<Link> findByKey(String key);
}
