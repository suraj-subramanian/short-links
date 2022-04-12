package com.surajsbmn.shorturls;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = LinkController.class)
public class LinkControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private LinkService linkService;

    @Test
    public void shortenLink() {
        when(linkService.shortenLink("https://spring.io")).thenReturn(Mono.just("http://localhost:8080/abcdef"));
        client.post()
                .uri("/link")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"link\": \"https://spring.io\"}")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.shortenedLink")
                .value(val -> Assert.assertEquals("http://localhost:8080/abcdef", val));
    }

    @Test
    public void redirectsToOriginalLink() {
        when(linkService.getOriginalLink("abcdef"))
                .thenReturn(Mono.just(new Link("https://spring.io", "abcdef")));
        client.get()
                .uri("/abcdef")
                .exchange()
                .expectStatus()
                .isPermanentRedirect()
                .expectHeader()
                .value("Location", location -> Assert.assertEquals("https://spring.io", location));
    }
}
