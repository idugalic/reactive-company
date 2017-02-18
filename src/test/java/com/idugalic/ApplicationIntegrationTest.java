package com.idugalic;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.idugalic.domain.blog.BlogPost;
import com.idugalic.domain.blog.BlogPostRepository;
import com.idugalic.web.blog.BlogPostController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

	WebTestClient webClient;
	
	List<BlogPost> expected;
	
	@Autowired
	BlogPostRepository blogPostRepository;
	
	@Before
	public void setup() {
		webClient = WebTestClient.bindToController(new BlogPostController(blogPostRepository)).build();
		expected = blogPostRepository.findAll().collectList().block();

	}

	@Test
	public void listBlogPostsIntegrationTest() {
		this.webClient.get().uri("/blogposts")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeEquals(MediaType.APPLICATION_JSON_UTF8)
			.expectBody(BlogPost.class).list().isEqualTo(expected);
	}
	
	@Test
	public void streamBlogPostsIntegrationTest() throws Exception {
		ExchangeResult<Flux<BlogPost>> result = this.webClient.get()
			.uri("/blogposts")
			.accept(TEXT_EVENT_STREAM)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeEquals(TEXT_EVENT_STREAM)
			.expectBody(BlogPost.class)
			.returnResult();

		StepVerifier.create(result.getResponseBody())
			.expectNext(expected.get(0), expected.get(1))
			.expectNextCount(1)
			.consumeNextWith(blogPost -> assertThat(blogPost.getAuthorId(), endsWith("4")))
			.thenCancel()
			.verify();
	}
	
	@Test
	public void getBlogPostIntegrationTest() throws Exception {
		this.webClient.get().uri("/blogposts/"+expected.get(0).getId())
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentTypeEquals(MediaType.APPLICATION_JSON_UTF8)
				.expectBody(BlogPost.class).value().isEqualTo(expected.get(0));
	}
	
	@Test
	public void postBlogPostIntegrationTest() throws Exception {
		this.webClient.post().uri("/blogposts")
				.exchange(Mono.just(new BlogPost("authorId5", "title5", "content5", "tagString5")), BlogPost.class)
				.expectStatus().isOk()
				.expectBody().isEmpty();
	}

}
