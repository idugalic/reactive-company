package com.idugalic.web.blog;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idugalic.domain.blog.BlogPost;
import com.idugalic.domain.blog.BlogPostRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author idugalic
 * 
 *         Blog post controller
 * 
 *         On the server-side WebFlux supports 2 distinct programming models:
 *
 *         - Annotation-based with @Controller and the other annotations
 *         supported also with Spring MVC - Functional, Java 8 lambda style
 *         routing and handling
 * 
 *         This is example of 'Annotation-based with @Controller' programming
 *         model.
 *
 */
@RestController
public class BlogPostController {

	private static final Logger LOG = LoggerFactory.getLogger(BlogPostController.class);

	private final BlogPostRepository blogPostRepository;

	public BlogPostController(BlogPostRepository blogPostRepository) {
		this.blogPostRepository = blogPostRepository;
	}

	@PostMapping("/blogposts")
	Mono<Void> create(@RequestBody Publisher<BlogPost> blogPostStream) {
		LOG.info("BlogPost - Create");
		return this.blogPostRepository.save(blogPostStream).then();
	}

	@GetMapping("/blogposts")
	Flux<BlogPost> list() {
		LOG.info("BlogPost - List");
		return this.blogPostRepository.findAll();
	}

	@GetMapping("/blogposts/{id}")
	Mono<BlogPost> findById(@PathVariable String id) {
		LOG.info("BlogPost - FindById");
		return this.blogPostRepository.findOne(id);
	}

	@GetMapping("/blogposts/search/bytitle")
	Flux<BlogPost> findByTitle(@RequestParam String title) {
		LOG.info("BlogPost - FindByTitle");
		return this.blogPostRepository.findByTitle(Mono.just(title));
	}
}
