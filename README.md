# reactive-company

## Reactive programming

In a nutshell reactive programming is about non-blocking, event-driven applications that scale with a small number of threads with backpressure as a key ingredient that aims to ensure producers do not overwhelm consumers. [The Reactive Streams](https://github.com/reactive-streams/reactive-streams-jvm) specification (also adopted in Java 9) enables the ability to communicate demand across layers and libraries from different providers. For example an HTTP connection writing to a client can communicate its availability to write all the way upstream to a data repository fetching data from a database so that given a slow HTTP client the repository can slow down too or even pause. For a more extensive introduction to reactive programming check Dave Syer’s multipart series [“Notes on Reactive Programming”](https://spring.io/blog/2016/06/07/notes-on-reactive-programming-part-i-the-reactive-landscape).



## Running instructions

```bash
$ cd reactive-company
$ ./mvnw spring-boot:run
```


## References

- https://spring.io/blog/2016/11/28/going-reactive-with-spring-data
- https://spring.io/blog/2016/07/28/reactive-programming-with-spring-5-0-m1
- http://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#web-reactive


