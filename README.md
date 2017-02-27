# [projects](http://idugalic.github.io/projects)/reactive-company

This project is intended to demonstrate best practices for building a reactive web application with Spring platform.

## Table of Contents

   * [Reactive programming](#reactive-programming)
      * [Why now?](#why-now)
      * [Spring WebFlux (web reactive) module](#spring-webflux-web-reactive-module)
         * [Server side](#server-side)
            * [Annotation based](#annotation-based)
            * [Functional](#functional)
         * [Client side](#client-side)
      * [Spring Reactive data](#spring-reactive-data)
   * [Running instructions](#running-instructions)
      * [Run the application by maven:](#run-the-application-by-maven)
      * [Run the application by Docker:](#run-the-application-by-docker)
      * [Browse the application:](#browse-the-application)
   * [References](#references)

## Reactive programming

In plain terms reactive programming is about [non-blocking](http://www.reactivemanifesto.org/glossary#Non-Blocking) applications that are [asynchronous](http://www.reactivemanifesto.org/glossary#Asynchronous) and [message-driven](http://www.reactivemanifesto.org/glossary#Message-Driven) and require a small number of threads to [scale](http://www.reactivemanifesto.org/glossary#Scalability) vertically (i.e. within the JVM) rather than horizontally (i.e. through clustering).

A key aspect of reactive applications is the concept of backpressure which is a mechanism to ensure producers don’t overwhelm consumers. For example in a pipeline of reactive components extending from the database to the HTTP response when the HTTP connection is too slow the data repository can also slow down or stop completely until network capacity frees up.

Reactive programming also leads to a major shift from imperative to declarative async composition of logic. It is comparable to writing blocking code vs using the CompletableFuture from Java 8 to compose follow-up actions via lambda expressions.

For a longer introduction check the blog series [“Notes on Reactive Programming”](https://spring.io/blog/2016/06/07/notes-on-reactive-programming-part-i-the-reactive-landscape) by Dave Syer.

Read the ['Reactive Manifesto'](http://www.reactivemanifesto.org/).

### Why now?

What is driving the rise of Reactive in Enterprise Java? Well, it’s not (all) just a technology fad — people jumping on the bandwagon with the shiny new toys. The driver is efficient resource utilization, or in other words, spending less money on servers and data centres. The promise of Reactive is that you can do more with less, specifically you can process higher loads with fewer threads. This is where the intersection of Reactive and non-blocking, asynchronous I/O comes to the foreground. For the right problem, the effects are dramatic. For the wrong problem, the effects might go into reverse (you actually make things worse). Also remember, even if you pick the right problem, there is no such thing as a free lunch, and Reactive doesn’t solve the problems for you, it just gives you a toolbox that you can use to implement solutions.


### Spring WebFlux (web reactive) module

Spring Framework 5 includes a new spring-webflux module. The module contains support for reactive HTTP and WebSocket clients as well as for reactive server web applications including REST, HTML browser, and WebSocket style interactions.

#### Server side
On the server-side WebFlux supports 2 distinct programming models:

- Annotation-based with @Controller and the other annotations supported also with Spring MVC
- Functional, Java 8 lambda style routing and handling

##### Annotation based
```java
@RestController
public class BlogPostController {

	private final BlogPostRepository blogPostRepository;

	public BlogPostController(BlogPostRepository blogPostRepository) {
		this.blogPostRepository = blogPostRepository;
	}

	@PostMapping("/blogposts")
	Mono<Void> create(@RequestBody Publisher<BlogPost> blogPostStream) {
		return this.blogPostRepository.save(blogPostStream).then();
	}
	
	@GetMapping("/blogposts")
	Flux<BlogPost> list() {
		return this.blogPostRepository.findAll();
	}
	
	@GetMapping("/blogposts/{id}")
	Mono<BlogPost> findById(@PathVariable String id) {
		return this.blogPostRepository.findOne(id);
	}
}
```
##### Functional

Functional programming model is not implemented within this application. I am not sure if it is posible to have both models in one application.

Both programming models are executed on the same reactive foundation that adapts non-blocking HTTP runtimes to the Reactive Streams API.

#### Client side

WebFlux includes a functional, reactive WebClient that offers a fully non-blocking and reactive alternative to the RestTemplate. It exposes network input and output as a reactive ClientHttpRequest and ClientHttpRespones where the body of the request and response is a Flux<DataBuffer> rather than an InputStream and OutputStream. In addition it supports the same reactive JSON, XML, and SSE serialization mechanism as on the server side so you can work with typed objects.

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

	WebTestClient webTestClient;
	
	List<BlogPost> expectedBlogPosts;
	List<Project> expectedProjects;
	
	@Autowired
	BlogPostRepository blogPostRepository;
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Before
	public void setup() {
		webTestClient = WebTestClient.bindToController(new BlogPostController(blogPostRepository), new ProjectController(projectRepository)).build();
		
		expectedBlogPosts = blogPostRepository.findAll().collectList().block();
		expectedProjects = projectRepository.findAll().collectList().block();

	}

	@Test
	public void listAllBlogPostsIntegrationTest() {
		this.webTestClient.get().uri("/blogposts")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody(BlogPost.class).list().isEqualTo(expectedBlogPosts);
	}
	
	@Test
	public void listAllProjectsIntegrationTest() {
		this.webTestClient.get().uri("/projects")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody(Project.class).list().isEqualTo(expectedProjects);
	}
	
	@Test
	public void streamAllBlogPostsIntegrationTest() throws Exception {
		FluxExchangeResult<BlogPost> result = this.webTestClient.get()
			.uri("/blogposts")
			.accept(TEXT_EVENT_STREAM)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(TEXT_EVENT_STREAM)
			.expectBody(BlogPost.class)
			.returnResult();

		StepVerifier.create(result.getResponseBody())
			.expectNext(expectedBlogPosts.get(0), expectedBlogPosts.get(1))
			.expectNextCount(1)
			.consumeNextWith(blogPost -> assertThat(blogPost.getAuthorId(), endsWith("4")))
			.thenCancel()
			.verify();
	}
	
	...
}

```
Please note that webClient is requesting [Server-Sent Events](https://community.oracle.com/docs/DOC-982924) (text/event-stream).
We could stream individual JSON objects (application/stream+json) but that would not be a valid JSON document as a whole and a browser client has no way to consume a stream other than using Server-Sent Events or WebSocket.

### Spring Reactive data

Spring Data Kay M1 is the first release ever that comes with support for reactive data access. Its initial set of supported stores — MongoDB, Apache Cassandra and Redis 

The repositories programming model is the most high-level abstraction Spring Data users usually deal with. They’re usually comprised of a set of CRUD methods defined in a Spring Data provided interface and domain-specific query methods.

In contrast to the traditional repository interfaces, a reactive repository uses reactive types as return types and can do so for parameter types, too.

```java
public interface BlogPostRepository extends ReactiveSortingRepository<BlogPost, String>{
    
	Flux<BlogPost> findByTitle(Mono<String> title);

}
```

## Running instructions

This application is using embedded mongo database for testing only. 
You have to install and run mongo database before you run the application.

```bash
$ brew install mongodb
$ mongod
```

### Run the application by maven:

```bash
$ cd reactive-company
$ ./mvnw spring-boot:run
```

Build Docker images (optional):

```bash
$ ./mvnw clean install
$ DOCKER_HOST=unix:///var/run/docker.sock mvn docker:build
```

or build and push images to docker hub via maven (requires username and password of a docker repository):

```bash
$ DOCKER_HOST=unix:///var/run/docker.sock mvn docker:build -DpushImage
```

### Run the application by Docker:

```bash
$ cd reactive-company
$ ./docker-swarm.sh
```

### Browse the application:

Blog posts:
```bash
http://localhost:8080/blogposts
```

Projects:
```bash
http://localhost:8080/projects
```

## References

- https://blog.redelastic.com/what-is-reactive-programming-bc9fa7f4a7fc#.xcjlvcg7s
- http://www.reactivemanifesto.org/
- http://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html
- https://spring.io/blog/2016/04/19/understanding-reactive-types
- https://spring.io/blog/2016/11/28/going-reactive-with-spring-data
- https://spring.io/blog/2016/07/28/reactive-programming-with-spring-5-0-m1
- http://www.ducons.com/blog/tests-and-thoughts-on-asynchronous-io-vs-multithreading
- https://community.oracle.com/docs/DOC-982924



