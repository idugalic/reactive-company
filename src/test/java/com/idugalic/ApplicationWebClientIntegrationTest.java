package com.idugalic;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.idugalic.domain.blog.BlogPost;
import com.idugalic.domain.blog.BlogPostRepository;
import com.idugalic.domain.project.Project;
import com.idugalic.domain.project.ProjectRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * 
 * @author idugalic
 *
 * The main purpose of this test class is to present the usage of {@link WebClient} interface.
 * 
 * There is a true integration test class {@link ApplicationIntegrationTest} that is using {@link WebTestClient} interface  
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationWebClientIntegrationTest {

	private WebClient webClient;
	
	@LocalServerPort
    private Integer port;
	
	@Autowired
	BlogPostRepository blogPostRepository;
	
	@Autowired
	ProjectRepository projectRepository;
	
    List<BlogPost> expectedBlogPosts;
    List<Project> expectedProjects;
    
	@Before
    public void setup() {
		webClient = WebClient.create("http://localhost:"+port);
		
		expectedBlogPosts = blogPostRepository.findAll().collectList().block();
		expectedProjects = projectRepository.findAll().collectList().block();
    }
	
	@Test
	public void streamBlogPostsIntegrationTest() throws Exception {
		Flux<BlogPost> result = this.webClient.get()
			.uri("/blogposts")
			.accept(TEXT_EVENT_STREAM)
			.exchange()
			.flatMap(bp->bp.bodyToFlux(BlogPost.class));

		StepVerifier.create(result)
			.expectNext(expectedBlogPosts.get(0), expectedBlogPosts.get(1))
			.expectNextCount(1)
			.consumeNextWith(blogPost -> assertThat(blogPost.getAuthorId(), endsWith("4")))
			.thenCancel()
			.verify();
	}
	
	@Test
	public void streamProjectsIntegrationTest() throws Exception {
		Flux<Project> result = this.webClient.get()
			.uri("/projects")
			.accept(TEXT_EVENT_STREAM)
			.exchange()
			.flatMap(bp->bp.bodyToFlux(Project.class));

		StepVerifier.create(result)
			.expectNext(expectedProjects.get(0), expectedProjects.get(1))
			.expectNextCount(1)
			.consumeNextWith(blogPost -> assertThat(blogPost.getName(), endsWith("4")))
			.thenCancel()
			.verify();
	}
}
