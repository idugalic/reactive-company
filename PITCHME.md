### Reactive Programming
### And
### Reactive Systems

<span style="color:gray">Lab</span>

---

### Reactive Programming

is about non-blocking applications that are:

  - asynchronous
  - message-driven
  - require a small number of threads to scale vertically
  - use backpressure - which is a mechanism to ensure producers don’t overwhelm consumers

+++

### From imperative to declarative async composition of logic - service

```java
@GetMapping("/blogposts")
Flux<BlogPost> list() {
	LOG.info("Received request: BlogPost - List");
	try {
		return this.blogPostRepository.findAll().log();
	} finally {
		LOG.info("Request pocessed: BlogPost - List");
	}
}
```

+++

### From imperative to declarative async composition of logic - log

A possible log output we could see is:
![Log - Reactive](logs-reactive.png?raw=true)

As we can see the output of the controller method is evaluated after its execution in a different thread too!

+++

### From imperative to declarative async composition of logic

<ol>
<li class="fragment" data-fragment-index="1">We can no longer think in terms of a linear execution model where one request is handled by one thread</li>
<li class="fragment" data-fragment-index="2">The reactive streams will be handled by a lot of threads in their lifecycle</li>
<li class="fragment" data-fragment-index="3">We no longer can rely on thread affinity for things like the security context or transaction handling</li>
</ol>

---

### Reactive Systems

are:

  - responsive
  - resilient
  - elastic
  - message driven
  
+++

### Reactive Systems Traits

![Reactive Traits](reactive-traits.svg)

+++

### Reactive Systems - Responsive

  - The system responds in a timely manner if at all possible.

+++

### Reactive Systems - Resilient

  - The system stays responsive in the face of failure.

+++

### Reactive Systems - Elastic

  - The system stays responsive under varying workload

+++

### Reactive Systems - Message Driven

  - Reactive Systems rely on asynchronous message-passing to establish a boundary between components that ensures loose coupling, isolation and location transparency. 

---

### Summary

 - Reactive programming offers productivity for developers—through performance and resource efficiency—at the component level for internal logic and dataflow transformation. 
 - Reactive systems offer productivity for architects and DevOps practitioners—through resilience and elasticity—at the system level
 
