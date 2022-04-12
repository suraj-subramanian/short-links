package com.surajsbmn.shorturls;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkServiceTest {

    private LinkRepository linkRepository = mock(LinkRepository.class);

    private LinkService linkService = new LinkService("http://localhost:8080/", linkRepository);

    @Before
    public void setup() {
        when(linkRepository.save(any()))
                .thenAnswer((Answer<Mono<Link>>) invocationOnMock -> Mono.just((Link) invocationOnMock.getArguments()[0]));
    }

    @Test
    public void shortenLink() {
        StepVerifier.create(linkService.shortenLink("https://spring.io"))
                .expectNextMatches(res -> res != null && res.startsWith("http://localhost:8080/"))
                .expectComplete()
                .verify();
    }
}
