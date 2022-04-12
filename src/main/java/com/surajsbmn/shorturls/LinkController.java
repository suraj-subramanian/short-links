package com.surajsbmn.shorturls;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService service;

    @PostMapping("/link")
    Mono<CreateLinkResponse> create(@RequestBody CreateLinkRequest request) {
        return service.shortenLink(request.getLink()).map(CreateLinkResponse::new);
    }

    @GetMapping("/{key}")
    Mono<ResponseEntity<Object>> getLink(@PathVariable String key) {
        return service.getOriginalLink(key)
                .map(link -> ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                        .location(URI.create(link.getOriginalLink()))
                        .build())
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Link Not Found.\"}"));
    }

    @Value
    public static class CreateLinkRequest {
        private String link;
    }

    @Value
    public static class CreateLinkResponse {
        private String shortenedLink;
    }

}
