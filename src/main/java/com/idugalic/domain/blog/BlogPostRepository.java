package com.idugalic.domain.blog;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlogPostRepository extends ReactiveSortingRepository<BlogPost, String>{
    
	Flux<BlogPost> findByTitle(Mono<String> title);

}
