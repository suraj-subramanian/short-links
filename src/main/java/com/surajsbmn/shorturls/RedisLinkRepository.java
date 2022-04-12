package com.surajsbmn.shorturls;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisLinkRepository implements LinkRepository {

    private final ReactiveRedisOperations<String, String> redisOperations;

    @Override
    public Mono<Link> save(Link link) {
        return redisOperations.opsForValue()
                .set(link.getKey(), link.getOriginalLink(), Duration.ofDays(1))
                .map(__ -> link);
    }

    @Override
    public Mono<Link> findByKey(String key) {
        return redisOperations.opsForValue()
                .get(key)
                .map(res -> new Link(res, key));
    }

}
