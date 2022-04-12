package com.surajsbmn.shorturls;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LinkService {

    private final String baseUrl;

    private final LinkRepository linkRepository;

    public LinkService(@Value("${app.baseUrl}") String baseUrl, LinkRepository linkRepository) {
        this.baseUrl = baseUrl;
        this.linkRepository = linkRepository;
    }

    Mono<String> shortenLink(String link) {
        String randomKey = RandomStringUtils.randomAlphabetic(6);
        if (!link.startsWith("http"))
            link = "http://" + link;
        // save to db
        return linkRepository.save(new Link(link, randomKey)).map(res -> baseUrl + res.getKey());
    }

    Mono<Link> getOriginalLink(String key) {
        return linkRepository.findByKey(key);
    }
}
