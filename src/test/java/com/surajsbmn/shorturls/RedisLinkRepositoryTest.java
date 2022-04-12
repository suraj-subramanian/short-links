package com.surajsbmn.shorturls;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLinkRepositoryTest {

    @Autowired
    private RedisLinkRepository linkRepository;

    @Test
    public void returnSameLinkAsArgument() {
        Link link = new Link("https://spring.io", "abcdef");
        StepVerifier.create(linkRepository.save(link))
                .expectNext(link)
                .verifyComplete();
    }

    @Test
    public void savesInRedis() {
        Link link = new Link("https://spring.io", "abcdef");
        StepVerifier.create(linkRepository.save(link).flatMap(__ -> linkRepository.findByKey(link.getKey())))
                .expectNext(link)
                .verifyComplete();
    }
}